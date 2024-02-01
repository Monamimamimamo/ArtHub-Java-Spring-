package Program;

public class FullProdDTO {

    private String name;

    public FullProdDTO(String name, String link, String description, String[] systems, String[] pluses, String[] minuses, String site, String logo, String[] examples) {
        this.name = name;
        this.link = link;
        this.description = description;
        this.systems = systems;
        this.pluses = pluses;
        this.minuses = minuses;
        this.site = site;
        this.logo = logo;
        this.examples = examples;
    }

    private String link;
    private String description;
    private String[] systems;
    private String[] pluses;
    private String[] minuses;
    private String[] examples;

    private String logo;
    private String site;

    public void setName(String name) {
        this.name = name;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setSystems(String[] systems) {
        this.systems = systems;
    }

    public void setPluses(String[] pluses) {
        this.pluses = pluses;
    }

    public void setMinuses(String[] minuses) {
        this.minuses = minuses;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getName() {
        return name;
    }

    public String getLink() {
        return link;
    }

    public String getDescription() {
        return description;
    }

    public String[] getSystems() {
        return systems;
    }

    public String[] getPluses() {
        return pluses;
    }

    public String[] getMinuses() {
        return minuses;
    }

    public String getSite() {
        return site;
    }
}
