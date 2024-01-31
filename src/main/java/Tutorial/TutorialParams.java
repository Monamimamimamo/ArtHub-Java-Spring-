package Tutorial;

public class TutorialParams {
    private String title;
    private String link;
    private String description;
    private String difficulty;
    private String duration;
    private String author;

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

    public TutorialParams(String title, String link, String description, String difficulty, String duration, String author) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.difficulty = difficulty;
        this.duration = duration;
        this.author = author;
    }
}
