package cn.wnhyang.coolguard.decision.vo.page;

import cn.wnhyang.coolguard.common.pojo.PageParam;
import lombok.Data;

import java.io.Serial;

/**
 * @author wnhyang
 * @date 2024/3/14
 **/
@Data
public class IndicatorPageVO extends PageParam {

    @Serial
    private static final long serialVersionUID = -4138518261130779757L;

    /**
     * 指标名
     */
    private String name;

    /**
     * 类型
     */
    private String type;

    /**
     * 场景
     */
    private String scene;

    /**
     * 场景类型
     */
    private String sceneType;

    /**
     * 是否发布
     */
    private Boolean publish;

    /**
     * 最新
     */
    private Boolean latest;

    /**
     * 有版本号
     */
    private Boolean hasVersion;

}
