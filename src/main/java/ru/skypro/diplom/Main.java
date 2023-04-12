package ru.skypro.diplom;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.skypro.diplom.enums.Role;

import java.util.List;

import static ru.skypro.diplom.enums.Role.USER;


@OpenAPIDefinition
@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        LoggerFactory.getLogger(Main.class).info("!  started  !");

    }

}
