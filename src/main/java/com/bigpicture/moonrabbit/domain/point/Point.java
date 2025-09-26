package com.bigpicture.moonrabbit.domain.point;

import lombok.Getter;

@Getter
public enum Point {
    CREATE_BOARD(10),
    DELETE_BOARD(-10),
    CREATE_ANSWER(5),
    DELETE_ANSWER(-5),
    CREATE_DAILY_ANSWER(3);

    private final int value;

    Point(int value) {
        this.value = value;
    }
}