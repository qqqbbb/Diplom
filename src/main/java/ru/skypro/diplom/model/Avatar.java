package ru.skypro.diplom.model;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "Avatars")
public class Avatar {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String filePath;
    private long fileSize;
    private String mediaType;
    private byte[] data;

    public Avatar() {
    }

    public Avatar(String filePath, long fileSize, String mediaType, byte[] data) {
        this.filePath = filePath;
        this.fileSize = fileSize;
        this.mediaType = mediaType;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public String getFilePath() {
        return filePath;
    }

    public long getFileSize() {
        return fileSize;
    }

    public String getMediaType() {
        return mediaType;
    }

    public byte[] getData() {
        return data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Avatar avatar = (Avatar) o;
        return getFileSize() == avatar.getFileSize() && getId() == avatar.getId() && getMediaType().equals(avatar.getMediaType()) && Arrays.equals(getData(), avatar.getData());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getId(), getFileSize(), getMediaType());
        result = 31 * result + Arrays.hashCode(getData());
        return result;
    }
}

