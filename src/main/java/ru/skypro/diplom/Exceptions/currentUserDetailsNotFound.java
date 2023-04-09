package ru.skypro.diplom.Exceptions;

public class currentUserDetailsNotFound extends RuntimeException {
    public currentUserDetailsNotFound() {
        super();
    }

    public currentUserDetailsNotFound(String s) {
        super(s);
    }
}