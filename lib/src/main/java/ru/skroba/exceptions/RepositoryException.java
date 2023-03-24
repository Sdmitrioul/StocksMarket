package ru.skroba.exceptions;

public class RepositoryException extends RuntimeException {
    public RepositoryException(final String message) {
        super(message);
    }
}
