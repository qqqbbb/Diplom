package ru.skypro.diplom.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.diplom.model.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {

    public Optional<User> findFirstByFirstNameAndAndLastNameAndPhone(String firstName, String lastName, String phone);
}
