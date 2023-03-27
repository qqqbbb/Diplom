package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.Ad;

public interface AdRepository extends JpaRepository<Ad, Integer> {
}
