package com.banquito.platform.identity.domain.repository;

import com.banquito.platform.identity.domain.model.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {
}
