package ru.skypro.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.*;
//import ru.skypro.diplom.model.Comment;
import ru.skypro.diplom.service.AdService;
import ru.skypro.diplom.service.CommentService;
import ru.skypro.diplom.service.ImageService;

import java.io.IOException;

@RestController
@RequestMapping("Ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdService adService;
    private final CommentService commentService;
    private final ImageService imageService;

    public AdsController(AdService adService, CommentService commentService, ImageService imageService) {
        this.adService = adService;
        this.commentService = commentService;
        this.imageService = imageService;
    }

    @PostMapping
    public ResponseEntity<AdFull> addAd(@RequestPart(value = "properties") CreateAd createAd, @RequestPart(value = "image") MultipartFile file) {


        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<ResponseWrapperAds> getAllAds() {
        ResponseWrapperAds rwa = adService.getAllAds();
        return ResponseEntity.ok(rwa);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdPreview> getAd(@PathVariable int id) {
        return ResponseEntity.ok(adService.getAd(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAd(@PathVariable int id) {
        adService.deleteAd(id);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateAd createAd) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<ResponseWrapperAds> getAds() {
        return adService.getCurrentUserAds();
    }

    @GetMapping("/{id}/image")
    public ResponseEntity<byte[]> getAdImage(@PathVariable int id) throws IOException {
        adService.getImage(id);
        return ResponseEntity.ok().build();
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
    public ResponseEntity<CommentDTO> addComment(@RequestBody CommentDTO commentDTO, @PathVariable int adId) {
        commentService.addComment(commentDTO, adId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> getComment(@PathVariable int adId, @PathVariable int commentId) {
        return commentService.getComment(adId, commentId);
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComment(@PathVariable int commentId, @PathVariable int adId) {
        commentService.deleteComment(adId, commentId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<CommentDTO> updateComment(@PathVariable int commentId, @PathVariable int adId, @RequestBody CommentDTO commentDTO) {
        CommentDTO commentDTO1 = commentService.updateComment(commentId, adId, commentDTO);
        return ResponseEntity.ok(commentDTO1);
    }

}
