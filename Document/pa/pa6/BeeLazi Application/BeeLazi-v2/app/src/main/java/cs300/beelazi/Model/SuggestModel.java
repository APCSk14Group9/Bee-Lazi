package cs300.beelazi.Model;

import java.io.Serializable;
import java.util.ArrayList;


public class SuggestModel implements Serializable{
    ArrayList<CommonModel> listModel;

    public SuggestModel(){
        listModel = new ArrayList<>();
    }

    public void addNewFood(FoodModel food){
        listModel.add(food);
    }

    public void addNewFilm(FilmModel film){
        listModel.add(film);
    }

    public ArrayList<CommonModel> getListModel() {
        return listModel;
    }

    public void setListModel(ArrayList<CommonModel> listModel) {
        this.listModel = listModel;
    }
}
