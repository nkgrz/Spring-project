package com.whitetail.learningspring.service;

import com.whitetail.learningspring.domain.Message;
import com.whitetail.learningspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class MessageService {

    @Value("${upload.path}")
    private String uploadPath;

    private final MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public void save(Message message) {
        messageRepository.save(message);
    }

    public List<Message> findAll() {
        return messageRepository.findAll();
    }

    public List<Message> findByTag(String tag) {
        return messageRepository.findByTag(tag);
    }

    public String saveImage(MultipartFile file) throws IOException {
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }
        String uuidFile = UUID.randomUUID().toString();
        String fileName = uuidFile + "." + file.getOriginalFilename();
        file.transferTo(new File(uploadPath + fileName));
        return fileName;
    }
    public void deleteMessages() {
        deleteMessages(null);
    }

    public void deleteMessages(Long userId) {
        List<Message> messages;
        if (userId != null) {
            messages = messageRepository.findByAuthor_Id(userId);
            messageRepository.deleteByAuthor_Id(userId);
        } else {
            messages = messageRepository.findAll();
            messageRepository.deleteAll();
        }
        // Удаляем все сообщения и связанные с ними файлы
        messages.forEach(message -> {
            String filename = message.getFilename();
            deleteFile(filename);
        });
    }

    public void deleteFile(String filename) {
        if (filename != null && !filename.isEmpty()) {
            Path filePath = Paths.get(uploadPath, filename);
            try {
                Files.deleteIfExists(filePath); // Удаляем файл, если он существует
            } catch (IOException e) {
                throw new RuntimeException("Could not delete file: " + filename, e);
            }
        }
    }
}
