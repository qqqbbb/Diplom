package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.Exceptions.AvatarNotFoundException;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.AvatarRepository;
import ru.skypro.diplom.repository.ImageRepository;

import java.io.*;


@Service
public class AvatarService {

    private final UserService userService;
    private final AvatarRepository avatarRepository;
    private final ImageRepository imageRepository;
    private final ImageService imageService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AvatarService(UserService userService, AvatarRepository avatarRepository, ImageRepository imageRepository, ImageService imageService) {
        this.userService = userService;
        this.avatarRepository = avatarRepository;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    public ResponseEntity uploadAvatar(MultipartFile file, String userName) throws IOException {
        log.info("uploadAvatar " + file.getOriginalFilename());
        if (file.getSize() > 1024 * 1024)
            return ResponseEntity.badRequest().body("File is too big");

        String contentType = file.getContentType();
//        System.out.println("upload avatar contentType " + contentType);
        if (contentType == null || !contentType.contains("image"))
            return ResponseEntity.badRequest().body("Only images can be uploaded");

        User user = userService.getUserByName(userName);
        Avatar avatar = new Avatar(file.getOriginalFilename(), file.getSize(), file.getContentType(), file.getBytes());
        user.setAvatar(avatar);
        return ResponseEntity.ok(avatarRepository.save(avatar));
    }

    public Avatar getAvatar(User user) {
        log.info("getAvatar " );
        Avatar avatar = user.getAvatar();
        if (avatar == null)
            throw new AvatarNotFoundException();

        return avatar;
    }

    public ResponseEntity<byte[]> downloadAvatar(User user) {
        log.info("downloadAvatar " + user.getId());
        Avatar avatar = getAvatar(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }


}
