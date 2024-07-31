package ru.dponyashov.safety.impl;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.dponyashov.safety.DataEncoder;
import ru.dponyashov.safety.StringEncoder;
import ru.dponyashov.safety.annotation.StringToEncode;

import java.lang.reflect.Field;

@Component
@RequiredArgsConstructor
public class SimpleDataEncoder implements DataEncoder {

    private final StringEncoder stringEncoder;

    @Override
    public <T> T encode(T instance) {
        objectEncode(instance, stringEncoder::encode);
        return instance;
    }

    @Override
    public <T> T decode(T instance) {
        objectEncode(instance, stringEncoder::decode);
        return instance;
    }

    private void objectEncode(Object instance, Coder coder) {
        Class<?> cls = instance.getClass();
        while (cls != null) {
            for (Field field : cls.getDeclaredFields()) {
                if(field.getType().equals(String.class)){
                    StringFieldEncode(field, instance, coder);
                } else if (!field.getType().isPrimitive()) {
                    Object objectField = fieldObject(field, instance);
                    if(objectField != null && objectField.getClass().isAnnotationPresent(Entity.class)){
                        objectEncode(objectField, coder);
                    }
                }
            }
            cls = cls.getSuperclass();
        }
    }

    private void StringFieldEncode(Field field, Object instance, Coder coder) {
        if (!field.isAnnotationPresent(StringToEncode.class)){
            return;
        }

        StringToEncode annotation = field.getAnnotation(StringToEncode.class);
        if (!annotation.on()){
            return;
        }

        textEncode(field, instance, coder);
    }

    private Object fieldObject(Field field, Object instance){
        try {
            boolean isAccess = field.canAccess(instance);
            field.setAccessible(true);
            Object subObject = field.get(instance);
            field.setAccessible(isAccess);
            return subObject;
        } catch (Exception ignore) {
        }
        return null;
    }

    private void textEncode(Field field, Object instance, Coder coder) {
        try {
            boolean isAccess = field.canAccess(instance);
            field.setAccessible(true);
            String fieldValue = field.get(instance).toString();
            String newValue = coder.coding(fieldValue);
            field.set(instance, newValue);
            field.setAccessible(isAccess);
        } catch (Exception ignore) {
        }
    }

    @FunctionalInterface
    interface Coder{
        String coding(String text);
    }
}
