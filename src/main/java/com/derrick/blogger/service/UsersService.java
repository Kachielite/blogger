package com.derrick.blogger.service;

import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public interface UsersService {
    UserResponseDTO updateUser(Integer userId, UserRequestDTO userRequestDTO) throws NotFoundException, IOException;

    UserResponseDTO readUser(Integer userId) throws NotFoundException;

    String generateResetLink(String email) throws NotFoundException;

    UserResponseDTO resetPassword(UserRequestDTO userRequestDTO) throws BadRequestException, NotFoundException;
}
