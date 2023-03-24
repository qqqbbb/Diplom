package ru.skypro.diplom.service;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.UserDTO;
import ru.skypro.diplom.Main;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User dtoToUser(UserDTO userDTO) {
        return new User(userDTO.getId(),userDTO.getFirstName(),userDTO.getLastName(),userDTO.getEmail(),userDTO.getPhone(),userDTO.getRegDate(),userDTO.getCity());
    }

    public UserDTO userToDTO(User user) {
        return new UserDTO(user.getId(),user.getFirstName(),user.getLastName(),user.getEmail(),user.getPhone(),user.getRegDate(),user.getCity());
    }

    public User testSavingUser (UserDTO userDTO){
        LoggerFactory.getLogger(this.getClass()).info("testSavingUser " + userDTO);
        return userRepository.save(dtoToUser(userDTO));
    }
}
