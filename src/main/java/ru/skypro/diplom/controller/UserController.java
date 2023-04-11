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
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
        this.authService = authService;
    }

    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("set_password " );
        if (authentication == null)
            log.info("set_password authentication == null" );
        else
            log.info("set_password authentication getName " + authentication.getName() );

        log.info("set_password currentPassword  " + newPassword.currentPassword );
        log.info("set_password newPassword " + newPassword.newPassword );
        authService.changePassword(
                        newPassword.getCurrentPassword(),
                        newPassword.getNewPassword());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUser(Authentication authentication) {
        log.info("getUser");
        return ResponseEntity.ok(userService.getUserByName(authentication.getName()));
    }

    @PatchMapping ("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, Authentication authentication) {
        log.info("updateUser");
        if (userDTO == null)
            log.info("updateUser userDTO == null");
        else
            log.info("updateUser " + userDTO.getEmail() + " " + userDTO.getFirstName());

        log.info("updateUser authentication.getName " + authentication.getName());
        UserDTO updatedDTO = userService.updateUser(userDTO, authentication.getName());
        return ResponseEntity.ok(updatedDTO);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserAvatar(@RequestPart(value = "image") MultipartFile file, Authentication authentication) {
        log.info("updateUserAvatar");
        if (file.getSize() > 1024 * 1024)
            return ResponseEntity.badRequest().body("File is too big");

        String contentType = file.getContentType();
//        System.out.println("upload avatar contentType " + contentType);
        if (contentType == null || !contentType.contains("image"))
            return ResponseEntity.badRequest().body("Only images can be uploaded");

        userService.setAvatar(file, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping(value ="/me/avatar", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAvatar(Authentication authentication) {
        log.info("getAvatar " );
        return ResponseEntity.ok(userService.getAvatar(authentication.getName()));
    }

}
