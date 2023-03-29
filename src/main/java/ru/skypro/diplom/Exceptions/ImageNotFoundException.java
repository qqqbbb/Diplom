package ru.skypro.diplom.Exceptions;

public class ImageNotFoundException extends RuntimeException {
    public ImageNotFoundException() {
        super();
    }

    public ImageNotFoundException(String s) {
        super(s);
    }
}
