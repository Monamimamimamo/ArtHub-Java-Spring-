package Brush;


import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.security.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.*;


@EntityListeners(AuditingEntityListener.class)
@Entity
public class Brushes {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
    private String title;
    private String link;
    private String description;
    private String program;
    private String image;

    public Brushes(String title, String link, String description, String program) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.program = program;
    }

    public Brushes() {
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

    public void setProgram(String program) {
        this.program = program;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String getProgram() {
        return program;
    }

    public String getImage() {
        return image;
    }
}
