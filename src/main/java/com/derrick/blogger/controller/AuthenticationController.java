package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for all things authentication")
public class AuthenticationController {

    public final AuthenticationService authenticationService;

    @Operation(summary = "Create new user")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
            })
    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<AuthResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Authenticate user")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(schema = @Schema(implementation = AuthResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation = AuthResponseDTO.class,
                                                        example = "{\"message\": \"Unauthorized\"}"))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not found",
                        content =
                                @Content(
                                        schema =
                                                @Schema(
                                                        implementation = AuthResponseDTO.class,
                                                        example = "{\"message\": \"Not found\"}"))),
            })
    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(
            @Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<AuthResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            AuthResponseDTO responseDTO = authenticationService.login(loginRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        } catch (InvalidAuthRequestException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.UNAUTHORIZED);
        }
    }

    private ResponseEntity<AuthResponseDTO> validateInput(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            // Return bad request with validation errors
            return ResponseEntity.badRequest()
                    .body(AuthResponseDTO.builder()
                            .message("Validation error")
                            .errors(errors)
                            .build());
        }
        return null;
    }
}
