package com.derrick.blogger.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "reset_tokens")
public class ResetToken {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private String token;
    private Integer userId;
    private LocalDateTime expiryDate;

    @CreationTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
}
