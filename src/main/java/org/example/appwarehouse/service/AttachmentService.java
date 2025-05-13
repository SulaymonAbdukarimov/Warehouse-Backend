package org.example.appwarehouse.service;

import lombok.SneakyThrows;
import org.example.appwarehouse.entity.Attachment;
import org.example.appwarehouse.entity.AttachmentContent;
import org.example.appwarehouse.payload.Result;
import org.example.appwarehouse.repository.AttachmentContentRepository;
import org.example.appwarehouse.repository.AttachmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.Iterator;
import java.util.Optional;

@Service
public class AttachmentService {
    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private AttachmentContentRepository attachmentContentRepository;

    @SneakyThrows
    public Result uploadFile(MultipartHttpServletRequest request) {
        Iterator<String> fileNames = request.getFileNames();
        MultipartFile file = request.getFile(fileNames.next());

        // ATTACHMENT
        Attachment attachment = new Attachment();
        attachment.setName(file.getOriginalFilename());
        attachment.setSize(file.getSize());
        attachment.setContentType(file.getContentType());
        Attachment savedAttachment = attachmentRepository.save(attachment);


        // ATTACHMENT CONTENT
        AttachmentContent attachmentContent = new AttachmentContent();
        attachmentContent.setBytes(file.getBytes());
        attachmentContent.setAttachment(savedAttachment);
        attachmentContentRepository.save(attachmentContent);

        return new Result("Fayl saqlandi",true,savedAttachment.getId());
    }

    public Result deleteAttachment(Integer id) {
        Optional<Attachment> optionalAttachment = attachmentRepository.findById(id);

        if (!optionalAttachment.isPresent()) {
            return new Result("Bundey fayl mavjud emas",false);
        }


        return new Result("Fayl sqlandi",true,null);
    }
}
