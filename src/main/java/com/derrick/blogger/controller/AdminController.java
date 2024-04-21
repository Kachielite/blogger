package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AdminRequestDTO;
import com.derrick.blogger.dto.AdminResponseDTO;
import com.derrick.blogger.dto.AdminUpdateDTO;
import com.derrick.blogger.dto.ErrorResponseDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Tag(name = "Administration", description = "Endpoints for all things administration")
public class AdminController {

    public final AdminService adminService;

    @Operation(summary = "Create new user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                @ApiResponse(
                        responseCode = "400",
                        description = "Bad Request",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "409",
                        description = "Conflict",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @PostMapping(value = "/create-user", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> createUser(
            @Valid @RequestBody AdminRequestDTO adminRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(adminService.createUser(adminRequestDTO), HttpStatus.CREATED);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(401)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.UNAUTHORIZED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(409)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(500)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Fetch user by id")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @GetMapping(value = "/users/{userId}", produces = "application/json")
    public ResponseEntity<?> fetchUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(adminService.readUser(Integer.valueOf(userId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch all users")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @GetMapping(value = "/users", produces = "application/json")
    public ResponseEntity<?> fetchUser(
            @RequestParam(defaultValue = "0") String page, @RequestParam(defaultValue = "10") String size) {
        PageRequest pageable = PageRequest.of(Integer.parseInt(page), Integer.parseInt(size));
        return new ResponseEntity<>(adminService.readUsers(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Update user role")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @PutMapping(value = "/users", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> updateUserRole(
            @Valid @RequestBody AdminUpdateDTO adminUpdateDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(adminService.updateUserRole(adminUpdateDTO), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Reset user password")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = @Content(schema = @Schema(implementation = String.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @PostMapping("/users/reset-password")
    public ResponseEntity<String> resetUserPassword(@RequestParam String email) {
        try {
            return new ResponseEntity<>(adminService.generateResetPasswordLink(email), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete user")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "Ok",
                        content = @Content(schema = @Schema(implementation = AdminResponseDTO.class))),
                @ApiResponse(
                        responseCode = "403",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @DeleteMapping(value = "/users/{userId}", produces = "application/json")
    public ResponseEntity<?> deleteUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(adminService.deleteUser(Integer.valueOf(userId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<ErrorResponseDTO> validateInput(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            // Return bad request with validation errors
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
                            .message("Validation error")
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
