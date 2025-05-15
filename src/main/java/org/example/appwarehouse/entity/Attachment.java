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

    private  String  fileOrginalName;

    private Long size;

    private String contentType;

    @OneToOne(mappedBy = "attachment", cascade = CascadeType.ALL, orphanRemoval = true)
    private AttachmentContent attachmentContent;

    public void setAttachmentContent(AttachmentContent attachmentContent) {
        this.attachmentContent = attachmentContent;
        if (attachmentContent != null && attachmentContent.getAttachment() != this) {
            attachmentContent.setAttachment(this);
        }
    }
}
