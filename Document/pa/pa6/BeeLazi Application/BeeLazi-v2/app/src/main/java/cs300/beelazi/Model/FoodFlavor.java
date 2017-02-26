package cs300.beelazi.Model;


public class FoodFlavor {
    String[] foodType;

    public String[] getFoodType() {
        return foodType;
    }

    public void setFoodType(String[] foodType) {
        this.foodType = foodType;
    }

    public FoodFlavor(){
        foodType = new String[13];

    }

    public FoodFlavor(String[] foodType) {
        this.foodType = foodType;
    }
}
