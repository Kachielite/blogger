package com.derrick.blogger.repository;

import com.derrick.blogger.model.Blog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BlogRepository extends JpaRepository<Blog, Integer> {}
