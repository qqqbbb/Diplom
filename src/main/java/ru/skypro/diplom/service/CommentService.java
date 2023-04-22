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

    /**
     * Преобразует long в LocalDateTime
     *
     * @param dateTime дата и время {@link long}
     * @return дата и время {@link LocalDateTime}
     */
    public LocalDateTime longToLocalDateTime(long dateTime) {
        log.info("longToLocalDateTime");
        Instant instant = Instant.ofEpochMilli(dateTime);
        ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
        return zonedDateTime.toLocalDateTime();
    }

    /**
     * Преобразует LocalDateTime в long
     *
     * @param localDateTime дата и время {@link LocalDateTime}
     * @return дата и время {@link long}
     */
    public long localDateTimeToLong(LocalDateTime localDateTime) {
        log.info("localDateTimeToLong");
        return localDateTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }

    public Comment dtoToComment(CommentDTO dto, int adId) {
        log.info("dtoToComment " + adId);
        log.info("dtoToComment dto.getAuthorId " + dto.getAuthor());
        User user = userRepository.findById(dto.getAuthor()).orElseThrow(() -> new UserNotFoundException());
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        LocalDateTime localDateTime = longToLocalDateTime(dto.getCreatedAt());
//        Optional<Comment> optionalComment = commentRepository.findById(dto.getId());
        return new Comment(dto.getPk(), localDateTime, dto.getText(), user, ad);
    }

    /**
     * Преобразует Comment в CommentDTO
     *
     * @param comment комментарий {@link Comment}
     * @return DTO {@link CommentDTO}
     */
    public CommentDTO commentToDTO(Comment comment) {
        log.info("commentToDTO");
        User user = comment.getUser();
        long time = localDateTimeToLong(comment.getCreationDate());
        String avatar = "/users/" + user.getId() + "/avatar";
        CommentDTO commentDTO = new CommentDTO(comment.getId(), comment.getText(), time, user.getId(), avatar, user.getFirstName());
        log.info("commentToDTO " + commentDTO);
        return commentDTO;
    }

    /**
     * Добавляет и сохраняет новый комментарий
     *
     * @param commentDTO комментарий {@link CommentDTO}
     * @param adId первичный ключ объявления
     * @return комментарий {@link CommentDTO}
     */
    public CommentDTO addComment(CommentDTO commentDTO, int adId, Authentication authentication) {
        log.info("addComment " + adId);
        log.info("addComment commentDTO " + commentDTO);
        boolean isAuthorized = authService.isAuthorized(authentication);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
        User user = userRepository.findFirstByUsername(authentication.getName()).orElseThrow(()-> new UserNotFoundException());
        log.info("addComment isAuthorized " + isAuthorized);
        Comment comment = new Comment(LocalDateTime.now(), commentDTO.getText(), user, ad);
        commentRepository.save(comment);
        return commentToDTO(comment);
    }

    /**
     * Обновляет комментарий
     *
     * @param commentDTO комментарий {@link CommentDTO}
     * @param adId первичный ключ объявления
     * @param commentId первичный ключ комментария
     * @return комментарий {@link CommentDTO}
     */
    public CommentDTO updateComment(int commentId, int adId, CommentDTO commentDTO, Authentication authentication) {
        log.info("updateComment " + commentId);
        log.info("updateComment commentDTO " + commentDTO);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException());
        User user = comment.getUser();
        authService.isAuthorized(user, authentication);
        comment.setText(commentDTO.getText());
        commentRepository.save(comment);
        return commentToDTO(comment);
    }

    /**
     * Возвращает все комментарии для данного объявления
     *
     * @param adId первичный ключ объявления
     * @return DTO комментарии {@link ResponseWrapperComment}
     */
    public ResponseWrapperComment getComments(int adId) {
        log.info("getComments " + adId);
        Ad ad = adRepository.findById(adId).orElseThrow(() -> new AdNotFoundException());
//        log.info("getComments ad getUsername " + ad.getUser().getUsername());
        List<Comment> comments = commentRepository.findAllByAd(ad);
        List<CommentDTO> commentDTOs = new ArrayList<>();
        for (Comment comment : comments) {
//            log.info("getComments comment getUsername " + comment.getUser().getUsername());
            CommentDTO commentDTO = commentToDTO(comment);
            commentDTOs.add(commentDTO);
        }
        return new ResponseWrapperComment(commentDTOs.size(), commentDTOs);
    }

    /**
     * Удаляет комментарий
     *
     * @param adId первичный ключ объявления
     * @param commentId первичный ключ комментария
     */
    public void deleteComment(int adId, int commentId, Authentication authentication) {
        log.info("delete single Comment " + commentId);
        Comment comment = commentRepository.findById(commentId).orElseThrow(()->new CommentNotFoundException());
        User user = comment.getUser();
        authService.isAuthorized(user, authentication);
        commentRepository.deleteById(commentId);
    }


}
