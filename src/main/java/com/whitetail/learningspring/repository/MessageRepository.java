package com.whitetail.learningspring.repository;

import com.whitetail.learningspring.domain.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByTag(String tag);
    void deleteByAuthor_Id(Long userId);
}
