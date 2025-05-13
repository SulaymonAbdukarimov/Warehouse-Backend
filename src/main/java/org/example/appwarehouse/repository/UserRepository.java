package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
   Optional<User> findTopByUserByCodeDesc();
   Optional<User> findByPhone(String phone);
}
