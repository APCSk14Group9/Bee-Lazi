package cs300.beelazi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.MapModules.MapCoord;
import cs300.beelazi.Model.FoodModel;
import cs300.beelazi.Model.Place;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.ServerWidget.RequestToServer;
import cs300.beelazi.ServerWidget.RequestToServer.RequestResult;
import ru.rambler.libs.swipe_layout.SwipeLayout;

public class DetailActivity extends AppCompatActivity implements RequestResult{

    public static ArrayList<Place> abcList = new ArrayList<>();
    RequestToServer requestToServer;
    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView userCoverView;
    private AppBarLayout appBarLayout;
    TextView tvFoodTitle;
    ArrayList<Place> PlacesList;
    ArrayList<Integer> itemOffset;
    FoodModel foodModel;
    int position;
    Adapter adapter;
    RecyclerView recycler;


    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        position = Integer.parseInt(getIntent().getStringExtra("position"));
        foodModel = (FoodModel) StaticData.listSuggest.getListModel().get(position);

        if (getSupportActionBar() != null){
            getSupportActionBar().setTitle(foodModel.getName());
        }
        initLayouts();
        initListeners();
    }

    private String decodeUnicode(String string){
        byte[] utf8 = new byte[0];
        try {
            utf8 = string.getBytes("UTF-8");
            string = new String(utf8, "UTF-8");
            Log.d("fuckfuck", string);
            return string;
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    private void initLayouts() {
        tvFoodTitle = (TextView) findViewById(R.id.foodTitle);
        tvFoodTitle.setText(foodModel.getName());
        foodModel = (FoodModel) StaticData.listSuggest.getListModel().get(position);

        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);

        userCoverView = (ImageView) findViewById(R.id.food_cover);
        Picasso.with(this).load(foodModel.getImgURL()).into(userCoverView);

        LinearLayoutManager manager = new LinearLayoutManager(this);
        recycler = (RecyclerView) findViewById(R.id.recycler);
        recycler.setLayoutManager(manager);
        PlacesList = new ArrayList<>();
        itemOffset = new ArrayList<>();
        adapter = new Adapter(this, PlacesList, itemOffset);


        JsonHelper jsonHelper = new JsonHelper();
        String action = "SEL";
        String table = "FoodAdd";
        ArrayList<Pair<String, String>> condition = new ArrayList<>();

        condition.add(new Pair<String, String>("foodname", decodeUnicode(foodModel.getName())));
        try {
            String query = jsonHelper.writeQuery(action, table, condition);
            makeQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void initListeners() {
        collapsingToolbar.setTitle(" ");
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
//                    collapsingToolbar.setTitle("Pizza");
                    collapsingToolbar.setTitle(foodModel.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });

    }

    private void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.arMode){
            startARActivity();
        }
        return super.onOptionsItemSelected(item);
    }

    private void startARActivity(){
        Intent intent = new Intent(this, ARDirection.class);
        intent.putExtra("position", String.valueOf(position));
        startActivity(intent);
    }

    @Override
    public void processFinish(String result) {
        Log.d("fuck2", result);
        try {
            JSONArray jsonArray = new JSONArray(result);
            for (int i=0; i<jsonArray.length(); i++){
                JSONArray object = (JSONArray) jsonArray.get(i);
                String placeName = object.getString(0),
                        address = object.getString(1),
                        addressURL = object.getString(2),
                        cost = object.getString(3);

                double latitude = 0, longitude = 0;

                Log.d("fuck", placeName);
                foodModel.addAddress(address);
                foodModel.addAddressURL(addressURL);
                foodModel.addCost(cost);
                foodModel.addPlaceName(placeName);

                MapCoord coord = new MapCoord(latitude, longitude);
                double lat = 0, log = 0;
                Geocoder geocoder = new Geocoder(this);
                List<Address> addresses;
                try {
                    addresses = geocoder.getFromLocationName(address, 1);
                    if(addresses.size() > 0) {
                        latitude= addresses.get(0).getLatitude();
                        longitude= addresses.get(0).getLongitude();
                        Log.d("Coordinate", latitude + " " + longitude);
                        coord = new MapCoord(latitude, longitude);

                        lat = latitude;
                        log = longitude;
                        Log.d("abcdek", lat + " " + log);


                    }



                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("eeeeeee", "Error");
                }

                PlacesList.add(new Place(placeName, address, cost + ".000 VND", "01264560570", coord, addressURL));
                itemOffset.add(i);

                Log.d("abcder", lat + " " + log);
            }
            StaticData.listSuggest.getListModel().set(position, foodModel);
            abcList = PlacesList;
            adapter.notifyDataSetChanged();
            recycler.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    private class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

        Context mContext;
        private ArrayList<Integer> itemOffset;
        ArrayList<Place> PlaceList;

        public Adapter(Context context, ArrayList<Place> PlaceList, ArrayList<Integer> itemOffset) {
            mContext = context;
            this.PlaceList = PlaceList;
            this.itemOffset = itemOffset;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            int layoutId = R.layout.placelist_item;

            View itemView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
            final ViewHolder viewHolder = new ViewHolder(itemView);

            viewHolder.swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {
                }

                @Override
                public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {
                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {
                }
            });

            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Place item = PlaceList.get(position);
            Picasso.with(mContext).load(item.getImgUrl()).into(holder.placeImage);
            holder.tvAddress.setText(item.getAddress());
            holder.tvCost.setText(item.getPrice());
            holder.tvPlaceName.setText(item.getName());
            holder.swipeLayout.setOffset(itemOffset.get(position));

            holder.leftView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (AccessToken.getCurrentAccessToken() != null) {
                        ShareLinkContent content;
                        content = new ShareLinkContent.Builder()
                                .setContentUrl(Uri.parse(PlaceList.get(position).getImgUrl()))
                                .build();
                        ShareDialog.show((Activity) mContext, content);
                    }
                    else{
                        Toast.makeText(DetailActivity.this, "Please sign in by Facebook account to use this function", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            holder.mapLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MapsActivity.class);
                    intent.putExtra("position", String.valueOf(position));
                    intent.putExtra("map_type", String.valueOf(1));
                    mContext.startActivity(intent);

                }
            });
            holder.smsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromParts("sms", PlaceList.get(position).getPhoneNo(), null));
                    startActivity(intent);
                }
            });
            holder.phoneLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:"+PlaceList.get(position).getPhoneNo()));
                    startActivity(intent);
                }
            });
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            if (holder.getAdapterPosition() != RecyclerView.NO_POSITION) {
                itemOffset.set(holder.getAdapterPosition(), holder.swipeLayout.getOffset());
            }
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            super.onViewRecycled(holder);
        }

        @Override
        public int getItemCount() {
            Log.d("abcderr", String.valueOf(PlacesList.size()));
            return PlaceList.size();
        }

        class ViewHolder extends RecyclerView.ViewHolder {

            private final SwipeLayout swipeLayout;
            private final View rightView;
            private final View leftView;
            private final View mapLayout;
            private final View smsLayout;
            private final View phoneLayout;
            TextView tvPlaceName, tvCost, tvAddress;
            ImageView placeImage;

            ViewHolder(View itemView) {
                super(itemView);
                swipeLayout = (SwipeLayout) itemView.findViewById(R.id.swipe_layout);
                rightView = itemView.findViewById(R.id.right_view);
                leftView = itemView.findViewById(R.id.left_view);
                mapLayout = itemView.findViewById(R.id.map_layout);
                smsLayout = itemView.findViewById(R.id.smsLayout);
                phoneLayout = itemView.findViewById(R.id.phoneLayout);

                tvPlaceName = (TextView) itemView.findViewById(R.id.placeName);
                tvCost = (TextView) itemView.findViewById(R.id.placeCost);
                tvAddress = (TextView) itemView.findViewById(R.id.placeAddress);
                placeImage = (ImageView) itemView.findViewById(R.id.placeImage);
            }
        }
    }

}
