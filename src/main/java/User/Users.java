package User;


import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EntityListeners(AuditingEntityListener.class)
@Entity
public class Users {

    public Users() {
        this.brushes = new ArrayList<>();
        this.references = new ArrayList<>();
        this.tutorials = new ArrayList<>();
        this.programs = new ArrayList<>();
    }


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "\"createdAt\"")
    private LocalDateTime createdAt;
    @Column(name = "\"updatedAt\"")
    private LocalDateTime updatedAt;
    private String name;
    private String email;
    private String hash;
    private String refreshToken;
    private String login;
    @ElementCollection
    private List<Integer> brushes;

    @Column(name = "\"references\"")
    @ElementCollection
    private List<Integer> references;
    @ElementCollection
    private List<Integer> tutorials;
    @ElementCollection
    private List<Integer> programs;


    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", hash='" + hash + '\'' +
                ", refreshToken='" + refreshToken + '\'' +
                ", login='" + login + '\'' +
                ", brushes=" + brushes +
                ", references=" + references +
                ", tutorials=" + tutorials +
                ", programs=" + programs +
                '}';
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

    public String getEmail() {
        return email;
    }

    public String getHash() {
        return hash;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getLogin() {
        return login;
    }

    public List<Integer> getBrushesList() {
        return brushes;
    }

    public List<Integer> getReferencesList() {
        return references;
    }

    public List<Integer> getTutorialsList() {
        return tutorials;
    }

    public List<Integer> getProgramsList() {
        return programs;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setBrushesList(List<Integer> brushesList) {
        this.brushes = brushesList;
    }

    public void setReferencesList(List<Integer> referencesList) {
        this.references = referencesList;
    }

    public void setTutorialsList(List<Integer> tutorialsList) {
        this.tutorials = tutorialsList;
    }

    public void setProgramsList(List<Integer> programsList) {
        this.programs = programsList;
    }
}