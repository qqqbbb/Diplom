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
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdService(AdRepository adRepository, UserRepository userRepository, UserService userService) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

//    public AdFull getFullAd(Ad ad) {
//        log.info("getFullAd");
//        User user = userService.getUserByName();
//        String image = "Ads/" + ad.getId() + "/image";
//        return new AdFull(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescriptione(), user.getUsername(), user.getPhone(), ad.getPrice(), image);
//    }

    public AdPreview adToDTO(Ad ad) {
        String image = "/ads/" + ad.getId() + "/image";
        return new AdPreview(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice(), image);
    }

    public AdFull adToFullDTO(Ad ad) {
        log.info("adToFullDTO");
        User user = ad.getUser();
        String image = "/ads/" + ad.getId() + "/image";
        AdFull adFull = new AdFull(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescriptione(), user.getUsername(), user.getPhone(), ad.getPrice(), image);
        return adFull;
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

    public AdPreview addAd(CreateAd createAd, MultipartFile file, String userName) {
        log.info("addAd");
        User user = userService.getUserByName(userName);
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user);
        setImage(ad, file);
        adRepository.save(ad);
        return adToDTO(ad);
    }

    public void setImage(Ad ad, MultipartFile file){
        log.info("setImage");
        try {
            byte[] bytes = file.getBytes();
            ad.setImage(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public AdPreview updateAd(CreateAd createAd, int id, String userName) {
        log.info("updateAd");
        User user = userService.getUserByName(userName);
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user);
        return adToDTO(ad);
    }

    public AdFull getAd(int id) {
        log.info("getAd " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return adToFullDTO(ad);
    }

    public void deleteAd(int id, String userName) {
        log.info("deleteAd " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        if (!ad.getUser().getUsername().equals(userName))
            throw new NotAuthorizedUserAction();

        adRepository.deleteById(id);
    }

    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds(String userName) {
        log.info("getCurrentUserAds ");
        User user = userService.getUserByName(userName);
        List<Ad> ads = adRepository.findAllByUser(user);
        List<AdPreview> adPreviews = new ArrayList<>();
        for (Ad ad: ads) {
            AdPreview adPreview = adToDTO(ad);
            adPreviews.add(adPreview);
        }
        return ResponseEntity.ok(new ResponseWrapperAds(ads.size(), adPreviews));
    }

    public ResponseEntity<?> updateAdImage(int id, MultipartFile file, String userName) {
        log.info("updateAdImage ");
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        if (!ad.getUser().getUsername().equals(userName))
            throw new NotAuthorizedUserAction();

        setImage(ad, file);
        adRepository.saveAndFlush(ad);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity<byte[]> getImage(int id) {
        log.info("getImage " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return ResponseEntity.ok(ad.getImage());
    }

}
