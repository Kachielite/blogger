package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.CloudinaryService;
import com.derrick.blogger.service.UserService;
import java.io.IOException;
import java.util.Optional;
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
    private final CloudinaryService cloudinaryService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        return userRepository.findByEmail(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

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
