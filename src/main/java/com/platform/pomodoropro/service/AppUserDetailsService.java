package com.platform.pomodoropro.service;

import com.platform.pomodoropro.entity.model.UserEntity;
import com.platform.pomodoropro.repository.UserRepository;
import com.platform.pomodoropro.security.jwt.AppUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AppUserDetailsService implements UserDetailsService {
    final private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserByPhone(phone);
        if(userEntity != null){
            return new AppUserDetails(userEntity);
        }
        throw new UsernameNotFoundException("user " + phone + "not found in database");
    }
}