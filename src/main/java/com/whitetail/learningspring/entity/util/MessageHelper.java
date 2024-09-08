package com.whitetail.learningspring.entity.util;

import com.whitetail.learningspring.entity.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "none";
    }
}
