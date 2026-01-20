package com.bankingApp.authService.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "outbox")
public class Outbox {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "aggregate_type")
    private String aggregate_type;

    @Column(name = "aggregate_id")
    private UUID aggregate_id;

    private String type;

    private String payload;

    private String status;

    private int retries;

    private String topic;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "processed_at")
    private LocalDateTime processedAt;

}
