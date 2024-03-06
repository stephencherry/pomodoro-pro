package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.UserEntity;
import com.platform.pomodoropro.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    final private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username);
        if(userEntity == null){
            throw new UsernameNotFoundException("Username " + username + " not found with username");
        }
        return org.springframework.security.core.userdetails.User.builder()
                .username(userEntity.getUsername())
                .password(userEntity.getPassword())
                .build();
    }
}
