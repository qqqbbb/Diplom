package ru.skypro.diplom.model;

import javax.persistence.*;

@Entity
@Table(name = "Ads")
public class Ad {
    @Id
    private long id;
    private String title;
    private int author;

    private int price;
    private String image;


}
