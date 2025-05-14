package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Integer> {
    boolean existsByPhoneNumber(String phoneNumber);
}
