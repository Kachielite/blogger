package com.derrick.blogger.service;

import com.derrick.blogger.dto.AdminRequestDTO;
import com.derrick.blogger.dto.AdminResponseDTO;
import com.derrick.blogger.dto.AdminUpdateDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import org.springframework.data.domain.PageRequest;

public interface AdminService {
    // Create user
    AdminResponseDTO createUser(AdminRequestDTO adminRequestDTO)
            throws ConflictException, InternalServerErrorException, InsufficientPermissionsException;

    // Read User
    AdminResponseDTO readUser(Integer userId) throws NotFoundException;

    // Read Users
    AdminResponseDTO readUsers(PageRequest pageable);

    // Update User
    AdminResponseDTO updateUserRole(AdminUpdateDTO adminUpdateDTO) throws NotFoundException;

    // Delete User
    AdminResponseDTO deleteUser(Integer userId) throws NotFoundException;
}
