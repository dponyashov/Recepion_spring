package ru.dponyashov.utils;

import lombok.Getter;

@Getter
public enum NotifyCode {
    PHONE("PHONE"),
    MAIL("MAIL");

    private final String value;

    NotifyCode(String value) {
        this.value = value;
    }
}