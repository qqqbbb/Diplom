package ru.skypro.diplom.controller;

import org.springframework.http.ResponseEntity;
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

    @PatchMapping ("/me/image")
    public ResponseEntity<?> updateUserAvatar(@RequestPart(value = "image") MultipartFile file) throws IOException {
        if (file.getSize() > 1024 * 1024)
            return ResponseEntity.badRequest().body("FIle is too big");

        String contentType = file.getContentType();
        System.out.println("upload avatar contentType " + contentType);
        if (contentType == null || !contentType.contains("image"))
            return ResponseEntity.badRequest().body("Only images can be uploaded");

        avatarService.uploadAvatar(file);
        return ResponseEntity.ok().build();
    }

    @PostMapping ("/addUser")
    public ResponseEntity<?> addUser(@RequestBody UserDTO userDTO ) {

        return ResponseEntity.ok(userService.addUser(userDTO));
    }

}
