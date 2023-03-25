package ru.skypro.diplom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

//    public User testSavingUser (UserDTO userDTO){
//        log.info("testSavingUser " + userDTO);
//        return userRepository.save(dtoToUser(userDTO));
//    }
}
