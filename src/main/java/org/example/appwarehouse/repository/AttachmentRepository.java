package org.example.appwarehouse.repository;

import org.example.appwarehouse.entity.Attachment;
import org.example.appwarehouse.entity.AttachmentContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Integer> {
    @Query("SELECT a FROM Attachment a JOIN FETCH a.attachmentContent WHERE a.id = :id")
    Optional<Attachment> findByIdWithContent(@Param("id") Integer id);
}
