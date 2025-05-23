package cn.wnhyang.coolguard.system.service;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.stp.parameter.SaLogoutParameter;
import cn.dev33.satoken.stp.parameter.enums.SaLogoutRange;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.wnhyang.coolguard.common.enums.DeviceType;
import cn.wnhyang.coolguard.common.enums.UserType;
import cn.wnhyang.coolguard.common.util.RegexUtil;
import cn.wnhyang.coolguard.common.util.ServletUtils;
import cn.wnhyang.coolguard.redis.constant.RedisKey;
import cn.wnhyang.coolguard.satoken.Login;
import cn.wnhyang.coolguard.satoken.util.LoginUtil;
import cn.wnhyang.coolguard.system.dto.LoginLogCreateDTO;
import cn.wnhyang.coolguard.system.dto.UserCreateDTO;
import cn.wnhyang.coolguard.system.enums.login.LoginResult;
import cn.wnhyang.coolguard.system.enums.login.LoginType;
import cn.wnhyang.coolguard.system.login.LoginUser;
import cn.wnhyang.coolguard.system.vo.core.auth.EmailLoginVO;
import cn.wnhyang.coolguard.system.vo.core.auth.LoginReqVO;
import cn.wnhyang.coolguard.system.vo.core.auth.LoginRespVO;
import cn.wnhyang.coolguard.system.vo.core.auth.RegisterVO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

import static cn.wnhyang.coolguard.common.exception.GlobalErrorCode.UNAUTHORIZED;
import static cn.wnhyang.coolguard.common.exception.util.ServiceExceptionUtil.exception;
import static cn.wnhyang.coolguard.system.error.SystemErrorCode.*;


/**
 * @author wnhyang
 * @date 2023/7/26
 **/
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;

    private final LoginLogService loginLogService;

    private final ValueOperations<String, String> valueOperations;

    public LoginRespVO login(LoginReqVO reqVO) {
        String account = reqVO.getUsername();
        LoginUser user;
        LoginType loginType;
        if (ReUtil.isMatch(RegexUtil.MOBILE, account)) {
            user = userService.getLoginUser(account, account, "");
            loginType = LoginType.LOGIN_MOBILE;
        } else if (ReUtil.isMatch(RegexUtil.EMAIL, account)) {
            user = userService.getLoginUser(account, "", account);
            loginType = LoginType.LOGIN_EMAIL;
        } else {
            user = userService.getLoginUser(account, "", "");
            loginType = LoginType.LOGIN_USERNAME;
        }
        if (!user.getStatus()) {
            createLoginLog(user.getId(), account, loginType, LoginResult.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        if (user.getExpireDate() != null && user.getExpireDate().isBefore(LocalDate.now())) {
            createLoginLog(user.getId(), account, loginType, LoginResult.USER_EXPIRED);
            throw exception(AUTH_LOGIN_USER_EXPIRED);
        }
        if (!BCrypt.checkpw(reqVO.getPassword(), user.getPassword())) {
            createLoginLog(user.getId(), account, loginType, LoginResult.BAD_CREDENTIALS);
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }

        // 创建 Token 令牌，记录登录日志
        LoginUtil.login(user, DeviceType.PC);
        createLoginLog(user.getId(), account, loginType, LoginResult.SUCCESS);
        return new LoginRespVO()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setRealName(user.getNickname())
                .setDesc(user.getRemark())
                .setAccessToken(StpUtil.getTokenValue());
    }

    public LoginRespVO login(EmailLoginVO reqVO) {
        String email = reqVO.getEmail();
        String code = reqVO.getCode();
        LoginUser user;
        LoginType loginType;
        if (StrUtil.isNotEmpty(email) && ReUtil.isMatch(RegexUtil.EMAIL, email)) {
            user = userService.getLoginUser("", "", email);
            loginType = LoginType.LOGIN_EMAIL_CODE;
        } else {
            throw exception(AUTH_LOGIN_BAD_CREDENTIALS);
        }
        if (!user.getStatus()) {
            createLoginLog(user.getId(), user.getUsername(), loginType, LoginResult.USER_DISABLED);
            throw exception(AUTH_LOGIN_USER_DISABLED);
        }
        if (user.getExpireDate() != null && user.getExpireDate().isBefore(LocalDate.now())) {
            createLoginLog(user.getId(), user.getUsername(), loginType, LoginResult.USER_EXPIRED);
            throw exception(AUTH_TOKEN_EXPIRED);
        }
        String emailCode = valueOperations.get(RedisKey.EMAIL_CODE);
        if (!code.equals(emailCode)) {
            createLoginLog(user.getId(), email, loginType, LoginResult.BAD_EMAIL_CODE);
            throw exception(AUTH_LOGIN_BAD_EMAIL_CODE);
        }

        // 创建 Token 令牌，记录登录日志
        LoginUtil.login(user, DeviceType.PC);
        createLoginLog(user.getId(), email, loginType, LoginResult.SUCCESS);
        return new LoginRespVO()
                .setUserId(user.getId())
                .setUsername(user.getUsername())
                .setRealName(user.getNickname())
                .setDesc(user.getRemark())
                .setAccessToken(StpUtil.getTokenValue());
    }

    public Set<String> codes() {
        Login loginUser = LoginUtil.getLoginUser();
        if (loginUser == null) {
            throw exception(UNAUTHORIZED);
        }
        return loginUser.getPermissions();
    }

    public void generateEmailCode(String account) {
    }

    public void logout() {
        Login loginUser = LoginUtil.getLoginUser();
        if (loginUser != null) {
            StpUtil.logout(new SaLogoutParameter().setRange(SaLogoutRange.TOKEN));
            createLoginLog(loginUser.getId(), loginUser.getUsername(), LoginType.LOGOUT_SELF, LoginResult.SUCCESS);
        }
    }

    public void register(RegisterVO reqVO) {
        String username = reqVO.getUsername();
        String password = reqVO.getPassword();
        Integer userType = UserType.valueOf(reqVO.getUserType()).getType();
        UserCreateDTO reqDTO = new UserCreateDTO();
        reqDTO.setUsername(username);
        reqDTO.setNickname(username);
        reqDTO.setPassword(BCrypt.hashpw(password));
        reqDTO.setType(userType);
        userService.registerUser(reqDTO);
    }

    private void createLoginLog(Long userId, String account, LoginType loginType, LoginResult loginResult) {
        // 插入登录日志
        LoginLogCreateDTO reqDTO = new LoginLogCreateDTO();
        reqDTO.setLoginType(loginType.getType());
        reqDTO.setUserId(userId);
        reqDTO.setUserType(UserType.PC.getType());
        reqDTO.setAccount(account);
        reqDTO.setUserAgent(ServletUtils.getUserAgent());
        reqDTO.setUserIp(ServletUtils.getClientIP());
        reqDTO.setResult(loginResult.getResult());
        loginLogService.createLoginLog(reqDTO);
        // 更新最后登录时间
        if (userId != null && Objects.equals(LoginResult.SUCCESS.getResult(), loginResult.getResult())) {
            userService.updateUserLogin(userId, ServletUtils.getClientIP());
        }
    }
}
