package cs300.beelazi.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import cs300.beelazi.Model.TransferEventItem;
import cs300.beelazi.R;


public class EventsAdapter extends ArrayAdapter<Pair<String,TransferEventItem>>{

    Context mContext;
    int resource;
    ArrayList<Pair<String,TransferEventItem>> listEvents;
    TextView tvStartDate, tvTitle, tvEndDate, tvStartTime, tvEndTime;

    public EventsAdapter(Context context, int resource, ArrayList<Pair<String,TransferEventItem>> objects) {
        super(context, resource, objects);
        mContext = context;
        this.resource = resource;
        listEvents = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(resource, parent, false);
        }
        Pair<String,TransferEventItem> pItem = listEvents.get(position);
        String title = pItem.first;
        TransferEventItem fItem = pItem.second;
        String startDate = "FROM: " + fItem.getsDay() + "/" + fItem.getsMonth() + "/" + fItem.getsYear();
        String endDate = "TO: " + fItem.geteDay() + "/" + fItem.geteMonth() + "/" + fItem.geteYear();

        tvStartDate = (TextView) convertView.findViewById(R.id.tvStartDate);
        tvStartTime = (TextView) convertView.findViewById(R.id.tvStartTime);
        tvEndTime = (TextView) convertView.findViewById(R.id.tvEndTime);
        tvEndDate = (TextView) convertView.findViewById(R.id.tvToDate);
        tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);

        tvTitle.setText(title);
        tvStartDate.setText(startDate);
        tvStartTime.setText(getTransformDateValue(fItem.getsHour(), fItem.getsMinute()));
        tvEndDate.setText(endDate);
        tvEndTime.setText(getTransformDateValue(fItem.geteHour(), fItem.geteMinute()));

        return convertView;
    }


    private String getTransformDateValue(int fHour, int fMinute) {
        String minute = (fMinute < 10 ? ":0"+fMinute : ":"+fMinute);
        return (fHour > 12 ? (fHour-12) + minute + " PM":fHour + minute + " AM");
    }


}
