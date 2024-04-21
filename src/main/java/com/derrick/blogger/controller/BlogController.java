package com.derrick.blogger.controller;

import com.derrick.blogger.dto.BlogRequestDTO;
import com.derrick.blogger.dto.BlogResponseDTO;
import com.derrick.blogger.dto.BlogUpdateDTO;
import com.derrick.blogger.dto.ErrorResponseDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.NotFoundException;
import com.derrick.blogger.service.BlogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/blog")
@RequiredArgsConstructor
@Tag(name = "Blog", description = "Endpoints for all things blog")
public class BlogController {
    private final BlogService blogService;

    @Operation(summary = "Create new blog")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
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
    @PostMapping(produces = "application/json", consumes = "multipart/form-data")
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
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "201",
                        description = "Created",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
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
                @ApiResponse(
                        responseCode = "500",
                        description = "Internal Server Error",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @PutMapping(value = "/{blogId}", produces = "application/json", consumes = "multipart/form-data")
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

    @Operation(summary = "Fetch blog by Id")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @GetMapping(value = "/{blogId}", produces = "application/json")
    public ResponseEntity<?> fetchBlogById(@PathVariable String blogId) {
        try {
            return new ResponseEntity<>(blogService.readBlogByID(Integer.valueOf(blogId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
                            .message(e.getMessage())
                            .build(),
                    HttpStatus.NOT_FOUND);
        }
    }

    @Operation(summary = "Fetch blog by user id")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @GetMapping(value = "/user/{userId}", produces = "application/json")
    public ResponseEntity<BlogResponseDTO> fetchBlogByUserId(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(blogService.readBlogByUserId(Integer.valueOf(userId), page, size), HttpStatus.OK);
    }

    @Operation(summary = "Fetch all blogs")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @GetMapping(produces = "application/json")
    public ResponseEntity<BlogResponseDTO> fetchAllBlogs(
            @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        return new ResponseEntity<>(blogService.readAllBlogs(page, size), HttpStatus.OK);
    }

    @Operation(summary = "Delete a blog post")
    @SecurityRequirement(name = "Bearer Authentication")
    @ApiResponses(
            value = {
                @ApiResponse(
                        responseCode = "200",
                        description = "OK",
                        content = @Content(schema = @Schema(implementation = BlogResponseDTO.class))),
                @ApiResponse(
                        responseCode = "401",
                        description = "Unauthorized",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
                @ApiResponse(
                        responseCode = "404",
                        description = "Not Found",
                        content = @Content(schema = @Schema(implementation = ErrorResponseDTO.class))),
            })
    @DeleteMapping(value = "/{blogId}", produces = "application/json")
    public ResponseEntity<?> deleteBlogById(@PathVariable String blogId) {
        try {
            return new ResponseEntity<>(blogService.deleteBlogById(Integer.valueOf(blogId)), HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(
                    ErrorResponseDTO.builder()
                            .statusCode(400)
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
