package Program;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.security.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.*;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Programs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
    private String name;
    private String link;
    private String systems;
    private String description;
    private String logo;
    private String pluses;
    private String minuses;
    private String examples;
    private String site;

    public Programs(String name, String link, String systems, String description, String pluses, String minuses, String site) {
        this.name = name;
        this.link = link;
        this.systems = systems;
        this.description = description;
        this.pluses = pluses;
        this.minuses = minuses;
        this.site = site;
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

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getSystems() {
        return systems;
    }

    public String getDescription() {
        return description;
    }

    public String getLogo() {
        return logo;
    }

    public Programs() {
    }


    public String getPluses() {
        return pluses;
    }

    public String getMinuses() {
        return minuses;
    }

    public String getExamples() {
        return examples;
    }

    public String getSite() {
        return site;
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

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setSystems(String systems) {
        this.systems = systems;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setPluses(String pluses) {
        this.pluses = pluses;
    }

    public void setMinuses(String minuses) {
        this.minuses = minuses;
    }

    public void setExamples(String examples) {
        this.examples = examples;
    }

    public void setSite(String site) {
        this.site = site;
    }
}

