package cs300.beelazi.Model;


public class StaticData {
    public static String serverLink = "http://172.29.87.43:5000/client_post";
    public static UserInfomation user;
    public static SuggestModel listSuggest;
    public static String filmLink = "http://172.29.87.43:5000//client_post/files/";


    public static void setMainUser(UserInfomation User){
        user = User;
    }

    public static void setListFood(SuggestModel suggestList){
        listSuggest = suggestList;
    }
}

