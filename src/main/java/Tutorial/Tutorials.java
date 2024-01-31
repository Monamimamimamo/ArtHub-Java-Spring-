package Tutorial;

import java.security.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.*;

@Entity
public class Tutorials {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    public Tutorials(String title, String link, String description, String difficulty, String duration, String author) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.difficulty = difficulty;
        this.duration = duration;
        this.author = author;
    }

    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
    private String title;

    public Tutorials() {
    }

    private String link;
    private String description;
    private String difficulty;
    private String duration;
    private String author;
    private String image;

    public Integer getId() {
        return id;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public String getDuration() {
        return duration;
    }

    public String getAuthor() {
        return author;
    }

    public String getImage() {
        return image;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
