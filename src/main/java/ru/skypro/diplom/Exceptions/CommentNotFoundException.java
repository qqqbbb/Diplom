package ru.skypro.diplom.Exceptions;

public class CommentNotFoundException  extends RuntimeException {
    public CommentNotFoundException() {
        super();
    }

    public CommentNotFoundException(String message) {
        super(message);
    }
}