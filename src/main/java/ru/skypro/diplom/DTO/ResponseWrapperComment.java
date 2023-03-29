package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Comment;
import java.util.List;

public class ResponseWrapperComment {
    private int count;
    private List<Comment> results;

    public ResponseWrapperComment(int count, List<Comment> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public List<Comment> getResults() {
        return results;
    }
}
