package com.bankingApp.authService.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Data
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(name = "customer_id", columnDefinition = "BINARY(16)")
    private UUID id;

    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8)
    private String password;

    @ManyToOne
    @JoinColumn(name = "role_id")
    private Role role;
}

