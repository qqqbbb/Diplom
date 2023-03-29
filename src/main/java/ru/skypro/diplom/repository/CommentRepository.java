package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.*;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Integer> {
    List<Comment> findAllByAd(Ad ad);
}
