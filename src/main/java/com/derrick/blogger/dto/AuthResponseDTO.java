package com.derrick.blogger.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class AuthResponseDTO {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String token;

    // Constructors, getters, setters, and any other methods...

    public static AuthResponseDTO messageOnly(String message) {
        return AuthResponseDTO.builder().message(message).build();
    }
}
