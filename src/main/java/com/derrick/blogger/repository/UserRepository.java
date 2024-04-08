package com.derrick.blogger.repository;

import com.derrick.blogger.model.User;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);

    Page<User> findAll(Pageable pageable);
}
