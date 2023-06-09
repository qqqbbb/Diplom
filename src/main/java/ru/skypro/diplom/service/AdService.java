package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.*;


@Service
public class AdService {

    private final AdRepository adRepository;
    private final UserService userService;
    private final AuthService authService;
    private final CommentRepository commentRepository;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdService(AdRepository adRepository, UserService userService, AuthService authService, CommentRepository commentRepository) {
        this.adRepository = adRepository;
        this.userService = userService;
        this.authService = authService;
        this.commentRepository = commentRepository;
    }

    /**
     * Преобразует Ad в AdPreview
     *
     * @param ad объявление {@link Ad}
     * @return DTO {@link AdPreview}
     */
    public AdPreview adToDTO(Ad ad) {
        String image = "/ads/" + ad.getId() + "/image";
        return new AdPreview(ad.getId(), ad.getTitle(), ad.getUser().getId(), ad.getPrice(), image);
    }

    /**
     * Преобразует Ad в AdFull
     *
     * @param ad объявление {@link Ad}
     * @return DTO {@link AdPreview}
     */
    public AdFull adToFullDTO(Ad ad) {
        log.info("adToFullDTO");
        User user = ad.getUser();
        String image = "/ads/" + ad.getId() + "/image";
        AdFull adFull = new AdFull(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.getDescription(), user.getUsername(), user.getPhone(), ad.getPrice(), image);
        return adFull;
    }

    /**
     * Возвращает все объявления
     *
     * @return  объявления {@link ResponseWrapperAds}
     */
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

    /**
     * Создает и сохраняет новое объявление
     *
     * @param createAd  DTO объявление {@link CreateAd}
     * @param file - картинка
     * @return  DTO объявление {@link AdFull}
     */
    public ResponseEntity addAd(CreateAd createAd, MultipartFile file, Authentication authentication) {
        log.info("addAd");
        boolean isAuthorized = authService.isAuthorized(authentication);
        log.info("addAd isAuthorized " + isAuthorized);
        if (file.getSize() > 1024 * 1024)
            return ResponseEntity.badRequest().body("File is too big");

        String contentType = file.getContentType();
//        System.out.println("addAd image contentType " + contentType);
        if (contentType == null || !contentType.contains("image"))
            return ResponseEntity.badRequest().body("Only images can be uploaded");

        User user = userService.getUserByName(authentication.getName());
        Ad ad = new Ad(createAd.getTitle(), createAd.getDescription(), createAd.getPrice(), user);
        setImage(ad, file);
        adRepository.save(ad);
        return ResponseEntity.status(HttpStatus.CREATED).body(adToDTO(ad));
    }

    /**
     * Сохраняет новую картинку для объявления
     *
     * @param ad - объявление {@link Ad}
     * @param file - картинка
     */
    public void setImage(Ad ad, MultipartFile file){
        log.info("setImage");
        try {
            ad.setImage(file.getBytes());
        } catch (IOException e) {
            log.error("setImage could not read file");
            throw new RuntimeException(e);
        }
    }

    /**
     * Обновляет объявление
     *
     * @param createAd - DTO  {@link CreateAd}
     * @param id - первичный ключ объявления
     * @return  DTO {@link AdPreview}
     */
    public AdPreview updateAd(CreateAd createAd, int id, Authentication authentication) {
        log.info("updateAd");
        Ad ad = adRepository.findById(id).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        log.info("updateAd isAuthorized " + authService.isAuthorized(user, authentication));
        ad.setTitle(createAd.getTitle());
        ad.setDescription(createAd.getDescription());
        ad.setPrice(createAd.getPrice());
        adRepository.save(ad);
        return adToDTO(ad);
    }

    /**
     * Возвращает DTO объявления
     *
     * @param id - первичный ключ объявления
     * @return  DTO {@link AdFull}
     */
    public AdFull getAd(int id) {
        log.info("getAd " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return adToFullDTO(ad);
    }

    /**
     * Удаляет объявление
     *
     * @param id - первичный ключ объявления
     */
    @Transactional
    public void deleteAd(int id, Authentication authentication) {
        log.info("deleteAd " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        boolean isAuthorized = authService.isAuthorized(ad.getUser(), authentication);
        log.info("deleteAd isAuthorized " + isAuthorized);
        commentRepository.deleteAllByAd(ad);
        adRepository.deleteById(id);
    }

    /**
     * Возвращает все объявления текущего пользователя
     *
     * @return  DTO объявления {@link ResponseWrapperAds}
     */
    public ResponseWrapperAds getCurrentUserAds(Authentication authentication) {
        log.info("getCurrentUserAds ");
        authService.isAuthorized(authentication);
        User user = userService.getUserByName(authentication.getName());
        List<Ad> ads = adRepository.findAllByUser(user);
        List<AdPreview> adPreviews = new ArrayList<>();
        for (Ad ad: ads) {
            adPreviews.add(adToDTO(ad));
        }
        return new ResponseWrapperAds(ads.size(), adPreviews);
    }

    /**
     * Обновляет картинку объявления
     *
     * @param adId - первичный ключ объявления
     * @param file - картинка {@link MultipartFile}
     * @return  Обновленная картинка в двоичном формате
     */
    public byte[] updateAdImage(int adId, MultipartFile file, Authentication authentication) {
        log.info("updateAdImage ");
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        authService.isAuthorized(user, authentication);
        setImage(ad, file);
        adRepository.saveAndFlush(ad);
        return ad.getImage();
    }

    /**
     * Возвращает картинку объявления
     *
     * @param id - первичный ключ объявления
     * @return  картинка в двоичном формате
     */
    public ResponseEntity<byte[]> getImage(int id) {
        log.info("getImage " + id);
        Optional<Ad> optionalAd = adRepository.findById(id);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        return ResponseEntity.ok(ad.getImage());
    }



}
