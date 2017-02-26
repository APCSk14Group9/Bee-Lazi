package cs300.beelazi.CardUI;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs300.beelazi.CardUI.CardSlidePanel.CardSwitchListener;
import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.CommonModel;
import cs300.beelazi.Model.FilmModel;
import cs300.beelazi.Model.FoodModel;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.Model.SuggestModel;
import cs300.beelazi.R;
import cs300.beelazi.ServerWidget.RequestToServer;

@SuppressLint({"HandlerLeak", "NewApi", "InflateParams"})
public class CardFragment extends Fragment implements RequestToServer.RequestResult{

    private CardSwitchListener cardSwitchListener;
    RequestToServer requestToServer;
    public static int cardPosition;
    ArrayList<String> imagePaths;
    ArrayList<String> names;
    CardSlidePanel slidePanel;

    public CardFragment(){
        imagePaths = new ArrayList<>();
        names = new ArrayList<>();
    }

//    private String imagePaths[] = {"assets://wall01.jpg",
//            "assets://wall02.jpg", "assets://wall03.jpg",
//            "assets://wall04.jpg", "assets://wall05.jpg",
//            "assets://wall06.jpg", "assets://wall07.jpg",
//            "assets://wall08.jpg", "assets://wall09.jpg",
//            "assets://wall10.jpg", "assets://wall11.jpg",
//            "assets://wall12.jpg", "assets://wall01.jpg",
//            "assets://wall02.jpg", "assets://wall03.jpg",
//            "assets://wall04.jpg", "assets://wall05.jpg",
//            "assets://wall06.jpg", "assets://wall07.jpg",
//            "assets://wall08.jpg", "assets://wall09.jpg",
//            "assets://wall10.jpg", "assets://wall11.jpg", "assets://wall12.jpg"};
//
//    private String names[] = {"Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing",
//            "Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing",
//            "Testing", "Testing", "Testing", "Testing", "Testing", "Testing", "Testing"};

    private List<CardDataItem> dataList = new ArrayList<CardDataItem>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.card_layout, null);
        cardPosition = 0;
        initImageLoader(rootView.getContext());
        initView(rootView);
        return rootView;
    }

    private void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    @SuppressWarnings("deprecation")
    private void initImageLoader(Context mContext) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                mContext)
                .memoryCacheExtraOptions(480, 800)
                .threadPoolSize(3)
                .threadPriority(Thread.NORM_PRIORITY - 1)
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024).memoryCacheSizePercentage(13)
                .discCacheSize(50 * 1024 * 1024)
                .discCacheFileCount(100)
                .discCacheFileNameGenerator(new HashCodeFileNameGenerator())
                .imageDownloader(new BaseImageDownloader(mContext)) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
                .writeDebugLogs().build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
    }


    private void initView(View rootView) {
        slidePanel = (CardSlidePanel) rootView
                .findViewById(R.id.image_slide_panel);
        cardSwitchListener = new CardSwitchListener() {

            @Override
            public void onShow(int index) {
            }

            @Override
            public void onCardVanish(int index, int type) {
                if (type==1) {
                    // History Request
                    JsonHelper jsonHistory = new JsonHelper();
                    String action = "INS";
                    String table = "History";

                    CommonModel item;
                    if (cardPosition<StaticData.listSuggest.getListModel().size()) {
                        item = StaticData.listSuggest.getListModel().get(cardPosition);

                        ArrayList<Pair<String, String>> condition = new ArrayList<>();
                        condition.add(new Pair("username", StaticData.user.getUsername()));
                        condition.add(new Pair("itemname", item.getName()));
                        condition.add(new Pair("itemlink", item.getImgURL()));
                        condition.add(new Pair("Type", item.getType()));
                        condition.add(new Pair("itemposition", String.valueOf(cardPosition)));
                        try {
                            String query = jsonHistory.writeQuery(action, table, condition);
                            makeQuery(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        // Like Request

                        JsonHelper jsonLike = new JsonHelper();
                        String actionLike = "LIK";
                        try {
                            String query = jsonLike.writeQuery(actionLike, null, null);
                            makeQuery(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    // Unlike Request

                    JsonHelper jsonUnlike = new JsonHelper();
                    String actionUnlike = "UNL";
                    try {
                        String query = jsonUnlike.writeQuery(actionUnlike, null, null);
                        makeQuery(query);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                cardPosition++;
            }

            @Override
            public void onItemClick(View cardView, int index) {
            }
        };
        slidePanel.setCardSwitchListener(cardSwitchListener);

        JsonHelper jsonHelper = new JsonHelper();
        String action = "XXX";
        String table = "";
        ArrayList<Pair<String,String>> condition = new ArrayList<>();
        if (StaticData.user == null){
            Log.d("fuckfuck", "NOO");
        }
        condition.add(new Pair<String, String>("username", StaticData.user.getUsername()));
        try {
//            String query = jsonHelper.writeQuery(action, table, null);
            String query = jsonHelper.writeQuery(action, table, condition);
            makeQuery(query);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    private void prepareDataList() {
        ArrayList<CommonModel> listSuggest = StaticData.listSuggest.getListModel();
        for (int i=0; i<listSuggest.size(); i++){
            String imgURL  = listSuggest.get(i).getImgURL();
            String name = listSuggest.get(i).getName();
            imagePaths.add(imgURL);
            names.add(name);
        }

        int num = imagePaths.size();

            for (int i = 0; i < num; i++) {
                CardDataItem dataItem = new CardDataItem();
                dataItem.userName = names.get(i);
                dataItem.imagePath = imagePaths.get(i);
                dataItem.likeNum = (int) (Math.random() * 10);
                dataItem.imageNum = (int) (Math.random() * 6);
                dataList.add(dataItem);
            }
    }

    @Override
    public void processFinish(String result) {

        SuggestModel suggestModel = new SuggestModel();

        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i=0; i<jsonArray.length(); i++){
                JSONArray object = (JSONArray) jsonArray.get(i);
                String type = object.getString(0);
                if (type.equals("Film")){
                    String name = object.getString(1),
                            filmURL = object.getString(2),
                            description = object.getString(3),
                            duration = object.getString(5),
                            imgURL = object.getString(16);


                    FilmModel filmModel = new FilmModel(type, name, imgURL, filmURL, description, duration);
                    suggestModel.addNewFilm(filmModel);
                }
                else if (type.equals("Food")){
                    String name = object.getString(1),
                            imgURL = object.getString(19);

                    FoodModel foodModel = new FoodModel(type, name, imgURL);
                    suggestModel.addNewFood(foodModel);
                }
            }

            StaticData.setListFood(suggestModel);
            prepareDataList();
            slidePanel.fillData(dataList);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}