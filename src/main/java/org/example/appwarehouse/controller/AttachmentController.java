package org.example.appwarehouse.controller;

import org.example.appwarehouse.entity.Attachment;
import org.example.appwarehouse.entity.AttachmentContent;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.AttachmentRepository;
import org.example.appwarehouse.service.AttachmentService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartHttpServletRequest;

@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AttachmentRepository attachmentRepository;

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

    @GetMapping("/photos/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Integer id) {
        byte[] imageData =  attachmentService.getImage(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

}
