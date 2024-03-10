package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.model.Role;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.RoleRepository;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.AuthenticationService;
import com.derrick.blogger.service.TokenService;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
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
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @Override
    public AuthResponseDTO login(LoginRequestDTO loginRequestDTO) throws InvalidAuthRequestException, ConflictException {
        try {
            Optional<User> user = userRepository.findUserByUsername(loginRequestDTO.username());

            if (user.isEmpty()) {
                log.error("User does not exist");
                throw new ConflictException("User not found"); // Properly instantiate NotFoundException
            }

            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password()));
            log.info("Authenticating user: {}", auth);

            String token = tokenService.generateJwt(auth);

            log.info("User successfully authenticated");
            return AuthResponseDTO.builder()
                    .message("User authenticated successfully")
                    .token(token)
                    .build();

        } catch (ConflictException e) {
            log.error("Error user not found: {}", e.getMessage(), e);
            throw e; // Re-throw NotFoundException
        } catch (AuthenticationException e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new InvalidAuthRequestException("Invalid authentication request. Please check your credentials and try again.");
        }
    }


    @Override
    public AuthResponseDTO register(RegisterRequestDTO registerRequestDTO) throws ConflictException, InternalServerErrorException {

        try {
            Optional<User> user = userRepository.findUserByUsername(registerRequestDTO.username());

            if (user.isPresent()) {
                log.error("User not found");
                throw new ConflictException("User not found");
            }

            String encodedPassword = passwordEncoder.encode(registerRequestDTO.password());
            Role userRole = roleRepository.findByAuthority("USER").get();
            Set<Role> authorities = new HashSet<>();
            authorities.add(userRole);

            User newUser = User.builder()
                    .username(registerRequestDTO.username())
                    .password(encodedPassword)
                    .authorities(authorities)
                    .build();

            userRepository.save(newUser);
            return AuthResponseDTO.builder()
                    .message("User registered successfully")
                    .token(null)
                    .build();
        }catch (ConflictException e) {
            log.error("Error user already exist: {}", e.getMessage(), e);
            throw e; // Re-throw NotFoundException
        }catch (Exception e) {
            log.error("Error authenticating user: {}", e.getMessage(), e);
            throw new InternalServerErrorException("User could not be registered");
        }
    }
}
