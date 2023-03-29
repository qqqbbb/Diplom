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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdService(AdRepository adRepository, UserRepository userRepository, UserService userService, ImageService imageService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.imageService = imageService;
    }

    public FullAds getFullAd(Ad ad) {
        log.info("getFullAd");
        User user = userService.getCurrentUser();
        // image???
        FullAds fullAds = new FullAds(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescriptione(), user.getEmail(), user.getPhone(), ad.getPrice(), ad.getImage().getFilePath());
        return fullAds;
    }

    public Ads adToDTO(Ad ad) {
        return new Ads(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice());
    }

    public ResponseWrapperAds getAllAds() {
        log.info("getAllAds");
        List<Ad> ads = adRepository.findAll();
        return new ResponseWrapperAds(ads.size(), ads);
    }

    public FullAds addAd(CreateAds createAd, MultipartFile file) throws IOException {
        log.info("addAd");
        User user = userService.getCurrentUser();
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user, user.getPhone(), user.getEmail());
        imageService.uploadImage(ad, file);
        return getFullAd(ad);
    }

    public Ads getAd(int id) {
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
        ResponseWrapperAds rwa = new ResponseWrapperAds(ads.size(), ads);
        return ResponseEntity.ok(rwa);
    }

    public ResponseEntity<?> updateAdImage(int id, MultipartFile file) throws IOException {
        log.info("updateAdImage ");
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        imageService.uploadImage(ad, file);
        return ResponseEntity.ok().build();
    }


}
