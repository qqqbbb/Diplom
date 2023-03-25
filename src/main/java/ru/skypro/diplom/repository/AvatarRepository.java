package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.Avatar;

public interface AvatarRepository extends JpaRepository<Avatar, Long> {

}
