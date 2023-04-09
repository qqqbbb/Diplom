package ru.skypro.diplom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.service.*;

import java.io.IOException;

@RestController
@RequestMapping("users")
@CrossOrigin(value = "http://localhost:3000")
public class UserController {

    private final UserService userService;
    private final AvatarService avatarService;
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    public UserController(UserService userService, AvatarService avatarService, AuthService authService) {
        this.userService = userService;
        this.avatarService = avatarService;
        this.authService = authService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
//        NewPassword resultPassword = new NewPassword();
        log.info("set_password " );
        if (authentication == null)
            log.info("set_password authentication == null" );
        else
            log.info("set_password authentication getName " + authentication.getName() );

        log.info("set_password currentPassword  " + newPassword.currentPassword );
        authService.changePassword(
                        newPassword.getCurrentPassword(),
                        newPassword.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {
//        authentication.getName()
        return ResponseEntity.ok(userService.getUserByName(authentication.getName()));
    }

    @PatchMapping ("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO ) {
        UserDTO updatedDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserAvatar(@RequestPart(value = "image") MultipartFile file, Authentication authentication) throws IOException {

        avatarService.uploadAvatar(file, authentication.getName());
        return ResponseEntity.ok().build();
    }

//    @PostMapping ("/addUser")
//    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO ) {
//
//        return ResponseEntity.ok(userService.addUser(userDTO));
//    }

//    @GetMapping ("/getAvatar")
//    public ResponseEntity<byte[]> getAvatar() {
//
//        return avatarService.downloadAvatar(userService.getCurrentUser());
//    }

}
