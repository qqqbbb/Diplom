package ru.skypro.diplom.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.skypro.diplom.dto.CreateAds;
import ru.skypro.diplom.dto.NewPassword;
import ru.skypro.diplom.model.Comment;

@RestController
@RequestMapping("Ads")
@CrossOrigin(value = "http://localhost:3000")
public class AdsController {

    @GetMapping
    public ResponseEntity<?> getALLAds() {

        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> addAds(@RequestPart(value = "properties") CreateAds createAds, @RequestPart(value = "image") MultipartFile file) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/me")
    public ResponseEntity<?> getAds() {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{ad_pk}/comments")
    public ResponseEntity<?> getComments(@PathVariable String ad_pk) {

        return ResponseEntity.ok().build();
    }

    @PostMapping("/{ad_pk}/comments")
    public ResponseEntity<?> addComments(@RequestBody Comment comment, @PathVariable String ad_pk) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAds(@PathVariable int id) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeAds(@PathVariable int id) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> updateAds(@PathVariable int id, @RequestBody CreateAds createAds) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<?> getComments(@PathVariable int id, @PathVariable String ad_pk) {

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<?> deleteComments(@PathVariable int id, @PathVariable String ad_pk) {

        return ResponseEntity.ok().build();
    }

    @PatchMapping("/{ad_pk}/comments/{id}")
    public ResponseEntity<?> updateComments(@PathVariable int id, @PathVariable String ad_pk, @RequestBody Comment comment) {

        return ResponseEntity.ok().build();
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<?> updateImage(@PathVariable int id, @RequestPart(value = "image") MultipartFile file) {

        return ResponseEntity.ok().build();
    }

}
