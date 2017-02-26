package cs300.beelazi.Model;

import java.io.Serializable;

public class FilmModel extends CommonModel implements Serializable{

    String filmURL, description, duration;

    public String getFilmURL() {
        return filmURL;
    }

    public void setFilmURL(String filmURL) {
        this.filmURL = filmURL;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public FilmModel(String type, String name, String imgURL, String filmURL, String description, String duration) {
        this.type = type;
        this.name = name;
        this.imgURL = imgURL;
        this.filmURL = filmURL;
        this.description = description;
        this.duration = duration;
    }
}
