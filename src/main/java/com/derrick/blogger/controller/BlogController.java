package com.derrick.blogger.controller;

import com.derrick.blogger.dto.BlogRequestDTO;
import com.derrick.blogger.dto.BlogUpdateDTO;
import com.derrick.blogger.dto.ErrorResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Endpoints for all things blog")
public class BlogController {
    private final BlogService blogService;

    @Operation(summary = "Create new blog")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "400", description = "Bad Request"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
            })
    @PostMapping
    public ResponseEntity<?> createNewBlog(
            @Valid @ModelAttribute BlogRequestDTO blogRequestDTO, BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(blogService.createBlog(blogRequestDTO), HttpStatus.CREATED);
        } catch (BadRequestException | IOException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Update blog")
    @ApiResponses(
            value = {
                @ApiResponse(responseCode = "201", description = "Created"),
                @ApiResponse(responseCode = "400", description = "Bad Request"),
                @ApiResponse(responseCode = "401", description = "Unauthorized"),
                @ApiResponse(responseCode = "409", description = "Conflict"),
                @ApiResponse(responseCode = "500", description = "Internal Server Error"),
            })
    @PutMapping("/{blogId}")
    public ResponseEntity<?> updateBlog(
            @PathVariable String blogId,
            @Valid @ModelAttribute BlogUpdateDTO blogUpdateDTO,
            BindingResult bindingResult) {
        try {
            ResponseEntity<ErrorResponseDTO> errors = validateInput(bindingResult);
            if (errors != null) return errors;
            return new ResponseEntity<>(
                    blogService.updateBlog(Integer.valueOf(blogId), blogUpdateDTO), HttpStatus.CREATED);
        } catch (BadRequestException | IOException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.BAD_REQUEST);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(500)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(404)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.INTERNAL_SERVER_ERROR);
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
