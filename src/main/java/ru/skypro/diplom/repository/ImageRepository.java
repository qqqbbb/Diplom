package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.Image;

public interface ImageRepository extends JpaRepository<Image, Long> {

}
