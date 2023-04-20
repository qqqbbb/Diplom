package ru.skypro.diplom.controller;

import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.skypro.diplom.enums.Role;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.service.*;

import static ru.skypro.diplom.enums.Role.*;

@RestController
@CrossOrigin(value = "http://localhost:3000")
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
    }

    /**
     * Логин-метод
     *
     * @param req дто-объект, содержащий данные для входа
     * @return код 200 - введенные данные верные, вход произведен;<p>
     * код 403 - в ином случае
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginReq req) {
        LoggerFactory.getLogger(this.getClass()).info("login " + req.toString());
        if (authService.login(req.getUsername(), req.getPassword())) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

    }

    /**
     * Метод для регистрации нового пользователя
     *
     * @param req дто-объект, содержащий данные о новом пользователе
     * @return код 201 - если пользователь был создан
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterReq req) {
        LoggerFactory.getLogger(this.getClass()).info("register " + req.toString());
        if (authService.register(req)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }


}
