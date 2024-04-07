package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Validated // Ensure validation is enabled for this controller
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Endpoints for all things administration")
public class AdminController {

    public final AuthenticationService authenticationService;

    @Operation(summary = "Create new user")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
            })
    @PostMapping("/create-user")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody RegisterRequestDTO registerRequestDTO) {
        try {
            // TODO: Validate input to ensure that there a no null values
            return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
