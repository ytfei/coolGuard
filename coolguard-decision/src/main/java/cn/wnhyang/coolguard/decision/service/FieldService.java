package cn.wnhyang.coolguard.decision.service;

import cn.wnhyang.coolguard.common.pojo.PageResult;
import cn.wnhyang.coolguard.decision.context.FieldContext;
import cn.wnhyang.coolguard.decision.entity.Action;
import cn.wnhyang.coolguard.decision.entity.Field;
import cn.wnhyang.coolguard.decision.entity.ParamConfig;
import cn.wnhyang.coolguard.decision.vo.create.FieldCreateVO;
import cn.wnhyang.coolguard.decision.vo.create.TestDynamicFieldScript;
import cn.wnhyang.coolguard.decision.vo.page.FieldPageVO;
import cn.wnhyang.coolguard.decision.vo.update.FieldUpdateVO;

import java.util.List;
import java.util.Map;

/**
 * 字段表 服务类
 *
 * @author wnhyang
 * @since 2024/03/14
 */
public interface FieldService {

    /**
     * 新建
     *
     * @param createVO 新建VO
     * @return id
     */
    Long createField(FieldCreateVO createVO);

    /**
     * 更新
     *
     * @param updateVO 更新VO
     */
    void updateField(FieldUpdateVO updateVO);

    /**
     * 删除
     *
     * @param id id
     */
    void deleteField(Long id);

    /**
     * 查询单个
     *
     * @param id id
     * @return po
     */
    Field getField(Long id);

    /**
     * 分页查询
     *
     * @param pageVO 分页VO
     * @return pageResult
     */
    PageResult<Field> pageField(FieldPageVO pageVO);

    /**
     * 测试动态字段脚本
     *
     * @param testDynamicFieldScript 测试动态字段脚本
     * @return 测试结果
     */
    String testDynamicFieldScript(TestDynamicFieldScript testDynamicFieldScript);

    /**
     * 获取所有字段
     *
     * @return 字段列表
     */
    List<Field> listField();

    /**
     * 获取所有字段
     *
     * @return 字段列表
     */
    Map<String, Field> getFields();

    /**
     * 解析普通字段
     *
     * @param inputFieldList 输入字段列表
     * @param params         参数
     */
    FieldContext fieldParse(List<ParamConfig> inputFieldList, Map<String, String> params);

    /**
     * 设置字段
     *
     * @param setFields 设置字段
     */
    void setField(List<Action.SetField> setFields);


}
