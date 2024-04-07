package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AdminService;
import com.derrick.blogger.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImp implements AdminService {
    private final AuthenticationService authenticationService;

    @Override
    public AuthResponseDTO createUser(RegisterRequestDTO registerRequestDTO)
            throws ConflictException, InsufficientPermissionsException, InternalServerErrorException {
        try {
            // Check if the current user has the required authority
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!authentication.getAuthorities().contains(new SimpleGrantedAuthority("ADMIN"))) {
                throw new InsufficientPermissionsException(
                        "User does not have sufficient permissions to perform this operation.");
            }

            return authenticationService.register(registerRequestDTO);
        } catch (ConflictException | InternalServerErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserResponseDTO readUser(Integer userId) throws NotFoundException {
        return null;
    }

    @Override
    public UserResponseDTO readUsers() throws NotFoundException {
        return null;
    }

    @Override
    public UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO)
            throws NotFoundException, ConflictException, BadRequestException {
        return null;
    }

    @Override
    public UserResponseDTO deleteUser(Integer userId) throws NotFoundException {
        return null;
    }
}
