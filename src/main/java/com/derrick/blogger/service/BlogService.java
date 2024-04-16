package com.derrick.blogger.service;

import com.derrick.blogger.dto.BlogRequestDTO;
import com.derrick.blogger.dto.BlogResponseDTO;
import com.derrick.blogger.dto.BlogUpdateDTO;
import com.derrick.blogger.exceptions.BadRequestException;
import com.derrick.blogger.exceptions.InternalServerErrorException;
import com.derrick.blogger.exceptions.NotFoundException;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public interface BlogService {
    BlogResponseDTO createBlog(BlogRequestDTO blogRequestDTO) throws IOException, BadRequestException;

    BlogResponseDTO updateBlog(Integer blogId, BlogUpdateDTO blogUpdateDTO)
            throws NotFoundException, BadRequestException, InternalServerErrorException, IOException;

    BlogResponseDTO readBlogByID(Integer blogId) throws NotFoundException, InternalServerErrorException;

    BlogResponseDTO readBlogByUserId(Integer userId) throws NotFoundException, InternalServerErrorException;

    BlogResponseDTO readAllBlogs();

    BlogResponseDTO deleteBlogById(Integer blogId) throws NotFoundException;
}
