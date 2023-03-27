package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.UserDTO;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public LocalDate parseDate(String date) {
        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//            return LocalDate.parse(date.substring(0, 16), formatter);
            return LocalDate.parse(date);
        }
        catch (DateTimeParseException e)
        {
            log.error("Unable to parse date: " + date);
            e.printStackTrace();
        }
        return null;
    }

    public User dtoToUser(UserDTO userDTO){
        LocalDate localDate = parseDate(userDTO.getRegDate());
        return new User(userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone(), localDate, userDTO.getCity());
    }

    public UserDTO userToDTO(User user){
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone(), user.getRegDate().toString(), user.getCity());
    }

    public UserDTO updateUser (UserDTO userDTO){
        log.info("updateUser " + userDTO);
        User user = userRepository.save(dtoToUser(userDTO));
        return userToDTO(user);
    }

//    public User testSavingUser (UserDTO userDTO){
//        log.info("testSavingUser " + userDTO);
//        return userRepository.save(dtoToUser(userDTO));
//    }
}
