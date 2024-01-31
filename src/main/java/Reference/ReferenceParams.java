package Reference;

public class ReferenceParams {
    private String title;
    private String hashtag;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setHashtag(String hashtag) {
        this.hashtag = hashtag;
    }

    public String getTitle() {
        return title;
    }

    public String getHashtag() {
        return hashtag;
    }

    public ReferenceParams(String title, String hashtag) {
        this.title = title;
        this.hashtag = hashtag;
    }
}
