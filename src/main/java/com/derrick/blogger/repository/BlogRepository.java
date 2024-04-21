package com.derrick.blogger.repository;

import com.derrick.blogger.model.Blog;
import java.util.List;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface BlogRepository extends JpaRepository<Blog, Integer> {
    List<Blog> findByUserId(Integer userId, Pageable pageable);

    Page<Blog> findAll(Pageable pageable);
}
