package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
   @Query(value = "SELECT * FROM users WHERE code ~ '^[0-9]+$' ORDER BY CAST(code AS INTEGER) DESC LIMIT 1", nativeQuery = true)
   Optional<User> findTopByCodeAsNumberDesc();

   Optional<User> findByPhoneNumber(String phone);
}
