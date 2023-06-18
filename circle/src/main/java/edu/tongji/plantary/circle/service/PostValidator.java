package edu.tongji.plantary.circle.service;

import edu.tongji.plantary.circle.entity.Post;
import edu.tongji.plantary.circle.entity.UserItem;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PostValidator {
    private static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static void validatePost(Post post) {
        validatePoster(post.getPoster());
        validateThemeName(post.getThemeName());
        validateContent(post.getContent());
        validateReleaseTime(post.getReleaseTime());
    }

    public static void validatePoster(UserItem poster) {
        Objects.requireNonNull(poster, "Poster cannot be null.");
        validatePhone(poster.getPhone());
        validateName(poster.getName());
        validatePicture(poster.getPicture());
    }

    public static void validatePhone(String phone) {
        Objects.requireNonNull(phone, "Phone number cannot be null.");
        if (!phone.matches("\\d{11}")) {
            throw new IllegalArgumentException("Phone number must be 11 digits.");
        }
    }

    public static void validateName(String name) {
        Objects.requireNonNull(name, "Name cannot be null.");
        if (name.length() < 1 || name.length() > 30) {
            throw new IllegalArgumentException("Name length must be between 1 and 30.");
        }
    }

    public static void validatePicture(String picture) {
        Objects.requireNonNull(picture, "Picture cannot be null.");
        if (picture.isEmpty()) {
            throw new IllegalArgumentException("Picture cannot be empty.");
        }
        if (picture.length() > 2083) {
            throw new IllegalArgumentException("Picture length must be less than 2083.");
        }
    }

    public static void validateThemeName(String themeName) {
        Objects.requireNonNull(themeName, "Theme name cannot be null.");
        if (themeName.length() < 1 || themeName.length() > 30) {
            throw new IllegalArgumentException("Theme name length must be between 1 and 30.");
        }
    }

    public static void validateContent(String content) {
        Objects.requireNonNull(content, "Content cannot be null.");
        if (content.length() < 1 || content.length() > 1023) {
            throw new IllegalArgumentException("Content length must be between 1 and 1023.");
        }
    }

    public static void validateReleaseTime(String releaseTime) {
        Objects.requireNonNull(releaseTime, "Release time cannot be null.");

        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
            LocalDateTime.parse(releaseTime, formatter);
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid release time format. Expected format: " + DATE_TIME_FORMAT);
        }
    }
}
