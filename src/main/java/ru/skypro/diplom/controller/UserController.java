package ru.skypro.diplom.controller;

import org.springframework.http.*;
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
    public UserController(UserService userService, AvatarService avatarService) {
        this.userService = userService;
        this.avatarService = avatarService;
    }

    @PostMapping("/set_password") // rewrite with auth
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword ) {
        userService.changePassword(newPassword);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me") // rewrite with auth
    public ResponseEntity<?> getUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PatchMapping ("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO ) {
        UserDTO updatedDTO = userService.updateUser(userDTO);
        return ResponseEntity.ok(updatedDTO);
    }

    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserAvatar(@RequestPart(value = "image") MultipartFile file) throws IOException {

        avatarService.uploadAvatar(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping ("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO ) {

        return ResponseEntity.ok(userService.addUser(userDTO));
    }

//    @GetMapping ("/getAvatar")
//    public ResponseEntity<byte[]> getAvatar() {
//
//        return avatarService.downloadAvatar(userService.getCurrentUser());
//    }

}
