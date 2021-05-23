package RankedRetrieval.model;


import javax.persistence.*;
import java.util.Objects;

@Entity
public class Images {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String link;
    private String title;
    private String body;

    public Integer getImageId() {
        return id;
    }

    public void setImageId(Integer imageId) {
        this.id = imageId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Images images = (Images) o;
        return Objects.equals(id, images.id) &&
                Objects.equals(link, images.link) &&
                Objects.equals(title, images.title) &&
                Objects.equals(body, images.body);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, link, title, body);
    }
}
