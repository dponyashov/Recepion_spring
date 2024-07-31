package ru.dponyashov.safety.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.dponyashov.safety.StringEncoder;

import java.nio.charset.StandardCharsets;

//@Component
public class StringEncoderXor implements StringEncoder {
    private final String xorLabel = "{XOR}";
    private final String specialText = "_VST_";

    @Value("${safety.key}")
    private String key;

    @Override
    public String encode(String data) {
        return encodeStringData(xorLabel + data)
                .replaceAll("\u0000", specialText);
    }

    @Override
    public String decode(String data) {
        String decodeText = encodeStringData(data
                .replaceAll(specialText,"\u0000")
        );
        if(!decodeText.startsWith(xorLabel)){
            return data;
        }
        return decodeText.substring(xorLabel.length());
    }

    private String encodeStringData(String text) {
        byte[] byteKey = key.getBytes(StandardCharsets.ISO_8859_1);
        byte[] byteText = text.getBytes(StandardCharsets.ISO_8859_1);
        byte[] byteResult = new byte[byteText.length];
        for(int i = 0; i < byteText.length; i++) {
            byteResult[i] = (byte) (byteText[i] ^ byteKey[i % byteKey.length]);
        }
        return new String(byteResult, StandardCharsets.ISO_8859_1);
    }
}
