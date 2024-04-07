package com.derrick.blogger.dto;

import com.derrick.blogger.model.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
public class UserResponseDTO {
    private String message;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<User> users;

    public static UserResponseDTO messageOnly(String message) {
        return UserResponseDTO.builder().message(message).build();
    }
}
