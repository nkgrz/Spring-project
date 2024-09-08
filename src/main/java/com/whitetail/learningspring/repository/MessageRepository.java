package com.whitetail.learningspring.repository;

import com.whitetail.learningspring.entity.Message;
import com.whitetail.learningspring.entity.User;
import com.whitetail.learningspring.entity.dto.MessageDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    void deleteByAuthor_Id(Long userId);

    List<Message> findByAuthor_Id(Long userId);

    void deleteMessageById(Long userId);

    @Query("select new com.whitetail.learningspring.entity.dto.MessageDto(" +
            "        m, " +
            "        count(ml), " +
            "        (sum(case when ml = :user then 1 else 0 end)  > 0) " +
            "        ) " +
            "        from Message m left join m.likes ml " +
            "        group by m ")
    Page<MessageDto> findAll(Pageable pageable, @Param("user") User user);


    @Query("select new com.whitetail.learningspring.entity.dto.MessageDto(" +
            "        m, " +
            "        count(ml), " +
            "        (sum(case when ml = :user then 1 else 0 end)  > 0) " +
            "        ) " +
            "        from Message m left join m.likes ml " +
            "        where m.tag = :tag " +
            "        group by m ")
    Page<MessageDto> findByTag(Pageable pageable, @Param("tag") String tag, @Param("user") User user);

    @Query("select new com.whitetail.learningspring.entity.dto.MessageDto(" +
            "        m, " +
            "        count(ml), " +
            "        (sum(case when ml = :currentUser then 1 else 0 end)  > 0) " +
            "        ) " +
            "        from Message m left join m.likes ml " +
            "        where m.author = :user " +
            "        group by m ")
    Page<MessageDto> findByUser(Pageable pageable, @Param("user") User user, @Param("currentUser") User currentUser);
}
