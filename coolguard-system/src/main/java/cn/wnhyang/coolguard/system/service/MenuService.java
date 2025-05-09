package cn.wnhyang.coolguard.system.service;


import cn.wnhyang.coolguard.system.entity.MenuDO;
import cn.wnhyang.coolguard.system.vo.menu.*;

import java.util.List;
import java.util.Set;

/**
 * 菜单权限表
 *
 * @author wnhyang
 * @since 2023/05/14
 */
public interface MenuService {

    /**
     * 创建菜单
     *
     * @param reqVO 菜单信息
     * @return 菜单id
     */
    Long createMenu(MenuCreateVO reqVO);

    /**
     * 更新菜单
     *
     * @param reqVO 菜单信息
     */
    void updateMenu(MenuUpdateVO reqVO);

    /**
     * 删除菜单
     *
     * @param id 菜单id
     */
    void deleteMenu(Long id);

    /**
     * 查询菜单列表
     *
     * @param reqVO 菜单
     * @return 菜单列表
     */
    List<MenuDO> getMenuList(MenuListVO reqVO);

    /**
     * 查询菜单
     *
     * @param id 菜单id
     * @return 菜单
     */
    MenuDO getMenu(Long id);

    /**
     * 根据菜单ids查询菜单
     *
     * @param ids 菜单ids
     * @return 菜单
     */
    List<MenuDO> getMenuList(Set<Long> ids);

    /**
     * 获取所有菜单列表
     *
     * @return 菜单列表
     */
    List<MenuDO> getMenuList();

    /**
     * 获取树形菜单列表
     *
     * @param reqVO 树形菜单请求
     * @return 树形菜单列表
     */
    List<MenuTreeRespVO> getMenuTreeList(MenuListVO reqVO);

    /**
     * 获取登录用户的菜单列表
     *
     * @param removeButton 是否移除按钮
     * @return 菜单列表
     */
    List<MenuTreeRespVO> getLoginUserMenuWithRouteMetaTreeList(boolean removeButton);

    /**
     * 获取简单树形菜单不带根节点
     *
     * @return 树形菜单
     */
    List<MenuSimpleTreeVO> getMenuSimpleTreeList();

    /**
     * 获取简单树形菜单带根节点
     *
     * @return 树形菜单
     */
    List<MenuSimpleTreeVO> getMenuSimpleTreeListA();


}
