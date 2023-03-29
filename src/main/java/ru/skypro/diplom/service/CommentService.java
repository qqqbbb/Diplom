package ru.skypro.diplom.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.CommentDTO;
import ru.skypro.diplom.DTO.ResponseWrapperComment;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class CommentService {

    public final CommentRepository commentRepository;
    public final UserRepository userRepository;
    public final AdRepository adRepository;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, AdRepository adRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
    }

    public LocalDate parseDate(String date) {
        try {
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
//            return LocalDate.parse(date.substring(0, 16), formatter);
            return LocalDate.parse(date);
        }
        catch (DateTimeParseException e)
        {
            log.error("Unable to parse date: " + date);
            e.printStackTrace();
        }
        return null;
    }

    public Comment dtoToComment(CommentDTO dto) {
        Optional<User> optionalUser = userRepository.findById(dto.getUserId());
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        Optional<Ad> optionalAd = adRepository.findById(dto.getAdId());
        Ad ad = optionalAd.orElseThrow(() -> new CommentNotFoundException());
        LocalDate localDate = parseDate(dto.getCreationDate());
        return new Comment(dto.getId(), localDate, dto.getText(), user, ad);
    }

    public ResponseEntity<ResponseWrapperComment> getComments(int adId) {
        Optional<Ad> optionalAd = adRepository.findById(adId);
        Ad ad = optionalAd.orElseThrow(() -> new CommentNotFoundException());
        List<Comment> comments = commentRepository.findAllByAd(ad);
        ResponseWrapperComment rwc = new ResponseWrapperComment(comments.size(), comments);
        return ResponseEntity.ok(rwc);
    }
}
