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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    public final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(AuthResponseDTO.messageOnly(e.getMessage()), HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(AuthResponseDTO.messageOnly(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        try {
            AuthResponseDTO responseDTO = authenticationService.login(loginRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(AuthResponseDTO.messageOnly(e.getMessage()), HttpStatus.NOT_FOUND);
        } catch (InvalidAuthRequestException e) {
            return new ResponseEntity<>(AuthResponseDTO.messageOnly(e.getMessage()), HttpStatus.UNAUTHORIZED);
        }
    }
}
