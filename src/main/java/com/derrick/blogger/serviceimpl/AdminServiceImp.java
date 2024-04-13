package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.AdminRequestDTO;
import com.derrick.blogger.dto.AdminResponseDTO;
import com.derrick.blogger.dto.AdminUpdateDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.model.ResetToken;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.AdminService;
import com.derrick.blogger.service.EmailService;
import com.derrick.blogger.service.ResetTokenService;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImp implements AdminService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final ResetTokenService resetTokenService;

    @Override
    public AdminResponseDTO createUser(AdminRequestDTO adminRequestDTO)
            throws ConflictException, InternalServerErrorException, InsufficientPermissionsException {

        List<User> users = new ArrayList<>();
        try {
            log.info("Email received: {} , Password received: {}", adminRequestDTO.email(), adminRequestDTO.password());
            // Check if the current user has the required authority
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                log.error("User is not an ADMIN user");
                throw new InsufficientPermissionsException(
                        "User does not have sufficient permissions to perform this operation.");
            }
            Optional<User> user = userRepository.findByEmail(adminRequestDTO.email());

            if (user.isPresent()) {
                log.info("The user already exist");
                throw new ConflictException("The user you've chosen is already in use. Please select a different one");
            }

            String encodedPassword = passwordEncoder.encode(adminRequestDTO.password());

            User newUser = User.builder()
                    .email(adminRequestDTO.email())
                    .password(encodedPassword)
                    .role(adminRequestDTO.role())
                    .build();

            User savedUser = userRepository.save(newUser);
            users.add(savedUser);
            return AdminResponseDTO.builder()
                    .statusCode(201)
                    .message("Registration successful!")
                    .user(users)
                    .build();
        } catch (ConflictException e) {
            log.error("Error user already exist: {}", e.getMessage(), e);
            throw e; // Re-throw NotFoundException
        } catch (InsufficientPermissionsException e) {
            log.error("Error user does not have enough permission: {}", e.getMessage(), e);
            throw e;
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new InternalServerErrorException("User could not be registered");
        }
    }

    @Override
    public AdminResponseDTO readUser(Integer userId) throws NotFoundException {
        List<User> users = new ArrayList<>();
        try {
            Optional<User> user = userRepository.findById(userId);
            if (user.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with " + userId + " could not be found");
            }
            log.info("The user found");
            users.add(user.get());
            return AdminResponseDTO.builder()
                    .statusCode(200)
                    .message("User successfully retrieved")
                    .user(users)
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AdminResponseDTO readUsers(PageRequest pageable) {
        List<User> users;
        users = userRepository.findAll();
        return AdminResponseDTO.builder()
                .statusCode(200)
                .message("Users successfully retrieved")
                .user(users)
                .build();
    }

    @Override
    public String generateResetPasswordLink(String email) throws NotFoundException {
        String subject = "Password Reset";
        String body = "Kindly click this link to reset password ";
        String baseUrl = "http://localhost/";

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
    public AdminResponseDTO updateUserRole(AdminUpdateDTO adminUpdateDTO) throws NotFoundException {
        List<User> users = new ArrayList<>();
        try {
            Optional<User> searchUser = userRepository.findById(adminUpdateDTO.userId());
            if (searchUser.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with " + adminUpdateDTO.userId() + " could not be found");
            }
            log.info("The user found");
            User user = searchUser.get();
            user.setRole(adminUpdateDTO.role());
            userRepository.save(user);
            users.add(user);
            log.info("The user updated");
            return AdminResponseDTO.builder()
                    .statusCode(200)
                    .message("User role successfully updated")
                    .user(users)
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }

    @Override
    public AdminResponseDTO deleteUser(Integer userId) throws NotFoundException {
        try {
            Optional<User> searchUser = userRepository.findById(userId);
            if (searchUser.isEmpty()) {
                log.error("The user with provided id does not exist");
                throw new NotFoundException("The user with " + userId + " could not be found");
            }
            log.info("The user found");
            User user = searchUser.get();
            userRepository.delete(user);
            log.info("The user deleted");
            return AdminResponseDTO.builder()
                    .statusCode(200)
                    .message("User successfully deleted")
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e;
        }
    }
}
