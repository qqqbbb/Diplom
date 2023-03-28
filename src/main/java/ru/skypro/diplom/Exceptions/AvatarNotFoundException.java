package ru.skypro.diplom.Exceptions;

public class AvatarNotFoundException extends RuntimeException {

    public AvatarNotFoundException() {
        super();
    }

    public AvatarNotFoundException(String message) {
        super(message);
    }
}
