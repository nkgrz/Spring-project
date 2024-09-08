package com.whitetail.learningspring.entity.dto;

import com.whitetail.learningspring.entity.Message;
import com.whitetail.learningspring.entity.User;
import com.whitetail.learningspring.entity.util.MessageHelper;
import lombok.Getter;

@Getter
public class MessageDto {
    private final Long id;
    private final String text;
    private final String tag;
    private final User author;
    private final String filename;
    private final Long likes;
    private final Boolean meLiked;

    public MessageDto(Message message, Long likes, Boolean meLiked) {
        this.id = message.getId();
        this.text = message.getText();
        this.tag = message.getTag();
        this.author = message.getAuthor();
        this.filename = message.getFilename();
        this.likes = likes;
        this.meLiked = meLiked;
    }

    public String getAuthorName() {
        return MessageHelper.getAuthorName(author);
    }

    @Override
    public String toString() {
        return "MessageDto{" +
                "id=" + id +
                ", author=" + author +
                ", likes=" + likes +
                ", meLiked=" + meLiked +
                '}';
    }
}
