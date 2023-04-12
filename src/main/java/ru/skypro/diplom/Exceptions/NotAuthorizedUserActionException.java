package ru.skypro.diplom.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedUserActionException extends RuntimeException {
    public NotAuthorizedUserActionException() {
        super();
    }

    public NotAuthorizedUserActionException(String s) {
        super(s);
    }
}