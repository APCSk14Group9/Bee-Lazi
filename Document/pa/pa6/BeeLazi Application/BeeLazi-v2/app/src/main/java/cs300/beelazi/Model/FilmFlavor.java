package cs300.beelazi.Model;


public class FilmFlavor {

    String[] filmFlavor;

    public String[] getFilmFlavor() {
        return filmFlavor;
    }

    public void setFilmFlavor(String[] filmFlavor) {
        this.filmFlavor = filmFlavor;
    }

    public FilmFlavor(){
        filmFlavor = new String[11];
    }

    public FilmFlavor(String[] filmFlavor) {

        this.filmFlavor = filmFlavor;
    }
}
