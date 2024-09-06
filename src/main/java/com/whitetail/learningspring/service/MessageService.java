package com.whitetail.learningspring.service;

import ch.qos.logback.core.util.StringUtil;
import com.whitetail.learningspring.domain.Message;
import com.whitetail.learningspring.domain.User;
import com.whitetail.learningspring.domain.dto.MessageDto;
import com.whitetail.learningspring.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
public class MessageService {

    private final MessageRepository messageRepository;
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    public Page<MessageDto> getMessages(Pageable pageable, User user, String tag) {
        if (tag != null && !tag.isEmpty()) {
            return messageRepository.findByTag(pageable, tag, user);
        } else {
            return messageRepository.findAll(pageable, user);
        }
    }

    public void addNewMessage(Message message, String tag, MultipartFile file) throws IOException {
        saveMessage(message, null, tag, file, false);
    }

    public void saveMessage(Message message, String text,
                            String tag, MultipartFile file,
                            boolean isUpdate) throws IOException {
        boolean isChanged = false;

        if (!StringUtil.isNullOrEmpty(text)) {
            message.setText(text);
            isChanged = true;
        }
        if (tag != null) {
            message.setTag(tag);
            isChanged = true;
        }
        if (file != null && !file.isEmpty()) {
            deleteFile(message.getFilename());
            String fileName = saveImage(file);
            message.setFilename(fileName);
            isChanged = true;
        }

        if (!isUpdate || isChanged) {
            messageRepository.save(message);
        }
    }

    public Page<MessageDto> findMessagesByUser(Pageable pageable, User user, User currentUser) {
        return messageRepository.findByUser(pageable, user, currentUser);
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
        messages.forEach(message -> {
            String filename = message.getFilename();
            deleteFile(filename);
        });
    }

    public void deleteFile(String filename) {
        if (filename != null && !filename.isEmpty()) {
            Path filePath = Paths.get(uploadPath, filename);
            try {
                Files.deleteIfExists(filePath);
            } catch (IOException e) {
                throw new RuntimeException("Could not delete file: " + filename, e);
            }
        }
    }

    @Transactional
    public void deleteMessage(Long messageId, Long userId) {
        Message message = messageRepository.findById(messageId).orElse(null);
        if (message != null && Objects.equals(message.getAuthor().getId(), userId)) {
            deleteFile(message.getFilename());
            messageRepository.deleteMessageById(message.getId());
        }
    }

    public void likeMessage(Message message, User user) {
        Set<User> likes = message.getLikes();
        if (likes.contains(user)) {
            likes.remove(user);
        } else {
            likes.add(user);
        }
        message.setLikes(likes);
        messageRepository.save(message);
    }
}
