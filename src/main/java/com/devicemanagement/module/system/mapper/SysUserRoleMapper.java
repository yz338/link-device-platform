package com.devicemanagement.module.system.mapper;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;

/** 用户角色关联表使用联合主键，只提供本项目实际需要的关联增删操作。 */
public interface SysUserRoleMapper {

    /** 删除用户原有的全部角色关联。 */
    @Delete("DELETE FROM sys_user_role WHERE user_id = #{userId}")
    int deleteByUserId(Long userId);

    /** 新增一条用户角色关联。 */
    @Insert("INSERT INTO sys_user_role (user_id, role_id) VALUES (#{userId}, #{roleId})")
    int insertRelation(Long userId, Long roleId);
}
