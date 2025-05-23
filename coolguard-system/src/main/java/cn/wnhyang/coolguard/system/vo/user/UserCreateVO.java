package cn.wnhyang.coolguard.system.vo.user;

import com.alibaba.excel.annotation.ExcelProperty;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Set;

/**
 * @author wnhyang
 * @date 2023/8/3
 **/
@Data
public class UserCreateVO {

    /**
     * 用户账号
     */
    @NotBlank(message = "登录账号不能为空")
    @Size(min = 4, max = 16, message = "账号长度为 4-16 位")
    @Pattern(regexp = "^[A-Za-z0-9]+$", message = "账号格式为数字以及字母")
    @ExcelProperty("账号")
    private String username;

    /**
     * 用户昵称
     */
    @Size(max = 30, message = "用户昵称长度不能超过30个字符")
    @ExcelProperty("昵称")
    private String nickname;

    /**
     * 用户类型
     */
    @ExcelProperty("用户类型")
    private Integer type;

    /**
     * 描述
     */
    @ExcelProperty("描述")
    private String remark;

    /**
     * 用户邮箱
     */
    @Email(message = "邮箱格式不正确")
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 手机号码
     */
    @Size(max = 11, message = "手机号码长度不能超过11个字符")
    @ExcelProperty("手机号码")
    private String mobile;

    /**
     * 用户性别
     */
    @ExcelProperty("性别")
    private Integer sex;

    /**
     * 头像地址
     */
    private String avatar;

    /**
     * 状态
     */
    private Boolean status;

    /**
     * 账号过期时间
     */
    @Future
    private LocalDate expireDate;

    /**
     * 角色集合
     */
    private Set<Long> roleIds;

}
