package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.model.ResetToken;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.ResetTokenRepository;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.CloudinaryService;
import com.derrick.blogger.service.EmailService;
import com.derrick.blogger.service.ResetTokenService;
import com.derrick.blogger.service.UsersService;
import com.derrick.blogger.utils.CloudinaryUrlParser;
import java.io.IOException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsersServiceImpl implements UsersService {

    private final UserRepository userRepository;
    private final CloudinaryService cloudinaryService;
    private final ResetTokenService resetTokenService;
    private final EmailService emailService;
    private final ResetTokenRepository resetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final CloudinaryUrlParser cloudinaryUrlParser;

    @Override
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO)
            throws NotFoundException, IOException {
        try {
            Optional<User> searchUser = userRepository.findById(userId);
            if (searchUser.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with id " + userId + " could not be found");
            }

            if (userRequestDTO.profilePhoto() != null
                    && !userRequestDTO.profilePhoto().isEmpty()) {
                try {
                    log.info("Uploading user profile photo");
                    if (searchUser.get().getProfilePhoto() != null
                            && !searchUser.get().getProfilePhoto().isEmpty()) {
                        cloudinaryService.deleteImage(searchUser.get().getProfilePhoto());
                    }
                    String profileImage =
                            cloudinaryService.uploadImage(userRequestDTO.profilePhoto(), "blogger-profiles");
                    log.info("User profile photo uploaded");
                    searchUser.get().setProfilePhoto(profileImage);

                } catch (IOException e) {
                    log.error("Error occurred while uploading profile photo: {}", e.getMessage(), e);
                    throw e;
                }
            }

            if (userRequestDTO.bio() != null && !userRequestDTO.bio().isEmpty()) {
                searchUser.get().setBio(userRequestDTO.bio());
            }

            if (userRequestDTO.x() != null && !userRequestDTO.x().isEmpty()) {
                searchUser.get().setX(userRequestDTO.x());
            }

            if (userRequestDTO.instagram() != null
                    && !userRequestDTO.instagram().isEmpty()) {
                searchUser.get().setInstagram(userRequestDTO.instagram());
            }

            if (userRequestDTO.facebook() != null && !userRequestDTO.facebook().isEmpty()) {
                searchUser.get().setFacebook(userRequestDTO.facebook());
            }

            User savedUser = userRepository.save(searchUser.get());
            log.info("Saved user profile update");

            return UserResponseDTO.builder()
                    .message("User profile successfully updated")
                    .user(savedUser)
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UserResponseDTO readUser(Integer userId) throws NotFoundException {
        try {
            Optional<User> searchUser = userRepository.findById(userId);
            if (searchUser.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with id " + userId + " could not be found");
            }
            log.info("User found");
            User user = searchUser.get();
            return UserResponseDTO.builder()
                    .message("User successfully retrieved")
                    .user(user)
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public String generateResetLink(String email) throws NotFoundException {
        ;
        String subject = "Password Reset";
        String body = "Kindly click this link to reset password ";
        String baseUrl = "http://localhost:8080/api/v1/reset?token=";

        try {
            Optional<User> searchUser = userRepository.findByEmail(email);
            if (searchUser.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with email " + email + " could not be found");
            }
            log.info("The user found");
            ResetToken resetToken = resetTokenService.generateResetToken(searchUser.get());
            String token = resetToken.getToken();
            String emailBody = body + baseUrl + token;
            emailService.sendEmail(searchUser.get().getEmail(), subject, emailBody);
            log.info("Reset link email sent to: {}", searchUser.get().getEmail());
            return "Reset email link sent successfully";
        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public UserResponseDTO resetPassword(UserRequestDTO userRequestDTO) throws BadRequestException, NotFoundException {
        try {
            log.info("Validating token");
            boolean isTokenValid = resetTokenService.validateToken(userRequestDTO.token());
            if (!isTokenValid) {
                log.error("Invalid or expired token");
                throw new BadRequestException("Reset password link is either expired or invalid");
            }

            log.info("Searching user associated with token");
            ResetToken resetToken = resetTokenRepository.findByToken(userRequestDTO.token());
            Optional<User> user = userRepository.findById(resetToken.getUserId());

            if (user.isEmpty()) {
                log.error("UserId can not be found");
                throw new NotFoundException("UserId associated with reset token can not be found");
            }

            log.info("Encoding new password");
            String encryptedPassword = passwordEncoder.encode(userRequestDTO.password());
            log.info("Updating password");
            user.get().setPassword(encryptedPassword);

            log.info("Deleting used token");
            resetTokenRepository.delete(resetToken);

            return UserResponseDTO.builder()
                    .message("Password reset successful")
                    .build();

        } catch (BadRequestException | NotFoundException e) {
            log.error("Error resetting password: {}", e.getMessage(), e);
            throw e;
        }
    }
}
