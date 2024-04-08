package com.derrick.blogger.controller;

import com.derrick.blogger.dto.AdminRequestDTO;
import com.derrick.blogger.dto.AdminResponseDTO;
import com.derrick.blogger.dto.AdminUpdateDTO;
import com.derrick.blogger.exceptions.ConflictException;
import com.derrick.blogger.exceptions.InsufficientPermissionsException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
            })
    @PostMapping("/create-user")
    public ResponseEntity<AdminResponseDTO> createUser(
            @Valid @RequestBody AdminRequestDTO adminRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<AdminResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(adminService.createUser(adminRequestDTO), HttpStatus.CREATED);
        } catch (InsufficientPermissionsException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.UNAUTHORIZED);
        } catch (ConflictException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.CONFLICT);
        } catch (InternalServerErrorException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Operation(summary = "Fetch user by id")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @GetMapping("/users/{userId}")
    public ResponseEntity<AdminResponseDTO> fetchUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(adminService.readUser(Integer.valueOf(userId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch all users")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @GetMapping("/users")
    public ResponseEntity<AdminResponseDTO> fetchUser(
            @RequestParam(defaultValue = "0") int offset, @RequestParam(defaultValue = "10") int page) {
        PageRequest pageable = PageRequest.of(offset, page);
        return new ResponseEntity<>(adminService.readUsers(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Update user role")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @PutMapping("/users")
    public ResponseEntity<AdminResponseDTO> updateUserRole(
            @Valid @RequestBody AdminUpdateDTO adminUpdateDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<AdminResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(adminService.updateUserRole(adminUpdateDTO), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Delete user")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @DeleteMapping("/users/{userId}")
    public ResponseEntity<AdminResponseDTO> deleteUser(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(adminService.deleteUser(Integer.valueOf(userId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    AdminResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    private ResponseEntity<AdminResponseDTO> validateInput(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            bindingResult.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), error.getDefaultMessage());
            });
            // Return bad request with validation errors
            return new ResponseEntity<>(
                    AdminResponseDTO.builder()
                            .message("Validation error")
                            .errors(errors)
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
        return null;
    }
}
