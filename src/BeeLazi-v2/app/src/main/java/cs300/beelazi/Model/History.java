package cs300.beelazi.Model;


public class History {
    String name;
    String imgUrl;
    String type;
    String cardPosition;

    public History(String name, String imgUrl, String type, String cardPosition) {
        this.name = name;
        this.imgUrl = imgUrl;
        this.type = type;
        this.cardPosition = cardPosition;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCardPosition() {
        return cardPosition;
    }

    public void setCardPosition(String cardPosition) {
        this.cardPosition = cardPosition;
    }
}

