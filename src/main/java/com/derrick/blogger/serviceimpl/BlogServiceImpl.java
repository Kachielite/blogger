package com.derrick.blogger.serviceimpl;

import com.derrick.blogger.dto.BlogRequestDTO;
import com.derrick.blogger.dto.BlogResponseDTO;
import com.derrick.blogger.dto.BlogUpdateDTO;
import com.derrick.blogger.exceptions.NotFoundException;
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
            log.info("Saved new blog");

            // Process blog into the BlogResponse DTO
            List<BlogResponseDTO.BlogList> blogs = processBlogData(savedBlog);

            return BlogResponseDTO.builder()
                    .statusCode(201)
                    .message("Blog created successfully")
                    .blogs(blogs)
                    .build();

        } catch (IOException e) {
            log.error("Error uploading blog photo");
            throw e;
        } catch (RuntimeException e) {
            log.error("Something went wrong!");
            throw e;
        }
    }

    @Override
    public BlogResponseDTO updateBlog(Integer blogId, BlogUpdateDTO blogUpdateDTO)
            throws NotFoundException, IOException {
        try {
            log.info("Searching blog with id with " + blogId);
            Optional<Blog> searchedBlog = blogRepository.findById(blogId);
            if (searchedBlog.isEmpty()) {
                throw new NotFoundException("Blog with ID " + blogId + " could not be found");
            }

            Blog blogToUpdate = searchedBlog.get();

            if (blogUpdateDTO.title() != null && !blogUpdateDTO.title().isEmpty()) {
                log.info("Updating blog title and slug");
                blogToUpdate.setTitle(blogUpdateDTO.title());
                blogToUpdate.setSlug(slugGenerator.generateSlug(blogUpdateDTO.title()));
                log.info("Updated blog title and slug");
            }

            if (blogUpdateDTO.coverPhoto() != null
                    && !blogUpdateDTO.coverPhoto().isEmpty()) {
                log.info("Updating blog coverPhoto");
                if (blogToUpdate.getCoverPhoto() != null
                        && blogToUpdate.getCoverPhoto().isEmpty()) {
                    cloudinaryService.deleteImage(blogToUpdate.getCoverPhoto());
                }
                String coverPhoto = cloudinaryService.uploadImage(blogUpdateDTO.coverPhoto(), "blogger-post-images");
                blogToUpdate.setCoverPhoto(coverPhoto);
                log.info("Updated blog coverPhoto");
            }

            if (blogUpdateDTO.content() != null && !blogUpdateDTO.content().isEmpty()) {
                log.info("Updating blog content");
                blogToUpdate.setContent(blogUpdateDTO.content());
                log.info("Updated blog content");
            }

            if (blogUpdateDTO.tags() != null && !blogUpdateDTO.tags().isEmpty()) {
                log.info("Updating blog tags");
                blogToUpdate.setTags(blogToUpdate.getTags());
                log.info("Updated blog tags");
            }

            if (blogUpdateDTO.status() != null) {
                log.info("Updating blog status");
                blogToUpdate.setStatus(blogToUpdate.getStatus());
                log.info("Updated blog status");
            }

            log.info("Updating blog");
            Blog updatedBlog = blogRepository.save(blogToUpdate);

            // Process blog into the BlogResponse DTO
            List<BlogResponseDTO.BlogList> blogs = processBlogData(updatedBlog);

            return BlogResponseDTO.builder()
                    .statusCode(201)
                    .message("Blog created successfully")
                    .blogs(blogs)
                    .build();

        } catch (NotFoundException e) {
            log.error("Blog with ID " + blogId + " could not be found");
            throw e;
        } catch (IOException e) {
            log.error("Error new blog photo");
            throw e;
        } catch (RuntimeException e) {
            log.error("Something went wrong!");
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
    public BlogResponseDTO deleteBlogById(Integer blogId) {
        return null;
    }

    private List<BlogResponseDTO.BlogList> processBlogData(Blog blog) {
        // Construct the BlogResponseDTO with a list containing the saved blog
        List<BlogResponseDTO.BlogList> blogs = new ArrayList<>();
        BlogResponseDTO.BlogList blogList = BlogResponseDTO.BlogList.builder()
                .id(blog.getId())
                .title(blog.getTitle())
                .coverPhoto(blog.getCoverPhoto())
                .slug(blog.getSlug())
                .content(blog.getContent())
                .status(blog.getStatus())
                .author(BlogResponseDTO.BlogList.Author.builder()
                        .firstName(blog.getUser().getFirstName())
                        .lastName(blog.getUser().getLastName())
                        .email(blog.getUser().getEmail())
                        .profilePhoto(blog.getUser().getProfilePhoto())
                        .build())
                .build();
        blogs.add(blogList);

        return blogs;
    }
}
