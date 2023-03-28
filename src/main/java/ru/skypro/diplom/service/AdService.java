package ru.skypro.diplom.service;

import org.slf4j.*;
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
        User user = userService.getCurrentUser();
        // image???
        FullAds fullAds = new FullAds(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescriptione(), user.getEmail(), user.getPhone(), ad.getPrice(), ad.getImage().getFilePath());
        return fullAds;
    }

    public Ads adToDTO(Ad ad) {
        return new Ads(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice());
    }

    public ResponseWrapperAds getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return new ResponseWrapperAds(ads.size(), ads);
    }

    public FullAds addAd(CreateAds createAd, MultipartFile file) throws IOException {

        User user = userService.getCurrentUser();
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user, user.getPhone(), user.getEmail());
        imageService.uploadImage(ad, file);
        return getFullAd(ad);
    }

    public Ads getAd(int id) {
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return adToDTO(ad);
    }
}
