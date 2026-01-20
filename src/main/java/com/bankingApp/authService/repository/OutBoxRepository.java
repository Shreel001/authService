package com.bankingApp.authService.repository;

import com.bankingApp.authService.entity.Outbox;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<Outbox, Long> {
    List<Outbox> findByStatus(String status);
}
