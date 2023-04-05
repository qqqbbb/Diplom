package ru.skypro.diplom.controller;

import org.slf4j.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.service.*;

import java.io.IOException;

@RestController
@RequestMapping("Ads")
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
        public ResponseEntity<AdFull> addAd( @RequestPart(value = "image") MultipartFile file, @RequestPart(value = "x") CreateAd createAd) throws IOException {
        log.info("addAd");
        return ResponseEntity.ok(adService.addAd(createAd, file));
    }

    @PostMapping("/AddWithDefaultImage")
    public ResponseEntity<AdFull> addWithDefaultImage(@RequestBody CreateAd createAd) {
        log.info("AddWithDefaultImage");
        return ResponseEntity.ok(adService.addAd(createAd));
    }

//    @PostMapping("/AddImage")
//    public ResponseEntity<?> addImage() {
//        log.info("addImage");
//        return ResponseEntity.ok(adService.addImage());
//    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        log.info("getAllAds");
        ResponseWrapperAds rwa = adService.getAllAds();
        return ResponseEntity.ok(rwa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdPreview> getAd(@PathVariable int id) {
        log.info("getAd");
        return ResponseEntity.ok(adService.getAd(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable int id) {
        log.info("deleteAd");
        adService.deleteAd(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateAd createAd) {
        log.info("updateAd");
        adService.updateAd(createAd, id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getCurrentUserAds() {
        log.info("getCurrentUserAds");
        return adService.getCurrentUserAds();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) throws IOException {
        log.info("getAdImage");
        adService.getImage(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(value ="/{id}/image", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file) throws IOException {
        log.info("updateImage " + id);
        adService.updateAdImage(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable int adId) {
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
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId) {
        log.info("deleteComment " + commentId);
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO) {
        log.info("updateComment " + commentId);
        CommentDTO commentDTO1 = commentService.updateComment(commentId, adId, commentDTO);
        return ResponseEntity.ok(commentDTO1);
    }

}
