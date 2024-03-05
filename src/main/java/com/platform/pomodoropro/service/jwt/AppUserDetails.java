package com.platform.pomodoropro.service.jwt;

import com.platform.pomodoropro.entity.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


public class AppUserDetails implements UserDetails {
    private final UserEntity user;
    private final Set<? super GrantedAuthority> grantedAuthorities = new HashSet<>();

    public AppUserDetails(UserEntity user) {
        this.user = user;
        Set<UserEntity.ROLE> userRoleSet = Collections.singleton(user.getRole);
        grantedAuthorities.addAll(userRoleSet.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.name())).toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableList(new ArrayList(grantedAuthorities));
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
        return null;
    }


    @Override
    public boolean isAccountNonExpired() {
        return getUser().getStatus() == UserEntity.STATUS.ACTIVATED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getUser().getStatus() != UserEntity.STATUS.SUSPENDED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isValidStatus();
    }

    @Override
    public boolean isEnabled() {
        return isValidStatus();
    }

    private boolean isValidStatus() {
        return getUser().getStatus().equals(UserEntity.STATUS.ACTIVATED);
    }

    public UserEntity getUser() {
        return user;
    }
}
}
