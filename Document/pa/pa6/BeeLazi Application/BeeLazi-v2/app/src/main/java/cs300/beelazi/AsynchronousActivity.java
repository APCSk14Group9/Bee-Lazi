package cs300.beelazi;

import android.util.Log;
import android.util.Pair;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cs300.beelazi.Model.Event;
import cs300.beelazi.Model.TransferEventItem;
import cs300.beelazi.SQLite.SQLiteHelper;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AsynchronousActivity extends BaseActivity implements Callback<List<Event>> {

    private List<WeekViewEvent> events = new ArrayList<>();
    String[] color = {
            "#F4D03F",
            "#58D68D",
            "#E95B2E"
    };

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {

        // Download events from network if it hasn't been done already. To understand how events are
//         downloaded using retrofit, visit http://square.github.io/retrofit
        // Return only the events that matches newYear and newMonth.

//        SQLiteHelper db = new SQLiteHelper(this);
//        addEvent(db.getAllEvents());
//        db.close();

        List<WeekViewEvent> matchedEvents = new ArrayList<WeekViewEvent>();


        for (WeekViewEvent event : events) {
            if (eventMatches(event, newYear, newMonth)) {
                event.setColor(R.color.month_title_color);
//                curColor = (curColor + 1)%3;
             matchedEvents.add(event);
             }
        }
        return matchedEvents;
    }

    public void addEvent(ArrayList<Pair<String,TransferEventItem>> list){
        SQLiteHelper db = new SQLiteHelper(this);
        ArrayList<Pair<String,TransferEventItem>> eventList = db.getAllEvents();
        for (int i=0; i<eventList.size(); i++){
            TransferEventItem item = eventList.get(i).second;
            String title = eventList.get(i).first;
            events.add(new WeekViewEvent(item.getId(), title, item.getsYear(), item.getsMonth()+1, item.getsDay(),
                    item.getsHour(), item.getsMinute(), item.geteYear(), item.geteMonth()+1, item.geteDay(),
                    item.geteHour(), item.geteMinute()));
        }
        db.close();
        Log.d("tututut", String.valueOf(events.size()));

    }


    /**
     * Checks if an event falls into a specific year and month.
     * @param event The event to check for.
     * @param year The year.
     * @param month The month.
     * @return True if the event matches the year and month.
     */
    private boolean eventMatches(WeekViewEvent event, int year, int month) {
        return (event.getStartTime().get(Calendar.YEAR) == year && event.getStartTime().get(Calendar.MONTH) == month - 1) || (event.getEndTime().get(Calendar.YEAR) == year && event.getEndTime().get(Calendar.MONTH) == month - 1);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void success(List<Event> events, Response response) {
        this.events.clear();
        for (Event event : events) {
            this.events.add(event.toWeekViewEvent());
        }
        getWeekView().notifyDatasetChanged();
    }

    @Override
    public void failure(RetrofitError error) {
        error.printStackTrace();
    }
}
