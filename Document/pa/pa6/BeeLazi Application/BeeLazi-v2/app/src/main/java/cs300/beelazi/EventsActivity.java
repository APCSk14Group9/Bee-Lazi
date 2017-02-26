package cs300.beelazi;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.appeaser.sublimepickerlibrary.datepicker.SelectedDate;
import com.appeaser.sublimepickerlibrary.helpers.SublimeOptions;
import com.appeaser.sublimepickerlibrary.recurrencepicker.SublimeRecurrencePicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import cs300.beelazi.DatePicker.SublimePickerFragment;
import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.EventItem;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.ServerWidget.RequestToServer;

public class EventsActivity extends AppCompatActivity implements RequestToServer.RequestResult{

    RequestToServer requestToServer;
    Button btnStartDate, btnEndDate;
    TextView tvStartDate, tvEndDate;
    EditText etTitle;
    EventItem startTime, endTime;
    int state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.black)));
        getSupportActionBar().setTitle("Event Adding");
        initLayouts();
        setEvents();
    }

    private void setEvents() {
        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // DialogFragment to host SublimePicker
                state = 0;
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                state = 1;
                SublimePickerFragment pickerFrag = new SublimePickerFragment();
                pickerFrag.setCallback(mFragmentCallback);

                // Options
                Pair<Boolean, SublimeOptions> optionsPair = getOptions();

                // Valid options
                Bundle bundle = new Bundle();
                bundle.putParcelable("SUBLIME_OPTIONS", optionsPair.second);
                pickerFrag.setArguments(bundle);

                pickerFrag.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
                pickerFrag.show(getSupportFragmentManager(), "SUBLIME_PICKER");
            }
        });
    }

    private void initLayouts() {
        btnStartDate = (Button) findViewById(R.id.chooseSDate);
        btnEndDate = (Button) findViewById(R.id.chooseEDate);
        tvStartDate = (TextView) findViewById(R.id.tvStartDate);
        tvEndDate = (TextView) findViewById(R.id.tvEndDate);
        etTitle = (EditText) findViewById(R.id.chooseTitle);
    }

    SublimePickerFragment.Callback mFragmentCallback = new SublimePickerFragment.Callback() {
        @Override
        public void onCancelled() {
        }

        @Override
        public void onDateTimeRecurrenceSet(SelectedDate selectedDate,
                                            int hourOfDay, int minute,
                                            SublimeRecurrencePicker.RecurrenceOption recurrenceOption,
                                            String recurrenceRule) {

            SelectedDate mSelectedDate = selectedDate;
            int mHour = hourOfDay;
            int mMinute = minute;
            if (mSelectedDate != null){
                if (state == 0){
                    startTime = new EventItem(mHour, mMinute, mSelectedDate);
                    if (endTime != null){
                        if (!validate(startTime, endTime)){
                            Toast.makeText(EventsActivity.this, "Invalid Start Time", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String date = mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH) + "/" +
                                    (mSelectedDate.getStartDate().get(Calendar.MONTH)+1) + "/" +
                                    mSelectedDate.getStartDate().get(Calendar.YEAR) + " " +
                                    mHour + ":" + mMinute;
                            tvStartDate.setText(date);
                        }
                    }
                    else{
                        String date = mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH) + "/" +
                                (mSelectedDate.getStartDate().get(Calendar.MONTH)+1) + "/" +
                                mSelectedDate.getStartDate().get(Calendar.YEAR) + " " +
                                mHour + ":" + mMinute;
                        tvStartDate.setText(date);
                    }
                }
                else if (state == 1){
                    endTime = new EventItem(mHour, mMinute, mSelectedDate);
                    if (startTime != null){
                        if (!validate(startTime, endTime)){
                            Toast.makeText(EventsActivity.this, "Invalid End Time", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String date = mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH) + "/" +
                                    (mSelectedDate.getStartDate().get(Calendar.MONTH)+1) + "/" +
                                    mSelectedDate.getStartDate().get(Calendar.YEAR) + " " +
                                    mHour + ":" + mMinute;
                            tvEndDate.setText(date);
                        }
                    }
                    else{
                        String date = mSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH) + "/" +
                                (mSelectedDate.getStartDate().get(Calendar.MONTH)+1) + "/" +
                                mSelectedDate.getStartDate().get(Calendar.YEAR) + " " +
                                mHour + ":" + mMinute;
                        tvEndDate.setText(date);
                    }
                }
                state = -1;
            }
        }
    };

    private boolean validate(EventItem first, EventItem second) {
        if (first.getSelectedDate() == null || second.getSelectedDate() == null) return true;
        int fyear, fmonth, fday, fHour, fMinute;
        SelectedDate fSelectedDate = first.getSelectedDate();
        fyear = fSelectedDate.getStartDate().get(Calendar.YEAR);
        fmonth = fSelectedDate.getStartDate().get(Calendar.MONTH);
        fday = fSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH);
        fHour = first.getmHour();
        fMinute = first.getmMinute();

        int syear, smonth, sday, sHour, sMinute;
        SelectedDate sSelectedDate = second.getSelectedDate();
        syear = sSelectedDate.getStartDate().get(Calendar.YEAR);
        smonth = sSelectedDate.getStartDate().get(Calendar.MONTH);
        sday = sSelectedDate.getStartDate().get(Calendar.DAY_OF_MONTH);
        sHour = second.getmHour();
        sMinute = second.getmMinute();

        if (syear == fyear){
            if (fmonth == smonth){
                if (fday == sday){
                    if (sHour == fHour){
                        if (fMinute <= sMinute) return true;
                    }
                    else if (fHour < sHour) return true;
                }
                else if (fday < sday) return true;
            }
            else if (fmonth < smonth) return true;
        }
        else if (fyear < syear) return true;
        return false;
    }

    // Validates & returns SublimePicker options
    Pair<Boolean, SublimeOptions> getOptions() {
        SublimeOptions options = new SublimeOptions();
        int displayOptions = SublimeOptions.ACTIVATE_DATE_PICKER | SublimeOptions.ACTIVATE_TIME_PICKER;
        options.setPickerToShow(SublimeOptions.Picker.DATE_PICKER);
//        options.setPickerToShow(SublimeOptions.Picker.TIME_PICKER);

        // Enable/disable the date range selection feature
        options.setCanPickDateRange(false);

        // Example for setting date range:
        // Note that you can pass a date range as the initial date params
        // even if you have date-range selection disabled. In this case,
        // the user WILL be able to change date-range using the header
        // TextViews, but not using long-press.

        options.setDisplayOptions(displayOptions);

        // If 'displayOptions' is zero, the chosen options are not valid
        return new Pair<>(Boolean.TRUE , options);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_choice, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.yes){
            if (startTime != null && endTime != null && etTitle.getText().length() > 0){
                SelectedDate startDate = startTime.getSelectedDate(),
                        endDate = endTime.getSelectedDate();

                int startDay = startDate.getStartDate().get(Calendar.DAY_OF_MONTH),
                        startMonth = startDate.getStartDate().get(Calendar.MONTH)+1,
                        startYear = startDate.getStartDate().get(Calendar.YEAR),
                        startHour = startTime.getmHour(),
                        startMinute = startTime.getmMinute();

                int endDay = endDate.getStartDate().get(Calendar.DAY_OF_MONTH),
                        endMonth = endDate.getStartDate().get(Calendar.MONTH)+1,
                        endYear = endDate.getStartDate().get(Calendar.YEAR),
                        endHour = endTime.getmHour(),
                        endMinute = endTime.getmMinute();


                String sDate = startYear + "-" + (startMonth < 10 ? "0" + startMonth:startMonth) + "-" + (startDay < 10 ? "0" + startDay:startDay);
                String eDate = endYear + "-" + (endMonth < 10 ? "0" + endMonth:endMonth) + "-" + (endDay < 10 ? "0" + endDay:endDay);
                String sTime = (startHour < 10 ? "0" + startHour:startHour) + ":" + (startMinute < 10 ? "0" + startMinute:startMinute);
                String eTime = (endHour < 10 ? "0" + endHour:endHour) + ":" + (endMinute < 10 ? "0" + endMinute:endMinute);

                JsonHelper jsonHelper = new JsonHelper();
                String action = "INS";
                String table = "Event";
                ArrayList<Pair<String,String>> conditions = new ArrayList<>();
                conditions.add(new Pair<String, String>("username", StaticData.user.getUsername()));
                conditions.add(new Pair<String, String>("title", etTitle.getText().toString()));
                conditions.add(new Pair<String, String>("startdate", sDate));
                conditions.add(new Pair<String, String>("starttime", sTime));
                conditions.add(new Pair<String, String>("enddate", eDate));
                conditions.add(new Pair<String, String>("endtime", eTime));
                try {
                    String query = jsonHelper.writeQuery(action, table, conditions);
                    makeQuery(query);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }
        }
        return false;
    }

    private void makeQuery(String query) {
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AddEventsActivity.class);
        startActivity(intent);
        super.onBackPressed();
    }

    @Override
    public void processFinish(String result) {
        if (result.equals("True")) {
            Intent intent = new Intent(this, AddEventsActivity.class);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Your insertion is not completed", Toast.LENGTH_SHORT).show();
        }
    }
}
