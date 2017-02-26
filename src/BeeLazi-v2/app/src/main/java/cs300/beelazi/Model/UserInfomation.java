package cs300.beelazi.Model;


public class UserInfomation {
    String username;
    String realname;
    String password;

    public String getImageProfile() {
        return imageProfile;
    }

    public void setImageProfile(String imageProfile) {
        this.imageProfile = imageProfile;
    }

    public UserInfomation(String username, String realname, String password, String imageProfile) {

        this.username = username;
        this.realname = realname;
        this.password = password;
        this.imageProfile = imageProfile;
    }

    String imageProfile;
    FoodFlavor foodFlavor;
    FilmFlavor filmFlavor;

    public UserInfomation(String username, String realname, String password){
        foodFlavor = new FoodFlavor();
        filmFlavor = new FilmFlavor();
        this.username = username;
        this.realname = realname;
        this.password = password;
    }

    public UserInfomation(String username, String realname, String password, FoodFlavor foodFlavor, FilmFlavor filmFlavor) {
        this.username = username;
        this.realname = realname;
        this.password = password;
        this.foodFlavor = foodFlavor;
        this.filmFlavor = filmFlavor;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRealname() {
        return realname;
    }

    public void setRealname(String realname) {
        this.realname = realname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public FoodFlavor getFoodFlavor() {
        return foodFlavor;
    }

    public void setFoodFlavor(FoodFlavor foodFlavor) {
        this.foodFlavor = foodFlavor;
    }

    public FilmFlavor getFilmFlavor() {
        return filmFlavor;
    }

    public void setFilmFlavor(FilmFlavor filmFlavor) {
        this.filmFlavor = filmFlavor;
    }
}
