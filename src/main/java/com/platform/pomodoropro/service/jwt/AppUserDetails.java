package com.platform.pomodoropro.service.jwt;

import com.platform.pomodoropro.entity.model.ROLE;
import com.platform.pomodoropro.entity.model.STATUS;
import com.platform.pomodoropro.entity.model.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;

public class AppUserDetails implements UserDetails {
    @Getter
    private final UserEntity user;
    private final Set<? super GrantedAuthority> grantedAuthorities = new HashSet<>();

    public AppUserDetails(UserEntity user) {
        this.user = user;
        Set<ROLE> userRoleSet = Collections.singleton(user.getRole());
        grantedAuthorities.addAll(userRoleSet.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.name())).toList());
    }

    @Override
    public List getAuthorities() {
        return Collections.unmodifiableList(new ArrayList<>(grantedAuthorities));
    }

    @Override
    public String getPassword() {
        return getUser().getPassword();
    }

    @Override
    public String getUsername() {
       return getUser().getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return getUser().getStatus() == STATUS.ACTIVATED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return getUser().getStatus() != STATUS.SUSPENDED;
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
        return getUser().getStatus().equals(STATUS.ACTIVATED);
    }

}

