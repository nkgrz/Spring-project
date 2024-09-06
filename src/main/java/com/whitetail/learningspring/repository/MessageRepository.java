package com.whitetail.learningspring.repository;

import com.whitetail.learningspring.domain.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    Page<Message> findByTag(String tag, Pageable pageable);
    void deleteByAuthor_Id(Long userId);
    List<Message> findByAuthor_Id(Long userId);
    Page<Message> findByAuthor_Id(Long userId, Pageable pageable);
    void deleteMessageById(Long userId);
}
