package cs300.beelazi;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import cs300.beelazi.Adapter.CardSlideAdapter;
import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.ServerWidget.RequestToServer;

public class AdjustMovieFlavorActivity extends AppCompatActivity implements RequestToServer.RequestResult {

    String[] filmTitle = {"Seriesornot", "Duration", "Action", "Comedy", "Horror",
                "FamilySocialCommunity", "Romantic", "ScienceFiction", "Documentary", "Classic", "ChildAnimation", "Time"};

    String[] title = {"Series or Not", "Duration", "Action", "Comedy", "Horror",
            "Family/Social/Community", "Romantic", "Science Fiction", "Documentary", "Classic", "Child Animation", "Time"};


    RequestToServer requestToServer;

    ArrayList<Pair<String,String>> list;
    CardSlideAdapter adapter;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_movie_flavor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
            getSupportActionBar().setTitle("Movie Flavor Adjustion");
        }
        initLayout();
    }

    private void initLayout() {
        listView = (ListView) findViewById(R.id.listFilmFlavor);
        list = new ArrayList<>();
        adapter = new CardSlideAdapter(this, R.layout.item_cardslider, list);
        listView.setAdapter(adapter);

        JsonHelper jsonHelper = new JsonHelper();
        String action = "SEL";
        String table = "Favorite";
        ArrayList<Pair<String,String>> condition = new ArrayList<>();
        condition.add(new Pair<String, String>("username", StaticData.user.getUsername()));
        try {
            String query = jsonHelper.writeQuery(action, table, condition);
            makeQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choice, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.yes) {
            //Save to Database
            JsonHelper jsonHelper = new JsonHelper();
            String action = "UPD";
            String table = "Favorite";
            ArrayList<Pair<String, String>> condition1 = new ArrayList<>(), condition2 = new ArrayList<>();
            condition1.add(new Pair<String, String>("username", StaticData.user.getUsername()));
            for (int i=0; i<filmTitle.length; i++){
                float val = Float.parseFloat(adapter.getItem(i).second) * 100;
                int mVal = Math.round(val);
                if (mVal > 100){
                    mVal/= 100;
                }
                condition2.add(new Pair<String, String>(filmTitle[i], String.valueOf(mVal)));
            }
            try {
                String query = jsonHelper.writeQuery(action, table, condition1, condition2);
                makeQuery(query);
            } catch (IOException e) {
                e.printStackTrace();
            }
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinish(String result) {
        if (!result.equals("True") && !result.equals("False")){
            try {
                JSONArray jsonArray = new JSONArray(result);
                JSONArray object = (JSONArray) jsonArray.get(0);
                for (int i=0; i<title.length; i++){
                    Pair<String, String> item = new Pair<>(title[i], object.getString(i));
                    list.add(item);
                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        else{
            if (result.equals("True")){
                Toast.makeText(this, "Update successfully", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
            else Toast.makeText(this, "Get error when updating", Toast.LENGTH_SHORT).show();
        }
    }
}
