package ru.skypro.diplom.service;

import org.slf4j.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.*;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.*;

import java.time.*;
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

    public LocalDateTime longToLocalDateTime(long dateTime) {
        log.info("longToLocalDateTime");
        LocalDateTime triggerTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(dateTime),
                        TimeZone.getDefault().toZoneId());
        return triggerTime;
    }

//    public long localDateTimeToLong(LocalDateTime dateTime) {
//        log.info("localDateTimeToLong");
//        ZonedDateTime zdt = ZonedDateTime.of(dateTime, ZoneId.systemDefault());
//        return zdt.toInstant().toEpochMilli();
//    }

    public long localDateTimeToLong(LocalDateTime localDateTime) {
        log.info("localDateTimeToLong");
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public Comment dtoToComment(CommentDTO dto, int adId) {
        log.info("dtoToComment " + adId);
        log.info("dtoToComment dto.getAuthorId " + dto.getAuthorId());
        User user = userRepository.findById(dto.getAuthorId()).orElseThrow(() -> new UserNotFoundException());
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        LocalDateTime localDateTime = longToLocalDateTime(dto.getCreationDate());
//        Optional<Comment> optionalComment = commentRepository.findById(dto.getId());
        return new Comment(dto.getId(), localDateTime, dto.getText(), user, ad);
    }

    public CommentDTO commentToDTO(Comment comment) {
        log.info("commentToDTO");
        User user = comment.getUser();
        long time = localDateTimeToLong(comment.getCreationDate());
        return new CommentDTO(comment.getId(), comment.getText(), time, user.getId(), null, user.getFirstName());
    }

    public CommentDTO addComment(CommentDTO commentDTO, int adId, Authentication authentication) {
        log.info("addComment " + adId);
//        log.info("addComment commentDTO " + commentDTO);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        boolean isAuthorized = authService.isAuthorized(authentication);
        log.info("addComment isAuthorized " + isAuthorized);
        Comment comment = new Comment(LocalDateTime.now(), commentDTO.getText(), user, ad);
        commentRepository.save(comment);
        return commentToDTO(comment);
    }

    public CommentDTO updateComment(int commentId, int adId, CommentDTO commentDTO, Authentication authentication) {
        log.info("updateComment " + commentId);
        log.info("updateComment commentDTO " + commentDTO);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        authService.isAuthorized(user, authentication);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException());
        comment.setText(commentDTO.getText());
        commentRepository.save(comment);
        return commentToDTO(comment);
    }

    public ResponseWrapperComment getComments(int adId) {
        log.info("getComments " + adId);
        Optional<Ad> optionalAd = adRepository.findById(adId);
        Ad ad = optionalAd.orElseThrow(() -> new AdNotFoundException());
        List<Comment> comments = commentRepository.findAllByAd(ad);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
            CommentDTO commentDTO = commentToDTO(comment);
            commentDTOs.add(commentDTO);
        }
        return new ResponseWrapperComment(commentDTOs.size(), commentDTOs);
    }

    public ResponseEntity<CommentDTO> getComment(int adId, int commentId) {
        log.info("getComment " + commentId);
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
        log.info("delete single Comment " + commentId);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = ad.getUser();
        authService.isAuthorized(user, authentication);
        commentRepository.deleteById(commentId);
    }


}
