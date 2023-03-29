package ru.skypro.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
//import ru.skypro.diplom.model.Comment;
import ru.skypro.diplom.service.AdService;
import ru.skypro.diplom.service.CommentService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("Ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdService adService;
    private final CommentService commentService;

    public AdsController(AdService adService, CommentService commentService) {
        this.adService = adService;
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<FullAds> addAd(@RequestPart(value = "properties") CreateAds createAds, @RequestPart(value = "image") MultipartFile file) {


        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds rwa = adService.getAllAds();
        return ResponseEntity.ok(rwa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Ads> getAd(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable int id) {
        adService.deleteAd(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateAds createAds) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAds() {
        return adService.getCurrentUserAds();
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file) throws IOException {
        adService.updateAdImage(id, file);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments")
    public ResponseEntity<ResponseWrapperComment> getComments(@PathVariable int adId) {
        return commentService.getComments(adId);
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<?> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int adId) {

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> getComments(@PathVariable int commentId, @PathVariable int adId) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO) {

        return ResponseEntity.ok().build();
    }

}
