package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    public final AuthenticationService authenticationService;

    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO)
            throws ConflictException, InternalServerErrorException {
        try {
            return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
        } catch (ConflictException e) {
            throw new ConflictException("User already exist");
        } catch (InternalServerErrorException e) {
            throw new InternalServerErrorException("User creation failed");
        }
    }

    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO)
            throws NotFoundException, InvalidAuthRequestException {
        try {
            return new ResponseEntity<>(authenticationService.login(loginRequestDTO), HttpStatus.OK);
        } catch (NotFoundException e) {
            throw new NotFoundException("User does not exist");
        } catch (InvalidAuthRequestException e) {
            throw new InvalidAuthRequestException("User registration failed");
        }
    }
}
