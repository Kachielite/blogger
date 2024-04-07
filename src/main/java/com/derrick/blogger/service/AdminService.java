package com.derrick.blogger.service;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;

public interface AdminService {
    // Create user
    AuthResponseDTO createUser(RegisterRequestDTO registerRequestDTO)
            throws ConflictException, InternalServerErrorException, InsufficientPermissionsException;

    // Read User
    UserResponseDTO readUser(Integer userId) throws NotFoundException;

    // Read Users
    UserResponseDTO readUsers() throws NotFoundException;

    // Update User
    UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO)
            throws NotFoundException, ConflictException, BadRequestException;

    // Delete User
    UserResponseDTO deleteUser(Integer userId) throws NotFoundException;
}
