package com.whitetail.learningspring.domain.util;

import com.whitetail.learningspring.domain.User;

public abstract class MessageHelper {
    public static String getAuthorName(User author) {
        return author != null ? author.getUsername() : "none";
    }
}
