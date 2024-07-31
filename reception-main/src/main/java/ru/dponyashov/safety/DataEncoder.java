package ru.dponyashov.safety;

public interface DataEncoder {
    <T> T encode(T instance);
    <T> T decode(T instance);
}