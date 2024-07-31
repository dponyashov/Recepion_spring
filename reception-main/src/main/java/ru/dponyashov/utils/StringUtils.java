package ru.dponyashov.utils;

public class StringUtils {
    public static String stringFilterPattern(String text) {
        return (text != null ? "%" + text.trim() + "%" : null );
    }
}