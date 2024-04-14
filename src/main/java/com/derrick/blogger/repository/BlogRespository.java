package com.derrick.blogger.repository;

import com.derrick.blogger.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRespository extends JpaRepository<Blog, Integer> {}
