package ru.skypro.diplom.controller;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.service.*;

@RestController
@RequestMapping("ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {
    private final AdService adService;
    private final CommentService commentService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public AdsController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    /**
     * Сохраняет объявление вместе с картинкой
     *
     * @param createAd   текстовые данные объявления
     * @param file       картинка объявления в виде {@link MultipartFile}
     * @return данные о добавленном объявлении
     */
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postAd(@RequestPart(value = "image") MultipartFile file, @RequestPart(value = "properties") CreateAd createAd, Authentication authentication) {
        log.info("addAd");
        return adService.addAd(createAd, file, authentication);
    }

    /**
     * Метод возвращает все объявления
     *
     * @return объявления всех пользователей в виде дто-объекта {@link ResponseWrapperAds}
     */
    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        log.info("getAllAds");
        ResponseWrapperAds rwa = adService.getAllAds();
        return ResponseEntity.ok(rwa);
    }

    /**
     * Полные данные об объявлении
     *
     * @param id первичный ключ объявления
     * @return данные об объявлении в виде дто-объекта {@link AdFull}
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdFull> getAd(@PathVariable("id")  int id, Authentication authentication) {
        log.info("getAd");
        return ResponseEntity.ok(adService.getAd(id));
    }

    /**
     * Удаляет объявление
     *
     * @param id первичный ключ объявления
     * @return код 204 - если объявление было удалено или не существовало
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable int id, Authentication authentication) {
        log.info("deleteAd");
        adService.deleteAd(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Обновляет объявление
     *
     * @param id        первичный ключ объявления
     * @param createAd текстовая информация обновления
     * @return обновленное объявление в виде дто-объекта {@link AdPreview}
     */
    @PatchMapping("/{id}")
    public ResponseEntity<AdPreview> updateAd(@PathVariable int id, @RequestBody CreateAd createAd, Authentication authentication) {
        log.info("updateAd");
        AdPreview adPreview = adService.updateAd(createAd, id, authentication);
        return ResponseEntity.ok(adPreview);
    }

    /**
     * Получает данные об объявлениях пользователя
     *
     * @return данные об объявлениях пользователя в виде дто-объекта {@link ResponseWrapperAds}
     */
    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds(Authentication authentication) {
        log.info("getCurrentUserAds");
        return ResponseEntity.ok(adService.getCurrentUserAds(authentication));
    }

    /**
     * Метод возвращает картинку объявления
     *
     * @param id первичный ключ объявления
     * @return бинарные данные картинки объявления
     */
    @GetMapping(value ="/{id}/image", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) {
        log.info("getAdImage " + id);
        return adService.getImage(id);
    }

    /**
     * Обновляет картинку объявления
     *
     * @param id    первичный ключ объявления
     * @param file новая картинка
     * @return добавленная картинка в виде {@link ResponseEntity}
     */
    @PatchMapping(value ="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file, Authentication authentication) {
        log.info("updateImage " + id);
        byte[] image = adService.updateAdImage(id, file, authentication);
        return ResponseEntity.ok(image);
    }

    /**
     * Метод для получения комментариев по ид объявления
     *
     * @param id идентификатор объявления
     * @return дто-объект содержащий все комментарии объявления {@link ResponseWrapperComment}
     */
    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable int id) {
        log.info("getComments " + id);
        return ResponseEntity.ok(commentService.getComments(id));
    }

    /**
     * Метод для создания комментария
     *
     * @param id        идентификатор объявления
     * @param commentDTO     дто-объект, содержащий данные для создания комментария
     * @return данные о созданном комментарии в виде дто-объекта {@link CommentDTO}
     */
    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int id, Authentication authentication) {
        log.info("addComment " + id);
        return ResponseEntity.ok(commentService.addComment(commentDTO, id, authentication));
    }

    /**
     * Метод для удаления комментария по его ид
     *
     * @param adId        идентификатор объявления
     * @param commentId          идентификатор комментария
     * @return код 200 - если объект успешно удален или не существовал
     */
    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId, Authentication authentication) {
        log.info("deleteComment " + commentId);
        commentService.deleteComment(adId, commentId, authentication);
        return ResponseEntity.ok().build();
    }

    /**
     * Метод для изменения комментария по его ид
     *
     * @param adId        идентификатор объявления
     * @param commentId          идентификатор комментария
     * @param commentDTO     дто-объект содержащий данные о обновленном комментарии
     * @return данные о обновленном комментарии в виде дто-объекта {@link CommentDTO}
     */
    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO, Authentication authentication) {
        log.info("updateComment " + commentId);
        CommentDTO commentDTO1 = commentService.updateComment(commentId, adId, commentDTO, authentication);
        return ResponseEntity.ok(commentDTO1);
    }

}
