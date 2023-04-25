package ru.skypro.diplom.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.model.User;
import ru.skypro.diplom.service.*;

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

    /**
     * Метод для смены пароля текущего пользователя
     *
     * @param newPassword дто-объект, содержащий старый и новый пароли {@link NewPassword}
     * @return код 200 - при удачном изменении пароля, код 403 - при введении неверного текущего пароля
     */
    @PostMapping("/set_password")
    public ResponseEntity<?> setPassword(@RequestBody NewPassword newPassword, Authentication authentication) {
        log.info("set_password " );
        log.info("set_password authentication getName " + authentication.getName() );
        log.info("set_password currentPassword  " + newPassword.currentPassword );
        log.info("set_password newPassword " + newPassword.newPassword );
        authService.changePassword(
                        newPassword.getCurrentPassword(),
                        newPassword.getNewPassword(), authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод возвращает данные о пользователе
     *
     * @return данные о пользователе в виде дто-объекта {@link UserDTO}
     */
    @GetMapping("/me")
    public ResponseEntity<UserDTO> getUser(Authentication authentication) {
        log.info("getUser");
        User user = userService.getUserByName(authentication.getName());
        return ResponseEntity.ok(userService.userToDTO(user));
    }

    /**
     * Метод обновляет данные пользователя
     *
     * @param userDTO     дто-объект, содержащий данные для обновления пользователя {@link UserDTO}
     * @return данные о пользователе в виде дто-объекта {@link UserDTO}
     */
    @PatchMapping ("/me")
    public ResponseEntity<UserDTO> updateUser(@RequestBody UserDTO userDTO, Authentication authentication) {
        log.info("updateUser");
        log.info("updateUser userDTO userName " + userDTO.getEmail() + " " + userDTO.getFirstName());
        log.info("updateUser authentication.getName " + authentication.getName());
        UserDTO updatedDTO = userService.updateUser(userDTO, authentication);
        return ResponseEntity.ok(updatedDTO);
    }

    /**
     * Метод обновляет аватар пользователя
     *
     * @param file       аватар пользователя в виде {@link MultipartFile}
     * @return код 200 - при удачном добавлении пользователя
     */
    @PatchMapping(value = "/me/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity updateUserAvatar(@RequestPart(value = "image") MultipartFile file, Authentication authentication) {
        log.info("updateUserAvatar");
        if (file.getSize() > 1024 * 1024)
            return ResponseEntity.badRequest().body("File is too big");

        String contentType = file.getContentType();
//        System.out.println("upload avatar contentType " + contentType);
        if (contentType == null || !contentType.contains("image"))
            return ResponseEntity.badRequest().body("Only images can be uploaded");

        userService.setAvatar(file, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод возвращает аватар текущего пользователя
     *
     * @param id первичный ключ пользователя
     * @return бинарные данные аватара пользователя
     */
    @GetMapping(value ="/me/avatar", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAvatar(Authentication authentication) {
        log.info("getAvatar " );
        return ResponseEntity.ok(userService.getAvatar(authentication.getName()));
    }

    /**
     * Метод возвращает аватар пользователя
     *
     * @param id первичный ключ пользователя
     * @return бинарные данные аватара пользователя
     */
    @GetMapping(value ="/{id}/avatar", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAvatarById(@PathVariable int id) {
        log.info("getAvatarById " + id);
        return ResponseEntity.ok(userService.getAvatar(id));
    }


}
