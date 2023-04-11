package ru.skypro.diplom.controller;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.service.*;

import java.io.IOException;

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
    public ResponseEntity<?> addAd( @RequestPart(value = "image") MultipartFile file, @RequestPart(value = "properties") CreateAd createAd, Authentication authentication) throws IOException {
        log.info("addAd");
        return adService.addAd(createAd, file, authentication.getName());
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
        adService.deleteAd(id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateAd createAd, Authentication authentication) {
        log.info("updateAd");
        adService.updateAd(createAd, id, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds(Authentication authentication) {
        log.info("getCurrentUserAds");
        return adService.getCurrentUserAds(authentication.getName());
    }

    @GetMapping(value ="/{id}/image", produces = {MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) {
        log.info("getAdImage " + id);
        return adService.getImage(id);
    }

    @PatchMapping(value ="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file, Authentication authentication) throws IOException {
        log.info("updateImage " + id);
        adService.updateAdImage(id, file, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable int adId) {
        log.info("getComments ");
        log.info("getComments " + adId);
        return commentService.getComments(adId);
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int adId) {
        log.info("addComment " + adId);
        commentService.addComment(commentDTO, adId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable int adId, @PathVariable int commentId) {
        log.info("getComment " + commentId);
        return commentService.getComment(adId, commentId);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId, Authentication authentication) {
        log.info("deleteComment " + commentId);
        commentService.deleteComment(adId, commentId, authentication.getName());
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO, Authentication authentication) {
        log.info("updateComment " + commentId);
        CommentDTO commentDTO1 = commentService.updateComment(commentId, adId, commentDTO, authentication.getName());
        return ResponseEntity.ok(commentDTO1);
    }

}
