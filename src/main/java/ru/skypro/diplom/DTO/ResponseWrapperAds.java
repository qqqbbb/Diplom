package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Ad;
import java.util.List;

public class ResponseWrapperAds {
    private int count;
    private List<AdPreview> results;

    public ResponseWrapperAds() {
    }

    public ResponseWrapperAds(int count, List<AdPreview> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public List<AdPreview> getResults() {
        return results;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setResults(List<AdPreview> results) {
        this.results = results;
    }
}
