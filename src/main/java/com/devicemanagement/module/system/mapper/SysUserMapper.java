package com.devicemanagement.module.system.mapper;

import com.devicemanagement.module.system.entity.SysUser;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 查询用户自身与其角色、权限编码。 */
@Mapper
public interface SysUserMapper extends BaseMapper<SysUser> {

    /** 查询用户被分配的全部角色编码。 */
    @Select("SELECT DISTINCT r.role_code FROM sys_user_role ur "
            + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE ur.user_id = #{userId}")
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);

    /** 查询用户通过角色获得的全部权限编码。 */
    @Select("SELECT DISTINCT p.permission_code FROM sys_user_role ur "
            + "INNER JOIN sys_role_permission rp ON ur.role_id = rp.role_id "
            + "INNER JOIN sys_permission p ON rp.permission_id = p.id WHERE ur.user_id = #{userId}")
    List<String> selectPermissionCodesByUserId(@Param("userId") Long userId);

    /** 权限变化后，找出通过角色拥有该权限的用户。 */
    @Select("SELECT DISTINCT ur.user_id FROM sys_user_role ur "
            + "INNER JOIN sys_role_permission rp ON ur.role_id = rp.role_id "
            + "WHERE rp.permission_id = #{permissionId}")
    List<Long> selectUserIdsByPermissionId(@Param("permissionId") Long permissionId);

    /** 角色变更后，找出需要刷新权限缓存的用户。 */
    @Select("SELECT user_id FROM sys_user_role WHERE role_id = #{roleId}")
    List<Long> selectUserIdsByRoleId(@Param("roleId") Long roleId);

    /** 查询所有管理员用户ID（角色编码为 ADMIN 的启用用户）。 */
    @Select("SELECT u.id FROM sys_user u "
            + "INNER JOIN sys_user_role ur ON u.id = ur.user_id "
            + "INNER JOIN sys_role r ON ur.role_id = r.id "
            + "WHERE r.role_code = 'ADMIN' AND u.enabled = 1")
    List<Long> selectAdminUserIds();

    /** 判断用户是否持有指定角色（用于创建工单时校验处理人必须是运维）。 */
    @Select("SELECT COUNT(1) FROM sys_user_role ur "
            + "INNER JOIN sys_role r ON ur.role_id = r.id "
            + "WHERE ur.user_id = #{userId} AND r.role_code = #{roleCode}")
    int countUserWithRole(@Param("userId") Long userId, @Param("roleCode") String roleCode);
}
