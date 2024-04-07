package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.AuthenticationService;
import com.derrick.blogger.utils.JWTUtils;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImp implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTUtils jwtUtils;

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO)
            throws InvalidAuthRequestException, NotFoundException {
        try {
            Optional<User> user = userRepository.findByEmail(loginRequestDTO.email());

            if (user.isEmpty()) {
                log.error("User does not exist");
                throw new NotFoundException("The user associated with the provided username could not be found.");
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.email(), loginRequestDTO.password()));
            log.info("Authenticating user: {}", auth);

            String token = jwtUtils.generateToken(user.get());

            log.info("User successfully authenticated");

            return AuthResponseDTO.builder()
                    .message("User authenticated successfully")
                    .token(token)
                    .build();

        } catch (NotFoundException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e; // Re-throw NotFoundException
        } catch (AuthenticationException e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new InvalidAuthRequestException(
                    "Invalid authentication request. Please check your credentials and try again.");
        }
    }

    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO)
            throws ConflictException, InternalServerErrorException {

        try {
            Optional<User> user = userRepository.findByEmail(registerRequestDTO.email());

            if (user.isPresent()) {
                log.error("The username already exist");
                throw new ConflictException(
                        "The username you've chosen is already in use. Please select a different one");
            }

            String encodedPassword = passwordEncoder.encode(registerRequestDTO.password());

            User newUser = User.builder()
                    .email(registerRequestDTO.email())
                    .password(encodedPassword)
                    .role("USER")
                    .build();

            userRepository.save(newUser);
            return AuthResponseDTO.builder()
                    .message("Registration successful!")
                    .token(null)
                    .build();
        } catch (ConflictException e) {
            log.error("Error user already exist: {}", e.getMessage(), e);
            throw e; // Re-throw NotFoundException
        } catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new InternalServerErrorException("User could not be registered");
        }
    }
}
