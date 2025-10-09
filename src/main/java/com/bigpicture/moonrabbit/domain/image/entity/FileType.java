package com.bigpicture.moonrabbit.domain.image.entity;

public enum FileType {
    BANNER("banners/"),
    BORDER("profile-borders/"),
    PROFILE("user-profile/");

    private final String prefix;

    FileType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
