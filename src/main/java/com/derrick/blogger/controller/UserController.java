package com.derrick.blogger.controller;

import com.derrick.blogger.dto.UserRequestDTO;
import com.derrick.blogger.dto.UserResponseDTO;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.UsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
@Tag(name = "User", description = "Endpoints for all things user")
public class UserController {
    private final UsersService usersService;

    @Operation(summary = "Fetch user details")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "401", description = "IO Exception"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> fetchUserDetails(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(usersService.readUser(Integer.valueOf(userId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    UserResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "update user profile")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "200", description = "Ok"),
                @ApiResponse(responseCode = "401", description = "IO Exception"),
                @ApiResponse(responseCode = "403", description = "Unauthorized"),
                @ApiResponse(responseCode = "404", description = "Not Found"),
            })
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable String userId, @ModelAttribute UserRequestDTO userRequestDTO) {
        try {
            return new ResponseEntity<>(
                    usersService.updateUser(Integer.valueOf(userId), userRequestDTO), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    UserResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            return new ResponseEntity<>(
                    UserResponseDTO.builder().message(e.getMessage()).build(), HttpStatus.EXPECTATION_FAILED);
        }
    }
}
