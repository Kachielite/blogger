package com.derrick.blogger.dto;

import com.derrick.blogger.enums.BlogStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BlogResponseDTO {
    private Integer statusCode;
    private String message;
    private List<BlogList> blogs;

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @RequiredArgsConstructor
    public static class BlogList {
        private Integer id;
        private String title;
        private String coverPhoto;
        private String slug;
        private String content;
        private Author author;
        private BlogStatus status;

        @Getter
        @Setter
        @Builder
        @AllArgsConstructor
        @RequiredArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        @JsonInclude(JsonInclude.Include.NON_NULL)
        public static class Author {
            private String firstName;
            private String lastName;
            private String email;
            private String profilePhoto;
        }
    }
}
