package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AuthResponseDTO;
import com.derrick.blogger.dto.ErrorResponseDTO;
import com.derrick.blogger.dto.LoginRequestDTO;
import com.derrick.blogger.dto.RegisterRequestDTO;
import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.InvalidAuthRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AuthenticationService;
import com.derrick.blogger.service.UsersService;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Endpoints for all things authentication")
public class AuthenticationController {

    public final AuthenticationService authenticationService;
    private final UsersService usersService;

    @Operation(summary = "Create new user")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
            })
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequestDTO registerRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(authenticationService.register(registerRequestDTO), HttpStatus.CREATED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder()
                            .statusCode(409)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder()
                            .statusCode(500)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequestDTO loginRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            AuthResponseDTO responseDTO = authenticationService.login(loginRequestDTO);
            return ResponseEntity.ok(responseDTO);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        } catch (InvalidAuthRequestException e) {
            return new ResponseEntity<>(
                    AuthResponseDTO.builder()
                            .statusCode(403)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.UNAUTHORIZED);
        }
    }

    @Operation(summary = "Generate password link")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @GetMapping("/reset-password-link")
    public ResponseEntity<String> generatePasswordLink(@RequestParam String email) {
        try {
            return new ResponseEntity<>(usersService.generateResetLink(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Reset password")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "401", description = "IO Exception"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody UserRequestDTO userRequestDTO) {
        try {
            return new ResponseEntity<>(usersService.resetPassword(userRequestDTO), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    private ResponseEntity<ErrorResponseDTO> validateInput(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            // Return bad request with validation errors
            return ResponseEntity.badRequest()
                    .body(ErrorResponseDTO.builder()
                            .message("Validation error")
                            .errors(errors)
                            .build());
        }
        return null;
    }
}
