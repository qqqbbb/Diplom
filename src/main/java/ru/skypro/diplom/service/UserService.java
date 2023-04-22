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

    /**
     * Преобразует UserDTO в User
     *
     * @param userDTO пользователь  {@link UserDTO}
     * @return пользователь {@link User}
     */
    public User dtoToUser(UserDTO userDTO){
        return new User(userDTO.getFirstName(), userDTO.getLastName(), userDTO.getEmail(), userDTO.getPhone());
    }

    /**
     * Преобразует User в UserDTO
     *
     * @param user пользователь {@link User}
     * @return пользователь {@link UserDTO}
     */
    public UserDTO userToDTO(User user){
        String avatar = "/users/me/avatar";
        return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPhone(), avatar);
    }

    /**
     * Обновляет данные о пользователе
     *
     * @param userDTO пользователь {@link UserDTO}
     * @return пользователь {@link UserDTO}
     */
    public UserDTO updateUser (UserDTO userDTO, Authentication authentication){
        String userName = authentication.getName();
        log.info("updateUser " + userName);
//        log.info("updateUser equals " + userName.equals(userDTO.getEmail()));
        User user = userRepository.findFirstByUsername(userName).orElseThrow(() -> new UserNotFoundException());
        authService.isAuthorized(user, authentication);
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

    /**
     * Возвращает пользователя по логину
     *
     * @param name логин пользователя {@link String}
     * @return пользователь {@link User}
     */
    public User getUserByName (String name){
        log.info(" getUserByName " + name);
//        Optional<User> optionalUser = userRepository.findById(1);
        Optional<User> optionalUser = userRepository.findFirstByUsername(name);
        return optionalUser.orElseThrow(() -> new UserNotFoundException());
    }

    /**
     * Сохраняет аватар текущего пользователя
     *
     * @param file аватар {@link MultipartFile}
     */
    public void setAvatar(MultipartFile file, Authentication authentication) {
        log.info("setAvatar");
        boolean isAuthorized = authService.isAuthorized(authentication);
        log.info("setAvatar isAuthorized " + isAuthorized);
        User user = userRepository.findFirstByUsername(authentication.getName()).orElseThrow(() -> new UserNotFoundException());
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

    /**
     * Возвращает аватар пользователя
     *
     * @param name логин пользователя {@link String}
     * @return аватар в двоичном формате
     */
    public byte[] getAvatar(String name) {
        log.info("getAvatar " + name);
        Optional<User> optionalUser = userRepository.findFirstByUsername(name);
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        return user.getAvatar();
    }

    /**
     * Возвращает аватар пользователя
     *
     * @param id первичный ключ пользователя {@link int}
     * @return аватар в двоичном формате
     */
    public byte[] getAvatar(int id) {
        log.info("getAvatar " + id);
        User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
        return user.getAvatar();
    }


}
