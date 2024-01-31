package Brush;

public class BrushParams {

    private String title;
    private String link;
    private String description;
    private String program;

    public BrushParams(String title, String link, String description, String program) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.program = program;
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
}
