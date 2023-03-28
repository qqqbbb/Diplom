package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.Exceptions.AvatarNotFoundException;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.AvatarRepository;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.*;
import java.util.Optional;

import static java.nio.file.StandardOpenOption.CREATE_NEW;

@Service
public class AvatarService {

    private final UserService userService;
    private final AvatarRepository avatarRepository;
    @Value("${path.to.avatars.folder}")
    private String avatarsDir;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AvatarService(UserService userService, AvatarRepository avatarRepository) {
        this.userService = userService;
        this.avatarRepository = avatarRepository;
    }

    public void uploadAvatar(MultipartFile file) throws IOException
    {
        log.info("uploadAvatar " );
        User user = userService.getCurrentUser();
        Path filePath = Path.of(avatarsDir, user + "." + getFileNameExtension(file.getOriginalFilename()));
        Files.createDirectories(filePath.getParent());
        Files.deleteIfExists(filePath);
        try (InputStream is = file.getInputStream();
             OutputStream os = Files.newOutputStream(filePath, CREATE_NEW);
             BufferedInputStream bis = new BufferedInputStream(is, 1024);
             BufferedOutputStream bos = new BufferedOutputStream(os, 1024))
        {
            bis.transferTo(bos);
        }
        Optional<Avatar> optional = avatarRepository.findById(user.getAvatar().getId());
        Avatar avatar = optional.orElse(new Avatar(filePath.toString(), file.getSize(), file.getContentType(), file.getBytes()));
        avatarRepository.save(avatar);
    }

    public Avatar getAvatar(User user)
    {
        log.info("getAvatar " );
        Optional<Avatar> optionalAvatar = avatarRepository.findById(user.getAvatar().getId());
        return optionalAvatar.orElseThrow(()-> new AvatarNotFoundException());
    }

    public ResponseEntity<byte[]> downloadAvatarFromDB(User user)
    {
        log.info("downloadAvatarFromDB " + user.getId());
        Avatar avatar = getAvatar(user);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(avatar.getMediaType()));
        headers.setContentLength(avatar.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(avatar.getData());
    }

    public void downloadAvatarFromFile(User user, HttpServletResponse response) throws IOException
    {
        log.info("downloadAvatarFromFile " + user.getId());
        Avatar avatar = getAvatar(user);
        Path path = Path.of(avatar.getFilePath());
        try(InputStream is = Files.newInputStream(path);
            OutputStream os = response.getOutputStream())
        {
            response.setStatus(200);
            response.setContentType(avatar.getMediaType());
            response.setContentLength((int) avatar.getFileSize());
            is.transferTo(os);
        }
    }

    private String getFileNameExtension(String fileName)
    {
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }


}
