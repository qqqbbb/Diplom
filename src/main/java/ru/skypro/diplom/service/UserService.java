package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.NewPassword;
import ru.skypro.diplom.DTO.UserDTO;
import ru.skypro.diplom.Exceptions.UserNotFoundException;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;
import java.time.LocalDate;
import java.time.format.*;
import java.util.Optional;

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
        return new User(userDTO.getId(), userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone());
    }

    public UserDTO userToDTO(User user){
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getPhone());
    }

    public UserDTO updateUser (UserDTO userDTO){
        log.info("updateUser " + userDTO);
        User user = userRepository.save(dtoToUser(userDTO));
        return userToDTO(user);
    }

    public User addUser (UserDTO userDTO){
        log.info("AddUser " + userDTO);
        return userRepository.save(dtoToUser(userDTO));
    }

    public void changePassword (NewPassword newPassword){
        User user = getCurrentUser();
        log.info("changePassword " + user);

    }

    public void updateAvater (MultipartFile file){
        User user = getCurrentUser();
        log.info("updateAvater " + user);

    }

    public User getCurrentUser (){
        log.info(" getCurrentUser " );
        Optional<User> optionalUser = userRepository.findById(1);
        return optionalUser.orElseThrow(() -> new UserNotFoundException());
    }
}
