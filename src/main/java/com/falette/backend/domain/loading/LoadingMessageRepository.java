package com.falette.backend.domain.loading;

import org.springframework.data.jpa.repository.JpaRepository;

public interface LoadingMessageRepository extends JpaRepository<LoadingMessage, Long> {
}