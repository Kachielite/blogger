package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.BlogRequestDTO;
import com.derrick.blogger.dto.BlogResponseDTO;
import com.derrick.blogger.model.Blog;
import com.derrick.blogger.model.User;
import com.derrick.blogger.repository.BlogRepository;
import com.derrick.blogger.repository.UserRepository;
import com.derrick.blogger.service.BlogService;
import com.derrick.blogger.service.CloudinaryService;
import com.derrick.blogger.utils.SlugGenerator;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BlogServiceImpl implements BlogService {
    private final BlogRepository blogRepository;
    private final UserRepository userRepository;
    private final SlugGenerator slugGenerator;
    private final CloudinaryService cloudinaryService;

    @Override
    public BlogResponseDTO createBlog(BlogRequestDTO blogRequestDTO) throws IOException {
        String coverPhotoUrl = null;
        try {
            log.info("New blog article received, title: {}", blogRequestDTO.title());
            // Retrieve the authenticated user's details
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName(); // Get the username from the authentication object
            Optional<User> searchUser = userRepository.findByEmail(username);
            User user = searchUser.get();

            if (blogRequestDTO.coverPhoto() != null
                    && !blogRequestDTO.coverPhoto().isEmpty()) {
                log.info("Uploading blog cover photo");
                coverPhotoUrl = cloudinaryService.uploadImage(blogRequestDTO.coverPhoto(), "blogger-post-images");
                log.info("Blog cover photo uploaded");
            }
            log.info("Creating new blog");
            Blog newBlog = Blog.builder()
                    .title(blogRequestDTO.title())
                    .slug(slugGenerator.generateSlug(blogRequestDTO.title()))
                    .coverPhoto(coverPhotoUrl)
                    .user(user)
                    .status(blogRequestDTO.status())
                    .content(blogRequestDTO.content())
                    .build();
            Blog savedBlog = blogRepository.save(newBlog);
            log.info("Saved ew blog");

            // Construct the BlogResponseDTO with a list containing the saved blog
            List<BlogResponseDTO.BlogList> blogs = new ArrayList<>();
            BlogResponseDTO.BlogList blogList = BlogResponseDTO.BlogList.builder()
                    .id(savedBlog.getId())
                    .title(savedBlog.getTitle())
                    .coverPhoto(savedBlog.getCoverPhoto())
                    .slug(savedBlog.getSlug())
                    .content(savedBlog.getContent())
                    .status(savedBlog.getStatus())
                    .author(BlogResponseDTO.BlogList.Author.builder()
                            .firstName(savedBlog.getUser().getFirstName())
                            .lastName(savedBlog.getUser().getLastName())
                            .email(savedBlog.getUser().getEmail())
                            .profilePhoto(savedBlog.getUser().getProfilePhoto())
                            .build())
                    .build();
            blogs.add(blogList);

            return BlogResponseDTO.builder()
                    .statusCode(201)
                    .message("Blog created successfully")
                    .blogs(blogs)
                    .build();

        } catch (IOException e) {
            log.error("Error uploading blog photo");
            throw e;
        }
    }

    @Override
    public BlogResponseDTO readBlogByID(Integer blogId) {
        return null;
    }

    @Override
    public BlogResponseDTO readBlogByUserId(Integer userId) {
        return null;
    }

    @Override
    public BlogResponseDTO readAllBlogs() {
        return null;
    }

    @Override
    public BlogResponseDTO updateBlog(Integer blogId) {
        return null;
    }

    @Override
    public BlogResponseDTO deleteBlogById(Integer blogId) {
        return null;
    }

}
