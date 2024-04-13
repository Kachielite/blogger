package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.model.ResetToken;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.ResetTokenRepository;
import com.derrick.blogger.service.ResetTokenService;
import com.derrick.blogger.utils.TokenGenerator;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResetTokenServiceImp implements ResetTokenService {
    private final ResetTokenRepository resetTokenRepository;

    @Override
    public ResetToken generateResetToken(User user) {
        String token = TokenGenerator.generateUniqueToken();
        LocalDateTime expiryDate = LocalDateTime.now().plusHours(3);
        ResetToken resetToken = ResetToken.builder()
                .expiryDate(expiryDate)
                .userId(user.getId())
                .token(token)
                .build();
        return resetTokenRepository.save(resetToken);
    }

    @Override
    public boolean validateToken(String token) {
        ResetToken resetToken = resetTokenRepository.findByToken(token);
        if (resetToken == null) {
            return false;
        }
        return !resetToken.getExpiryDate().isBefore(LocalDateTime.now()); // Token expired
    }
}
