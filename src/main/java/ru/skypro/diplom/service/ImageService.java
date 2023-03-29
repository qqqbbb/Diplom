package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.Exceptions.AvatarNotFoundException;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.ImageRepository;

import java.io.*;


@Service
public class ImageService {

    private final UserService userService;
    private final ImageRepository imageRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public ImageService(UserService userService, ImageRepository imageRepository) {
        this.userService = userService;
        this.imageRepository = imageRepository;
    }

    public void uploadImage(Ad ad, MultipartFile file) throws IOException {
        log.info("upload Image " );
        Image image = new Image(ad.getTitle(), file.getSize(), file.getContentType(), file.getBytes());
        ad.setImage(image);
        imageRepository.save(image);
    }

    public Image getImage(Ad ad) {
        log.info("getImage " );
        Image image = ad.getImage();
        if (image == null)
            throw new AvatarNotFoundException();

        return image;
    }

    public ResponseEntity<byte[]> downloadImage(Ad ad) {
        log.info("downloadImageFromDB " + ad.getTitle());
        Image image = ad.getImage();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(image.getMediaType()));
        headers.setContentLength(image.getData().length);
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(image.getData());
    }


}
