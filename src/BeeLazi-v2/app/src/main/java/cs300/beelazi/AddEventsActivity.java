package cs300.beelazi;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cn.refactor.lib.colordialog.ColorDialog;
import cn.refactor.lib.colordialog.PromptDialog;
import cs300.beelazi.Adapter.EventsAdapter;
import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.Model.TransferEventItem;
import cs300.beelazi.SQLite.SQLiteHelper;
import cs300.beelazi.ServerWidget.RequestToServer;

public class AddEventsActivity extends AppCompatActivity implements RequestToServer.RequestResult{

    // Launches SublimePicker
    RequestToServer requestToServer;
    ImageView ivLaunchPicker;
    ListView listEvents;
    private static EventsAdapter eventsAdapter;
    private static ArrayList<Pair<String, TransferEventItem > >  eventList;
    SQLiteHelper db;
    private int deletePosition;
    JsonHelper jsonHelper;
    String action;
    String table;
    ArrayList<Pair<String,String>> condition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_events);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
            getSupportActionBar().setTitle("Event Management");
        }

        jsonHelper = new JsonHelper();

        // Finish on navigation icon click
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ivLaunchPicker = (ImageView) findViewById(R.id.ivLaunchPicker);
        ivLaunchPicker.setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        ivLaunchPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddEventsActivity.this, EventsActivity.class);
                startActivity(intent);
                finish();
            }
        });
        listEvents = (ListView) findViewById(R.id.listEvents);

//        db = new SQLiteHelper(this);
//        eventList = db.getAllEvents();


        eventList = new ArrayList<>();
        eventsAdapter = new EventsAdapter(this, R.layout.item_events_layout, eventList);
        listEvents.setAdapter(eventsAdapter);


        if (StaticData.user != null) {
            Date cDate = new Date();
            String fDate = new SimpleDateFormat("yyyy-MM-dd").format(cDate);
            action = "WEE";
            table = "Event";
            condition = new ArrayList<>();
            condition.add(new Pair<String, String>("username", StaticData.user.getUsername()));
            condition.add(new Pair<String, String>("date", fDate));
            try {
                String query = jsonHelper.writeQuery(action, table, condition);
                makeQuery(query);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        listEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> parent, View view, final int position, long id) {
                ColorDialog dialog = new ColorDialog(AddEventsActivity.this);
                dialog.setTitle("WARNING");
                dialog.setContentText("Do you really want to delete this activity?" + "\n" + "You cannot restore it later!");
                dialog.setPositiveListener("DELETE", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                        deletePosition = position;

                        Pair<String, TransferEventItem> p = (Pair<String, TransferEventItem>) parent.getItemAtPosition(position);
                        String title = p.first;
                        TransferEventItem item = p.second;
                        String startDate = item.getsYear() + "-" + (item.getsMonth() < 10 ? "0" + item.getsMonth():item.getsMonth()) + "-" + (item.getsDay() < 10 ? "0" + item.getsDay():item.getsDay());
                        String endDate = item.geteYear() + "-" + (item.geteMonth() < 10 ? "0" + item.geteMonth():item.geteMonth()) + "-" + (item.geteDay() < 10 ? "0" + item.geteDay():item.geteDay());
                        String startTime = (item.getsHour() < 10 ? "0" + item.getsHour():item.getsHour()) + ":" + (item.getsMinute() < 10 ? "0" + item.getsMinute():item.getsMinute());
                        String endTime = (item.geteHour() < 10 ? "0" + item.geteHour():item.geteHour()) + ":" + (item.geteMinute() < 10 ? "0" + item.geteMinute():item.geteMinute());

                        action = "DEL";
                        table = "Event";
                        condition = new ArrayList<>();

                        condition.add(new Pair<String, String>("username", StaticData.user.getUsername()));
                        condition.add(new Pair<String, String>("title", title));
                        condition.add(new Pair<String, String>("startdate", startDate));
                        condition.add(new Pair<String, String>("starttime", startTime));
                        condition.add(new Pair<String, String>("enddate", endDate));
                        condition.add(new Pair<String, String>("endtime", endTime));
                        try {
                            String query = jsonHelper.writeQuery(action, table, condition);
                            makeQuery(query);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }).setNegativeListener("CANCEL", new ColorDialog.OnNegativeListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        dialog.dismiss();
                    }
                }).show();
            }
        });
    }

    private void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AsynchronousActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void processFinish(String result) {
        if (result.equals("True") || result.equals("False")){
            if (result.equals("True")){
                new PromptDialog(AddEventsActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_SUCCESS)
                        .setAnimationEnable(true)
                        .setTitleText("SUCCESS")
                        .setContentText("The item has been deleted!")
                        .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
                eventList.remove(deletePosition);
                eventsAdapter.notifyDataSetChanged();
            }
            else {
                new PromptDialog(AddEventsActivity.this)
                        .setDialogType(PromptDialog.DIALOG_TYPE_WRONG)
                        .setAnimationEnable(true)
                        .setTitleText("ERROR")
                        .setContentText("Get Error when deleting data!")
                        .setPositiveListener(getString(R.string.ok), new PromptDialog.OnPositiveListener() {
                            @Override
                            public void onClick(PromptDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        }
        else {
            eventList.clear();
            try {
                JSONArray jsonArray = new JSONArray(result);
                SQLiteHelper db = new SQLiteHelper(this);
                db.deleteAllEvents();
                ArrayList<Pair<String, TransferEventItem>> item1 = db.getAllEvents();
                Log.d("fuckfuckfuckfuck", String.valueOf(item1.size()));
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object = (JSONObject) jsonArray.get(i);
                    String title = object.getString("title");
                    String startDate = object.getString("startdate");
                    String startTime = object.getString("starttime");
                    String endDate = object.getString("enddate");
                    String endTime = object.getString("endtime");

                    int sYear = Integer.parseInt(startDate.substring(0, 4)),
                            sMonth = Integer.parseInt(startDate.substring(5, 7)),
                            sDay = Integer.parseInt(startDate.substring(8, 10)),
                            eYear = Integer.parseInt(endDate.substring(0, 4)),
                            eMonth = Integer.parseInt(endDate.substring(5, 7)),
                            eDay = Integer.parseInt(endDate.substring(8, 10));

                    int sHour = Integer.parseInt(startTime.substring(0, 2)),
                            sMinute = Integer.parseInt(startTime.substring(3, 5)),
                            eHour = Integer.parseInt(endTime.substring(0, 2)),
                            eMinute = Integer.parseInt(endTime.substring(3, 5));

                    TransferEventItem item = new TransferEventItem(sHour, sMinute, sDay, sMonth, sYear, eHour, eMinute, eDay, eMonth, eYear);

                    eventList.add(new Pair<String, TransferEventItem>(title, item));

                    db.insertEvents(i, title, item);
                }
                eventsAdapter.notifyDataSetChanged();
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
