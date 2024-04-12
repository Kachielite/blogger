package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserResponseDTO updateUser(Integer userId) {
        return null;
    }

    @Override
    public UserResponseDTO readUser(Integer userId) {
        return null;
    }

    @Override
    public UserResponseDTO resetPassword(Integer userId) {
        return null;
    }

    //    @Override
    //    public AdminResponseDTO resetPassword(String token) throws BadRequestException, NotFoundException {
    //        try {
    //            boolean isTokenValid = resetTokenService.validateToken(token);
    //            if(!isTokenValid){
    //                log.error("Invalid or expired token");
    //                throw new BadRequestException("Reset password link is either expired or invalid");
    //            }
    //
    //            ResetToken resetToken = resetTokenRepository.findByToken(token);
    //            Optional<User> user = userRepository.findById(resetToken.getId());
    //
    //            if (user.isEmpty()) {
    //                log.error("UserId can not be found");
    //                throw new NotFoundException("UserId associated with reset token can not be found");
    //            }
    //
    //            user.get().setPassword();
    //
    //
    //        } catch (BadRequestException | NotFoundException e) {
    //            log.error("Error resetting password: {}", e.getMessage(), e);
    //            throw e;
    //        }
    //
    //    }
}
