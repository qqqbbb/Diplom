package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.*;

@Service
public class CommentService {

    public final CommentRepository commentRepository;
    public final UserRepository userRepository;
    public final AdRepository adRepository;
    private final AuthService authService;
    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, AdRepository adRepository, AuthService authService) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.adRepository = adRepository;
        this.authService = authService;
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

    public Comment dtoToComment(CommentDTO dto, int adId) {
        log.info("dtoToComment " + adId);
        log.info("dtoToComment dto.getAuthorId " + dto.getAuthorId());
        User user = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new UserNotFoundException());
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        LocalDate localDate = parseDate(dto.getCreationDate());
        Optional<Comment> optionalComment = commentRepository.findById(dto.getId());
        return optionalComment.orElse(new Comment(dto.getId(), localDate, dto.getText(), user, ad));
    }

    public CommentDTO commentToDTO(Comment comment) {
        log.info("commentToDTO");
        User user = comment.getUser();
        return new CommentDTO(comment.getId(), comment.getText(), comment.getCreationDate().toString(), user.getId(), null, user.getFirstName());
    }

    public CommentDTO addComment(CommentDTO commentDTO, int adId, Authentication authentication) {
        log.info("addComment " + adId);
        log.info("addComment commentDTO " + commentDTO);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        boolean isAuthorized = authService.isAuthorized(authentication);
        log.info("addComment isAuthorized " + isAuthorized);
        Comment comment = dtoToComment(commentDTO, adId);
        commentRepository.save(comment);
        return commentDTO;
    }

    public CommentDTO updateComment(int commentId, int adId, CommentDTO commentDTO, Authentication authentication) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        authService.isAuthorized(user, authentication);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException());
        comment = dtoToComment(commentDTO, adId);
        commentRepository.save(comment);
        return commentDTO;
    }

    public ResponseEntity<ResponseWrapperComment> getComments(int adId) {
        log.info("getComments " + adId);
        Optional<Ad> optionalAd = adRepository.findById(adId);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        List<Comment> comments = commentRepository.findAllByAd(ad);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = commentToDTO(comment);
            commentDTOs.add(commentDTO);
        }
        ResponseWrapperComment rwc = new ResponseWrapperComment(commentDTOs.size(), commentDTOs);
        return ResponseEntity.ok(rwc);
    }

    public ResponseEntity<CommentDTO> getComment(int adId, int commentId) {
        log.info("getComment ");
        Optional<Ad> optionalAd = adRepository.findById(adId);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        List<Comment> comments = commentRepository.findAllByAd(ad);
        for (Comment c : comments) {
            if (c.getId() == commentId)
                return ResponseEntity.ok(commentToDTO(c));
        }
        throw new CommentNotFoundException();
    }

    public void deleteComment(int adId, int commentId, Authentication authentication) {
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        authService.isAuthorized(user, authentication);
        commentRepository.deleteById(commentId);
    }

}
