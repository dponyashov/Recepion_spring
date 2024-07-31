package ru.dponyashov.safety.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dponyashov.safety.StringEncoder;

//@Component
public class StringEncoderVigener implements StringEncoder {
    private final String vgrLabel = "{VGR}";
    private final String alphabet = "0123456789"+
            "АБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯабвгдеёжзийклмнопрстуфхцчшщъыьэюя"+
            "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"+
            "_ ()+-=.{}@";

    @Value("${safety.key}")
    private String key;

    @Override
    public String encode(String data) {
        return encodeStringData(vgrLabel + data, this::encodeOperator);
    }

    @Override
    public String decode(String data) {
        String decodeText = encodeStringData(data, this::decodeOperator);
        if(!decodeText.startsWith(vgrLabel)){
            return data;
        }
        return decodeText.substring(vgrLabel.length());
    }

    private String encodeStringData(String text, Operator codeOperator) {
        StringBuilder result = new StringBuilder();
        for(int i = 0; i < text.length(); i++) {
            int keyIndex = i % key.length();

            String textChar = String.valueOf(text.charAt(i));
            String keyChar = String.valueOf(key.charAt(keyIndex));

            int textCharIndex = alphabet.indexOf(textChar);
            int keyCharIndex = alphabet.indexOf(keyChar);
            if(textCharIndex < 0 || keyCharIndex < 0){
                result.append(text.charAt(i));
            } else {
                int newIndex = codeOperator.apply(textCharIndex, keyCharIndex);
                result.append(alphabet.charAt(newIndex));
            }
        }
        return result.toString();
    }

    @FunctionalInterface
    private interface Operator{
        int apply(int textIndex, int keyIndex);
    }

    private int encodeOperator(int textIndex, int keyIndex){
        int alphabetLen = alphabet.length();
        return (textIndex + keyIndex) % alphabetLen;
    }

    private int decodeOperator(int textIndex, int keyIndex){
        int alphabetLen = alphabet.length();
        return (textIndex - keyIndex + alphabetLen) % alphabetLen;
    }
}
