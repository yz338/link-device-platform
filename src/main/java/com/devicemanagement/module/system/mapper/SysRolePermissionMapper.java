package com.devicemanagement.module.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 角色权限关联表使用联合主键，只提供本项目实际需要的关联增删操作。 */
public interface SysRolePermissionMapper {

    /** 查询角色当前已关联的权限 ID。 */
    @Select("SELECT permission_id FROM sys_role_permission WHERE role_id = #{roleId}")
    List<Long> selectPermissionIdsByRoleId(Long roleId);

    /** 删除角色原有的全部权限关联。 */
    @Delete("DELETE FROM sys_role_permission WHERE role_id = #{roleId}")
    int deleteByRoleId(Long roleId);

    /** 删除某项权限与全部角色的关联。 */
    @Delete("DELETE FROM sys_role_permission WHERE permission_id = #{permissionId}")
    int deleteByPermissionId(Long permissionId);

    /** 新增一条角色权限关联。 */
    @Insert("INSERT INTO sys_role_permission (role_id, permission_id) VALUES (#{roleId}, #{permissionId})")
    int insertRelation(Long roleId, Long permissionId);
}
