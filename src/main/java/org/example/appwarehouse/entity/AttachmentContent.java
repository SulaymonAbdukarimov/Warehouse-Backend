package org.example.appwarehouse.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class AttachmentContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private byte[] bytes;

    @OneToOne
    @JoinColumn(name = "attachment_id")
    private Attachment attachment;

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
        if (attachment != null && attachment.getAttachmentContent() != this) {
            attachment.setAttachmentContent(this);
        }
    }

}
