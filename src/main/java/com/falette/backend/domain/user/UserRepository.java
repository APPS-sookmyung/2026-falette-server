package com.falette.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByGuestUuid(String guestUuid);

    Optional<User> findByGoogleId(String googleId);
}
