package com.devicemanagement.module.system.mapper;

import com.devicemanagement.module.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 查询用户自身与其角色、权限编码。 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /** 查询用户被分配的全部角色编码。 */
    @Select("SELECT DISTINCT r.role_code FROM sys_user_role ur "
            + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
    List<String> selectRoleCodesByUserId(Long userId);

    /** 查询用户通过角色获得的全部权限编码。 */
    @Select("SELECT DISTINCT p.permission_code FROM sys_user_role ur "
            + "INNER JOIN sys_role_permission rp ON ur.role_id = rp.role_id "
            + "INNER JOIN sys_permission p ON rp.permission_id = p.id WHERE ur.user_id = #{userId}")
    List<String> selectPermissionCodesByUserId(Long userId);

    /** 权限变化后，找出通过角色拥有该权限的用户。 */
    @Select("SELECT DISTINCT ur.user_id FROM sys_user_role ur "
            + "INNER JOIN sys_role_permission rp ON ur.role_id = rp.role_id "
            + "WHERE rp.permission_id = #{permissionId}")
    List<Long> selectUserIdsByPermissionId(Long permissionId);

    /** 角色变更后，找出需要刷新权限缓存的用户。 */
    @Select("SELECT user_id FROM sys_user_role WHERE role_id = #{roleId}")
    List<Long> selectUserIdsByRoleId(Long roleId);
}
