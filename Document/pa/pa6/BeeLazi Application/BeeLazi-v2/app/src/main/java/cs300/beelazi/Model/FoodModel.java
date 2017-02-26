package cs300.beelazi.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class FoodModel extends CommonModel implements Serializable{
    ArrayList<String> cost;
    ArrayList<String> address;
    ArrayList<String> placeName;
    ArrayList<String> addressURL;

    public ArrayList<String> getCost() {
        return cost;
    }

    public void setCost(ArrayList<String> cost) {
        this.cost = cost;
    }

    public ArrayList<String> getAddressURL() {
        return addressURL;
    }

    public void setAddressURL(ArrayList<String> addressURL) {
        this.addressURL = addressURL;
    }

    public FoodModel(String type, String name, String imgURL){
        this.name = name;
        this.imgURL = imgURL;
        this.type = type;
        address = new ArrayList<>();
        placeName = new ArrayList<>();
        cost = new ArrayList<>();
        addressURL = new ArrayList<>();
    }

    public void addAddressURL(String url){
        addressURL.add(url);
    }

    public void addCost(String c){
        cost.add(c);
    }

    public void addAddress(String addr){
        address.add(addr);
    }

    public void addPlaceName(String name){
        placeName.add(name);
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgURL() {
        return imgURL;
    }

    public void setImgURL(String imgURL) {
        this.imgURL = imgURL;
    }

    public ArrayList<String> getAddress() {
        return address;
    }

    public void setAddress(ArrayList<String> address) {
        this.address = address;
    }

    public ArrayList<String> getPlaceName() {
        return placeName;
    }

    public void setPlaceName(ArrayList<String> placeName) {
        this.placeName = placeName;
    }
}
