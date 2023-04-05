package ru.skypro.diplom.DTO;

import ru.skypro.diplom.model.Comment;
import java.util.List;

public class ResponseWrapperComment {
    private int count;
    private List<CommentDTO> results;

    public ResponseWrapperComment(int count, List<CommentDTO> results) {
        this.count = count;
        this.results = results;
    }

    public int getCount() {
        return count;
    }

    public List<CommentDTO> getResults() {
        return results;
    }
}
