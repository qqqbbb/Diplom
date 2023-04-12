package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.enums.Role;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.*;
import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserService(UserRepository userRepository, AuthService authServicer) {
        this.userRepository = userRepository;
        this.authService = authServicer;
        this.passwordEncoder = new BCryptPasswordEncoder();;
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
        return new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone());
    }

    public UserDTO userToDTO(User user){
        String avatar = "/users/me/avatar";
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhone(), avatar);
    }

    public UserDTO updateUser (UserDTO userDTO, Authentication authentication){
        // userDTO.getEmail is null
//        isAuthorized(userDTO.getEmail(), authentication);
        String userName = authentication.getName();
        log.info("updateUser " + userName);
        log.info("updateUser equals " + userName.equals(userDTO.getEmail()));
//        if (!userName.equals(userDTO.getEmail()))
//            throw new NotAuthorizedUserActionException();
        Optional<User> optionalUser = userRepository.findFirstByUsername(userName);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPhone(userDTO.getPhone());
        user = userRepository.save(user);
        return userToDTO(user);
    }

    public void addUser (RegisterReq registerReq){
        log.info("AddUser " + registerReq.getUsername());
        Optional<User> optionalUser = userRepository.findFirstByUsername(registerReq.getUsername());
//        if (optionalUser.isPresent())
//            throw new UserAlreadyRegisteredException();
        if (optionalUser.isEmpty()){
            User user = new User(registerReq.getFirstName(), registerReq.getLastName(), registerReq.getUsername(), registerReq.getPhone());
            userRepository.saveAndFlush(user);
        }
    }

    public User getUserByName (String name){
        log.info(" getUserByName " + name);
//        Optional<User> optionalUser = userRepository.findById(1);
        Optional<User> optionalUser = userRepository.findFirstByUsername(name);
        return optionalUser.orElseThrow(() -> new UserNotFoundException());
    }

    public void setAvatar(MultipartFile file, Authentication authentication) {
        log.info("setAvatar");
        authService.isAuthorized(authentication);
        Optional<User> optionalUser = userRepository.findFirstByUsername(authentication.getName());
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        try {
            byte[] bytes = file.getBytes();
            log.info("setAvatar bytes " + bytes.length);
            user.setAvatar(bytes);
            userRepository.save(user);
        } catch (IOException e) {
            log.error("setAvatar could not get avatar file" );
            throw new RuntimeException(e);
        }
    }

    public byte[] getAvatar(String name) {
        log.info("getAvatar");
        Optional<User> optionalUser = userRepository.findFirstByUsername(name);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        return user.getAvatar();
    }




}
