package Reference;

import java.security.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.*;


@Entity
@Table(name = "\"references\"")
public class References {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
    private String title;
    private String hashtag;
    private String image;

    public References(String title, String hashtag) {
        this.title = title;
        this.hashtag = hashtag;
    }

    public References() {
    }

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

    public String getHashtag() {
        return hashtag;
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

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
