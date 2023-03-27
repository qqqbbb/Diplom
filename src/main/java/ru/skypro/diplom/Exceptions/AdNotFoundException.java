package ru.skypro.diplom.Exceptions;

public class AdNotFoundException  extends RuntimeException {
    public AdNotFoundException() {
        super();
    }

    public AdNotFoundException(String message) {
        super(message);
    }
}