package cn.wnhyang.coolguard.mapper;

import cn.hutool.core.util.StrUtil;
import cn.wnhyang.coolguard.AdminApplication;
import cn.wnhyang.coolguard.decision.entity.Rule;
import cn.wnhyang.coolguard.decision.mapper.RuleMapper;
import com.ql.util.express.DefaultContext;
import com.ql.util.express.ExpressRunner;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

/**
 * @author wnhyang
 * @date 2024/7/21
 **/
@SpringBootTest(classes = AdminApplication.class)
@Slf4j
public class RuleMapperTest {

    @Resource
    private RuleMapper ruleMapper;

    @Test
    public void test() {
        List<Rule> list = ruleMapper.selectRunningListByPolicyCode("appLoginWorst");
        log.info("{}", list);
    }

    @Test
    public void test3() throws Exception {
        ExpressRunner runner = new ExpressRunner();
        DefaultContext<String, Object> context = new DefaultContext<>();
        String fun = "base + al * value";
        String express = StrUtil.format("min(upperLimit, max({}, lowerLimit))", fun);
        log.info(express);
        String[] outVarNames = runner.getOutVarNames(express);
        log.info(Arrays.toString(outVarNames));
        context.put("base", 45.434);
        context.put("al", 3.352);
        context.put("value", 24.3264);
        context.put("lowerLimit", -35.342);
        context.put("upperLimit", 3463.57);
        Object r = runner.execute(express, context, null, true, false);
        log.info("{}", r);
    }

    @Test
    public void test4() {
    }
}
