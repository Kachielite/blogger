package com.derrick.blogger.service;

import com.derrick.blogger.dto.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public interface UserService extends UserDetailsService {
    UserDetails loadUserByUsername(String email);

    UserResponseDTO updateUser(Integer userId);

    UserResponseDTO readUser(Integer userId);

    UserResponseDTO resetPassword(Integer userId);
}
