package com.derrick.blogger.service;

import com.derrick.blogger.model.ResetToken;
import com.derrick.blogger.model.User;
import org.springframework.stereotype.Service;

@Service
public interface ResetTokenService {
    ResetToken generateResetToken(User user);

    boolean validateToken(String token);
}
