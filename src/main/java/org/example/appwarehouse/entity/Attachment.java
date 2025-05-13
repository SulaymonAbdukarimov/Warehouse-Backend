package org.example.appwarehouse.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private Long size;

    private String contentType;

    @OneToOne(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttachmentContent attachmentContent;

}
