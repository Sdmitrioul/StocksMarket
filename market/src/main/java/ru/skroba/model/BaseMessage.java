package ru.skroba.model;

public record BaseMessage(int code, String message) {
    @Override
    public String toString() {
        return '{' + "\"code\": " + code + ", \"message\": \"" + message + "\"}";
    }
}
