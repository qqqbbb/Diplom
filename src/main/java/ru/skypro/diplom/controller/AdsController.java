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

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postAd(@RequestPart(value = "image") MultipartFile file, @RequestPart(value = "properties") CreateAd createAd, Authentication authentication) {
        log.info("addAd");
        return adService.addAd(createAd, file, authentication);
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        log.info("getAllAds");
        ResponseWrapperAds rwa = adService.getAllAds();
        return ResponseEntity.ok(rwa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdFull> getAd(@PathVariable("id")  int id, Authentication authentication) {
        log.info("getAd");
        return ResponseEntity.ok(adService.getAd(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable int id, Authentication authentication) {
        log.info("deleteAd");
        adService.deleteAd(id, authentication);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AdPreview> updateAd(@PathVariable int id, @RequestBody CreateAd createAd, Authentication authentication) {
        log.info("updateAd");
        AdPreview adPreview = adService.updateAd(createAd, id, authentication);
        return ResponseEntity.ok(adPreview);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds(Authentication authentication) {
        log.info("getCurrentUserAds");
        return ResponseEntity.ok(adService.getCurrentUserAds(authentication));
    }

    @GetMapping(value ="/{id}/image", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) {
        log.info("getAdImage " + id);
        return adService.getImage(id);
    }

    @PatchMapping(value ="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file, Authentication authentication) {
        log.info("updateImage " + id);
        byte[] image = adService.updateAdImage(id, file, authentication);
        return ResponseEntity.ok(image);
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable int id) {
        log.info("getComments " + id);
        return ResponseEntity.ok(commentService.getComments(id));
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int id, Authentication authentication) {
        log.info("addComment " + id);
        log.info("addComment commentDTO " + commentDTO);
        return ResponseEntity.ok(commentService.addComment(commentDTO, id, authentication));
    }

//    @GetMapping("/{adId}/comments/{commentId}")
//    public ResponseEntity<CommentDTO> getComment(@PathVariable int adId, @PathVariable int commentId) {
//        log.info("getComment " + commentId);
//        return commentService.getComment(adId, commentId);
//    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId, Authentication authentication) {
        log.info("deleteComment " + commentId);
        commentService.deleteComment(adId, commentId, authentication);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO, Authentication authentication) {
        log.info("updateComment " + commentId);
        CommentDTO commentDTO1 = commentService.updateComment(commentId, adId, commentDTO, authentication);
        return ResponseEntity.ok(commentDTO1);
    }

}
