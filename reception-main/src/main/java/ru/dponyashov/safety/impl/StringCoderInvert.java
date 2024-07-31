package ru.dponyashov.safety.impl;

import org.springframework.stereotype.Component;
import ru.dponyashov.safety.StringEncoder;

import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class StringCoderInvert implements StringEncoder {

    private static final String alphabet = "0123456789"+
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"+
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"+
            "_ ()+-=.{}@";
    private static Map<String, String> invertChars;

    static {
        initInvertChars();
    }

    @Override
    public String encode(String data) {
        return encodeStringData(data);
    }

    @Override
    public String decode(String data) {
        return encodeStringData(data);
    }

    private String encodeStringData(String text) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            String textChar = String.valueOf(text.charAt(i));
            String newChar = invertChars.get(textChar);
            result.append(Objects.requireNonNullElse(newChar, textChar));
        }
        return result.toString();
    }

    private static void initInvertChars() {
        invertChars = IntStream.iterate(0, i -> i + 1)
                .limit(alphabet.length())
                .boxed()
                .collect(Collectors.toMap(i -> String.valueOf(alphabet.charAt(i)),
                        i -> String.valueOf(alphabet.charAt(alphabet.length() - i - 1))));
    }
}
