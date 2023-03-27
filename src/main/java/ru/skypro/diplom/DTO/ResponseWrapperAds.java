package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Ad;
import java.util.List;

public class ResponseWrapperAds {
    private int count;
    private List<Ad> results;

    public ResponseWrapperAds(int count, List<Ad> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public List<Ad> getResults() {
        return results;
    }
}
