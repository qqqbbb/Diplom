package ru.skypro.diplom.Exceptions;

public class UserAlreadyRegisteredException extends RuntimeException {

    public UserAlreadyRegisteredException() {
        super();
    }

    public UserAlreadyRegisteredException(String message) {
        super(message);
    }
}
