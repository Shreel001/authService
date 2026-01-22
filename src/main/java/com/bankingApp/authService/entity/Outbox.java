package com.bankingApp.authService.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "outbox")
public class Outbox {

    @Id
    @GeneratedValue(generator = "snowflake")
    @GenericGenerator(
            name = "snowflake",
            strategy = "com.example.id.SnowflakeIdGenerator"
    )
    private Long id;

    @Column(name = "aggregate_type", nullable = false)
    private String aggregate_type;

    @Column(name = "aggregate_id", nullable = false)
    private UUID aggregate_id;

    @Column(nullable = false)
    private String type;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String payload;

    @Column(nullable = false)
    private String status;

    @Column(nullable = false)
    private int retries = 0;

    @Column(nullable = false)
    private String topic;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

    @Column(name = "error_message")
    private String errorMessage;
}
