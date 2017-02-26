package cs300.beelazi.Model;

import java.io.Serializable;

import cs300.beelazi.MapModules.MapCoord;


public class Place implements Serializable{
    private String name;
    private String address;
    private String price;
    private String phoneNo;
    private MapCoord coord;
    private String imgUrl;

    public Place(String name, String address, String price, String phoneNo, MapCoord coord, String imgUrl) {
        this.name = name;
        this.address = address;
        this.price = price;
        this.phoneNo = phoneNo;
        this.coord = coord;
        this.imgUrl = imgUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public MapCoord getCoord() {
        return coord;
    }

    public void setCoord(MapCoord coord) {
        this.coord = coord;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }
}