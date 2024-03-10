package com.derrick.blogger.service;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public interface UserService extends UserDetailsService { }
