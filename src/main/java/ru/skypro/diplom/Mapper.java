package ru.skypro.diplom;

import org.springframework.stereotype.Service;
import ru.skypro.diplom.DTO.*;
import ru.skypro.diplom.Exceptions.UserNotFoundException;
import ru.skypro.diplom.model.*;
import ru.skypro.diplom.repository.UserRepository;

import java.util.Optional;

@Service
public class Mapper {

    private final UserRepository userRepository;

    public Mapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Ad fullAdToAd(FullAd fullAd) {
        Optional<User> optionalUser = userRepository.findFirstByFirstNameAndAndLastNameAndPhone(fullAd.getAuthorFirstName(), fullAd.getAuthorLastName(), fullAd.getPhone());
        User user = optionalUser.orElseThrow(() -> new UserNotFoundException());
        return new Ad(fullAd.getTitle(), fullAd.getPrice(), user);
    }

//    public FullAd adToFullAd(Ad ad) {
//        User user = ad.getUser();
//        if (user == null)
//            throw new UserNotFoundException();
//
//        return new FullAd(ad.getId(), ad.getTitle(), user.getFirstName(), user.getLastName(), ad.)
//    }

//    public User RegisterReqToUser(RegisterReq registerReq){
//        User user = new User(registerReq.getFirstName(), registerReq.getLastName(), registerReq.get)
//
//    }
}
