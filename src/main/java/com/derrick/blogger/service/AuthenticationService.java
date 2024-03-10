package com.derrick.blogger.service;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface AuthenticationService {
    AuthResponseDTO login(LoginRequestDTO loginRequestDTO) throws InvalidAuthRequestException, NotFoundException;

    AuthResponseDTO register(RegisterRequestDTO registerRequestDTO)
            throws ConflictException, InternalServerErrorException;
}
