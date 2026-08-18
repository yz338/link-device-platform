package com.devicemanagement.framework.security;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.devicemanagement.framework.redis.UserAuthorityCacheService;
import com.devicemanagement.module.system.entity.SysUser;
import com.devicemanagement.module.system.mapper.SysUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/** 从 RBAC 五张表中加载账号、角色和权限，供密码登录和 JWT 请求复用。 */
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final SysUserMapper sysUserMapper;
    private final UserAuthorityCacheService userAuthorityCacheService;

    /** 根据用户名构造 Spring Security 需要的账号、密码和权限集合。 */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser user = sysUserMapper.selectOne(Wrappers.<SysUser>lambdaQuery()
                .eq(SysUser::getUsername, username));
        if (user == null) {
            throw new UsernameNotFoundException("用户不存在");
        }
        return User.withUsername(user.getUsername())
                .password(user.getPassword())
                // disabled 会让 Spring Security 拒绝账号密码登录。
                .disabled(Boolean.FALSE.equals(user.getEnabled()))
                .authorities(userAuthorityCacheService.getAuthorityCodes(user.getId()).stream()
                        .map(SimpleGrantedAuthority::new).toList())
                .build();
    }
}
