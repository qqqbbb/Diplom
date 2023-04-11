package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.RegisterReq;
import ru.skypro.diplom.DTO.UserDTO;
import ru.skypro.diplom.Exceptions.NotAuthorizedUserAction;
import ru.skypro.diplom.Exceptions.UserAlreadyRegisteredException;
import ru.skypro.diplom.Exceptions.UserNotFoundException;
import ru.skypro.diplom.Exceptions.currentUserDetailsNotFound;
import ru.skypro.diplom.enums.Role;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.repository.UserRepository;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.*;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private UserDetails currentUserDetails;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserService(UserRepository userRepository, UserDetailsManager userDetailsManager) {
        this.userRepository = userRepository;
        this.userDetailsManager = userDetailsManager;
        this.passwordEncoder = new BCryptPasswordEncoder();;
    }

    public boolean login(String userName, String password) {
        log.info("login " + userName);
        log.info("UserService login userExists " + userDetailsManager.userExists(userName));
//        if (!manager.userExists(userName)) {
//            return false;
//        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);
        currentUserDetails = userDetails;
//        log.info("UserService login userDetails getPassword " + userDetails.getPassword());
//        webSecurityConfig.setCurrentUserName(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        log.info("UserService login password " + passwordEncoder.matches(password, encryptedPasswordWithoutEncryptionType));
        return passwordEncoder.matches(password, encryptedPasswordWithoutEncryptionType);
    }

    public boolean register(RegisterReq registerReq) {
        log.info("register " + registerReq.getUsername() );
        userDetailsManager.deleteUser(registerReq.getUsername());
//        if (userDetailsManager.userExists(registerReq.getUsername())) {
//            log.info("register. User " + registerReq.getUsername() + " already resistered" );
//            return false;
//        }
        Role role = Role.USER;
        try{
            role = Role.valueOf(registerReq.getRole());
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("could not parse role from string " + registerReq.getRole() );
        }
        userDetailsManager.createUser(
                org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                        .password(registerReq.getPassword())
                        .username(registerReq.getUsername())
                        .roles(role.name())
                        .build()
        );
        addUser(registerReq);
        return true;
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

    public UserDTO updateUser (UserDTO userDTO, String userName){
        log.info("updateUser " + userDTO);
        if (!userName.equals(userDTO.getEmail()))
            throw new NotAuthorizedUserAction();

        User user = userRepository.save(dtoToUser(userDTO));
//        Optional<User> optionalUser = userRepository.findById(userDTO.getId());
//        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());

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

    public void setAvatar(MultipartFile file, String name) {
        log.info("setAvatar");
        Optional<User> optionalUser = userRepository.findFirstByUsername(name);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        try {
            byte[] bytes = file.getBytes();
            log.info("setAvatar bytes " + bytes.length);
            user.setAvatar(bytes);
            userRepository.save(user);
        } catch (IOException e) {
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
