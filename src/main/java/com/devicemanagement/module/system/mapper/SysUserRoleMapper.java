package com.devicemanagement.module.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/** 用户角色关联表使用联合主键，只提供本项目实际需要的关联增删操作。 */
public interface SysUserRoleMapper {

    /** 删除用户原有的全部角色关联。 */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(@Param("userId") Long userId);

    /** 新增一条用户角色关联。 */
    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertRelation(@Param("userId") Long userId, @Param("roleId") Long roleId);

    /** 查询用户已分配的角色 ID 列表，用于前端复选框回显。 */
    @Select("SELECT role_id FROM sys_user_role WHERE user_id = #{userId}")
    List<Long> selectRoleIdsByUserId(@Param("userId") Long userId);

    /** 查询用户分配的所有角色名称，多个使用顿号拼接时需要自行处理。 */
    @Select("SELECT r.role_name FROM sys_user_role ur "
            + "INNER JOIN sys_role r ON ur.role_id = r.id WHERE ur.user_id = #{userId} ORDER BY r.id")
    List<String> selectRoleNamesByUserId(@Param("userId") Long userId);
}
