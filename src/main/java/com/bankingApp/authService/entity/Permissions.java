package com.bankingApp.authService.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "permissions")
@Data
public class Permissions {
    @Id
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
}
