package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Attachment;
import org.example.appwarehouse.entity.AttachmentContent;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;

    @PostMapping("/upload")
    public Result upload(MultipartHttpServletRequest request) {
        Result result = attachmentService.uploadFile(request);
        return result;
    };

    @DeleteMapping("/{id}")
    public Result deleteAttachment(@PathVariable Integer id) {
        Result result = attachmentService.deleteAttachment(id);
        return result;
    }

    @GetMapping("/content/{id}")
    public AttachmentContent getAttachmentContentById(@PathVariable Integer id) {
        AttachmentContent attachmentContent = attachmentService.getAttachmentContentById(id);
        return attachmentContent;
    }

    @GetMapping("/{id}")
    public Attachment getAttachmentById(@PathVariable Integer id) {
        Attachment attachment = attachmentService.getAttachmentById(id);
        return attachment;
    }
}
