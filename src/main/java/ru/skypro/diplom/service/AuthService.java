package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.RegisterReq;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.enums.Role;
import ru.skypro.diplom.repository.UserRepository;

import java.util.Optional;

@Service
public class AuthService {

    private final UserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;


    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AuthService(UserDetailsManager userDetailsManager, UserRepository userRepository) {
        this.userDetailsManager = userDetailsManager;
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    /**
     * Логин-метод
     *
     * @param userName {@link String}
     * @return - true если удачно, false если нет
     */
    public boolean login(String userName, String password) {
        log.info("login " + userName);
        log.info("AuthService login userExists " + userDetailsManager.userExists(userName));
        if (!userDetailsManager.userExists(userName)) {
            return false;
        }
        UserDetails userDetails = userDetailsManager.loadUserByUsername(userName);
//        log.info("AuthService login userDetails getPassword " + userDetails.getPassword());
//        webSecurityConfig.setCurrentUserName(userName);
        String encryptedPassword = userDetails.getPassword();
        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
        boolean match = passwordEncoder.matches(password, encryptedPasswordWithoutEncryptionType);
        log.info("AuthService login password " + match);
        return match;
    }

    /**
     * регистрация нового пользователя
     *
     * @param registerReq новый пользователь {@link RegisterReq}
     * @return true
     */
    public boolean register(RegisterReq registerReq) {
        log.info("register " + registerReq.getUsername() );
        userDetailsManager.deleteUser(registerReq.getUsername());
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

    /**
     * Сохраняет нового пользователя в БД
     *
     * @param registerReq новый пользователь {@link RegisterReq}
     */
    public void addUser (RegisterReq registerReq){
        log.info("AddUser " + registerReq.getUsername());
        Optional<ru.skypro.diplom.model.User> optionalUser = userRepository.findFirstByUsername(registerReq.getUsername());
        if (optionalUser.isEmpty()){
            ru.skypro.diplom.model.User user = new ru.skypro.diplom.model.User(registerReq.getFirstName(), registerReq.getLastName(), registerReq.getUsername(), registerReq.getPhone());
            userRepository.saveAndFlush(user);
        }
    }

    /**
     * Изменяет пароль пользователя
     *
     * @param oldPassword текущий пароль {@link String}
     * @param newPassword новый пароль {@link String}
     */
    public void changePassword(String oldPassword, String newPassword, Authentication authentication) {
        log.info("changePassword " );
        isAuthorized(authentication);
        userDetailsManager.changePassword(oldPassword, newPassword);
    }

    /**
     * Проверка авторизации текущего пользователя
     *
     * @param user текущий пользователь {@link ru.skypro.diplom.model.User}
     * @return true если авторизован {@link boolean}
     */
    public boolean isAuthorized(ru.skypro.diplom.model.User user, Authentication authentication){
        log.info("isAuthorized " + user.getUsername() + " " + authentication.getName());
        if (!userDetailsManager.userExists(user.getUsername()))
            throw new UserNotFoundException();

        String role = authentication.getAuthorities().stream().findFirst()
                .map(authority -> authority.getAuthority())
                .orElse(null);
        if (role == null || role.isEmpty() || role.length() < 9){
            log.info("isAuthorized NotAuthorizedUserActionException ");
            throw new NotAuthorizedUserActionException();
        }
        role = role.substring(5);
//            log.info("isAuthorized role " + role);
        Role roleEnum = Role.USER;
        try{
            roleEnum = Role.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("could not parse role from string " + role );
        }
//            log.info("isAuthorized roleEnum " + roleEnum);
        if (roleEnum == Role.ADMIN)
            return true;

        if (user.getUsername().equals(authentication.getName()))
            return true;

        log.info("isAuthorized NotAuthorizedUserActionException ");
        throw new NotAuthorizedUserActionException();
    }

    /**
     * Проверка авторизации текущего пользователя
     *
     * @return true если авторизован {@link boolean}
     */
    public boolean isAuthorized(Authentication authentication){
        log.info("isAuthorized " + authentication.getName());
        if (!userDetailsManager.userExists(authentication.getName()))
            throw new UserNotFoundException();

        String role = authentication.getAuthorities().stream().findFirst()
                .map(authority -> authority.getAuthority())
                .orElse(null);

        if (role == null || role.isEmpty() || role.length() < 9){
            log.error("isAuthorized NotAuthorizedUserActionException " );
            throw new NotAuthorizedUserActionException();
        }

        role = role.substring(5);
//            log.info("isAuthorized role " + role);
        Role roleEnum = Role.USER;
        try{
            roleEnum = Role.valueOf(role);
        } catch (IllegalArgumentException | NullPointerException e) {
            log.warn("could not parse role from string " + role );
            log.error("isAuthorized NotAuthorizedUserActionException " );
            throw new NotAuthorizedUserActionException();
        }
//            log.info("isAuthorized roleEnum " + roleEnum);
        return true;
    }


}
