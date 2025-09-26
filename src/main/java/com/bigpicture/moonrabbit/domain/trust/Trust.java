package com.bigpicture.moonrabbit.domain.trust;

import lombok.Getter;

@Getter
public enum Trust {
    RECEIVE_LIKE(1),
    ANSWER_ACCEPTED(10),
    REPORTED(-5);

    private final int value;

    Trust(int value) {
        this.value = value;
    }
}
