package com.devicemanagement.module.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/** 前端固定菜单和按钮权限所需的当前用户信息。 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "当前登录用户信息")
public class UserProfileResponse {
    @Schema(description = "用户名", example = "admin")
    private String username;
    @Schema(description = "用户昵称", example = "管理员")
    private String nickname;
    @Schema(description = "角色编码列表，不含 ROLE_ 前缀", example = "[\"ADMIN\"]")
    private List<String> roles;
    @Schema(description = "权限码列表", example = "[\"system:user:list\"]")
    private List<String> permissions;
}
