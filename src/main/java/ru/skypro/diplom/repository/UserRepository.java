package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.User;

public interface UserRepository extends JpaRepository<User, Integer> {

}
