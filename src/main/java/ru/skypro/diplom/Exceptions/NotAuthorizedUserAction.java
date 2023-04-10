package ru.skypro.diplom.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public class NotAuthorizedUserAction extends RuntimeException {
    public NotAuthorizedUserAction() {
        super();
    }

    public NotAuthorizedUserAction(String s) {
        super(s);
    }
}