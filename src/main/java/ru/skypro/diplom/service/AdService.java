package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserRepository userRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdService(AdRepository adRepository, UserRepository userRepository) {
        this.adRepository = adRepository;
        this.userRepository = userRepository;
    }

//    public Ad dtoToAd(FullAds fullAds) {
//        Optional<User> optionalUser = userRepository.findById(fullAds.get());
//        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
//        return new Ad(adShort.getTitle(), adShort. adDTO.getPrice(), user);
//    }

    public Ads adToDTO(Ad ad) {
        return new Ads(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice());
    }

    public ResponseWrapperAds getAllAds() {
        List<Ad> ads = adRepository.findAll();
        return new ResponseWrapperAds(ads.size(), ads);
    }

//    public AdDTO addAd(CreateAd createAd, MultipartFile file) throws IOException {
//
//
//    }

    public Ads getAd(int id) {
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return adToDTO(ad);
    }
}
