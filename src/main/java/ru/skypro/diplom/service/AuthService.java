package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.RegisterReq;
import ru.skypro.diplom.Exceptions.UserNotFoundException;
import ru.skypro.diplom.Exceptions.currentUserDetailsNotFound;
import ru.skypro.diplom.WebSecurityConfig;
import ru.skypro.diplom.enums.Role;

@Service
public class AuthService {

    private final UserDetailsManager manager;
    private final PasswordEncoder encoder;
    private final UserService userService;
//    private final WebSecurityConfig webSecurityConfig;

    private ru.skypro.diplom.model.User currentUser;
    private UserDetails currentUserDetails;

//    private final InMemoryUserDetailsManager inMemoryUserDetailsManager;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AuthService(UserDetailsManager manager, UserService userService) {
        this.manager = manager;
        this.userService = userService;
        this.encoder = new BCryptPasswordEncoder();
//        this.inMemoryUserDetailsManager = inMemoryUserDetailsManager;
    }

//    public boolean login(String userName, String password) {
//        log.info("login " + userName);
//        System.out.println("AuthService login userExists " + manager.userExists(userName));
////        if (!manager.userExists(userName)) {
////            return false;
////        }
//        UserDetails userDetails = manager.loadUserByUsername(userName);
//        currentUserDetails = userDetails;
//        webSecurityConfig.setCurrentUserName(userName);
//        String encryptedPassword = userDetails.getPassword();
//        String encryptedPasswordWithoutEncryptionType = encryptedPassword.substring(8);
//        System.out.println("AuthService login password " + encoder.matches(password, encryptedPasswordWithoutEncryptionType));
//        return encoder.matches(password, encryptedPasswordWithoutEncryptionType);
//    }
//
//    public boolean register(RegisterReq registerReq, Role role) {
//        log.info("register " + registerReq.getUsername() + " " + " " + role);
//        if (manager.userExists(registerReq.getUsername())) {
//            return false;
//        }
//        manager.createUser(
//                User.withDefaultPasswordEncoder()
//                        .password(registerReq.getPassword())
//                        .username(registerReq.getUsername())
//                        .roles(role.name())
//                        .build()
//        );
//        userService.addUser(registerReq);
//        return true;
//    }

    public void changePassword(String oldPassword, String newPassword) {
        log.info("changePassword " );
//        if (!manager.userExists(name))
//            throw new UserNotFoundException();

        manager.changePassword(oldPassword, newPassword);
    }

    public ru.skypro.diplom.model.User getCurrentUser() {
        log.info("getCurrentUser " );
//        if (!manager.userExists(name))
//            throw new UserNotFoundException();
//        System.out.println("authentication.getName() " + authentication.getName());
//        System.out.println("authentication.getName() " + authentication.getDetails().);
        return currentUser;
    }

    public UserDetails getCurrentUseDetailsr() {
        log.info("getCurrentUseDetailsr " );
        if (currentUserDetails == null)
            throw new currentUserDetailsNotFound();

        return currentUserDetails;
    }

    public void setCurrentUser(ru.skypro.diplom.model.User user) {
        log.info("setCurrentUser " + user);
        currentUser = user;
    }


}
