package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;

import java.io.IOException;
import java.util.*;


@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ImageService imageService;
    private final ImageRepository imageRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdService(AdRepository adRepository, UserRepository userRepository, UserService userService, ImageService imageService, ImageRepository imageRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.imageService = imageService;
        this.imageRepository = imageRepository;
    }

    public AdFull getFullAd(Ad ad) {
        log.info("getFullAd");
        User user = userService.getCurrentUser();
        String image = "Ads/" + ad.getId() + "/image";
        return new AdFull(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescriptione(), user.getEmail(), user.getPhone(), ad.getPrice(), image);
    }

    public AdPreview adToDTO(Ad ad) {
        return new AdPreview(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice(), "");
    }

    public ResponseWrapperAds getAllAds() {
        log.info("getAllAds");
        List<Ad> ads = adRepository.findAll();
        List<AdPreview> adPreviews = new ArrayList<>();
        for (Ad ad: ads) {
            AdPreview adPreview = adToDTO(ad);
            adPreviews.add(adPreview);
        }
        return new ResponseWrapperAds(adPreviews.size(), adPreviews);
    }

    public ResponseEntity addImage() {
        log.info("addImage");
        User user = userService.getCurrentUser();
        Avatar avatar = user.getAvatar();
        Image image = new Image(avatar.getFilePath(), avatar.getFileSize(), avatar.getMediaType(), avatar.getData());
        imageRepository.saveAndFlush(image);
        return ResponseEntity.ok().build();
    }

    public AdFull addAd(CreateAd createAd, MultipartFile file) throws IOException {
        log.info("addAd");
        User user = userService.getCurrentUser();
        Avatar avatar = user.getAvatar();
        Image image = new Image(avatar.getFilePath(), avatar.getFileSize(), avatar.getMediaType(), avatar.getData());
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user, image);
        adRepository.save(ad);
//        imageService.uploadImage(ad, file);
        return getFullAd(ad);
    }

    public AdFull addAd(CreateAd createAd){
        log.info("addAd");
        User user = userService.getCurrentUser();
        Avatar avatar = user.getAvatar();
//        Image image = new Image(avatar.getFilePath(), avatar.getFileSize(), avatar.getMediaType(), avatar.getData());
        Optional<Image> imageOptioanl = imageRepository.findById(1);
        Image image = imageOptioanl.orElseThrow(() -> new ImageNotFoundException());
//        imageRepository.saveAndFlush(image);
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user, image);
        adRepository.save(ad);
        return getFullAd(ad);
    }

    public AdFull updateAd(CreateAd createAd, int id) {
        log.info("updateAd");
        User user = userService.getCurrentUser();
        Avatar avatar = user.getAvatar();
        Image image = new Image(avatar.getFilePath(), avatar.getFileSize(), avatar.getMediaType(), avatar.getData());
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user, image);
        return getFullAd(ad);
    }

    public AdPreview getAd(int id) {
        log.info("getAd " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return adToDTO(ad);
    }

    public void deleteAd(int id) {
        log.info("deleteAd " + id);
        adRepository.deleteById(id);
    }

    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds() {
        log.info("getCurrentUserAds ");
        User user = userService.getCurrentUser();
        List<Ad> ads = adRepository.findAllByUser(user);
        List<AdPreview> adPreviews = new ArrayList<>();
        for (Ad ad: ads) {
            AdPreview adPreview = adToDTO(ad);
            adPreviews.add(adPreview);
        }
        return ResponseEntity.ok(new ResponseWrapperAds(ads.size(), adPreviews));
    }

    public ResponseEntity<?> updateAdImage(int id, MultipartFile file) throws IOException {
        log.info("updateAdImage ");
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
//        imageService.uploadImage(ad, file);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<byte[]> getImage(int id) {
        log.info("getImage " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return imageService.downloadImage(ad);
    }

}
