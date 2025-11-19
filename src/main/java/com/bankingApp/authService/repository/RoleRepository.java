package com.bankingApp.authService.repository;

import com.bankingApp.authService.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    com.bankingApp.authService.entity.Role findByName(String name);
}
