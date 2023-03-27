package ru.skypro.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.DTO.Ads;
import ru.skypro.diplom.DTO.CreateAds;
import ru.skypro.diplom.DTO.ResponseWrapperAds;
import ru.skypro.diplom.model.Comment;
import ru.skypro.diplom.service.AdService;

import java.util.List;

@RestController
@RequestMapping("Ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    private final AdService adService;

    public AdsController(AdService adService) {
        this.adService = adService;
    }

    @PostMapping
    public ResponseEntity<Ads> addAd(@RequestPart(value = "properties") CreateAds createAds, @RequestPart(value = "image") MultipartFile file) {

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
    public ResponseEntity<?> removeAd(@PathVariable int id) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAd(@PathVariable int id, @RequestBody CreateAds createAds) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAds() {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{adId}/comments")
    public ResponseEntity<?> getComments(@PathVariable String adId) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{adId}/comments")
    public ResponseEntity<?> addComments(@RequestBody Comment comment, @PathVariable String adId) {

        return ResponseEntity.ok().build();
    }


    @GetMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> getComments(@PathVariable int commentId, @PathVariable String adId) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> deleteComments(@PathVariable int commentId, @PathVariable String adId) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{adId}/comments/{commentId}")
    public ResponseEntity<?> updateComments(@PathVariable int commentId, @PathVariable String adId, @RequestBody Comment comment) {

        return ResponseEntity.ok().build();
    }

}
