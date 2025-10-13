package com.bigpicture.moonrabbit.domain.image.entity;

public enum FileType {
    BANNER("banners/"),
    BORDER("profile-borders/"),
    PROFILE("user-profile/"),
    BADGE("badge"),
    NAME_COLOR("name-color/");

    private final String prefix;

    FileType(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}
