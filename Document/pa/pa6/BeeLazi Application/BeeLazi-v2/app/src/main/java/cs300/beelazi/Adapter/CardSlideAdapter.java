package cs300.beelazi.Adapter;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.rey.material.widget.Slider;

import java.util.ArrayList;

import cs300.beelazi.R;


public class CardSlideAdapter extends ArrayAdapter<Pair<String,String>> {

    ArrayList<Pair<String,String>> list;
    Context context;
    int res;


    public CardSlideAdapter(Context context, int resource, ArrayList<Pair<String,String>> objects) {
        super(context, resource, objects);
        this.context = context;
        list = objects;
        res = resource;
    }

    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(res, parent, false);
        }

        final Pair<String,String> item = list.get(position);
        String tvTitle = item.first;
        String slValue = item.second;

        TextView tv = (TextView) convertView.findViewById(R.id.tvItem);
        Slider slider = (Slider) convertView.findViewById(R.id.slItem);

        slider.setOnPositionChangeListener(new Slider.OnPositionChangeListener() {
            @Override
            public void onPositionChanged(Slider view, boolean fromUser, float oldPos, float newPos, int oldValue, int newValue) {
                list.set(position, new Pair<String, String>(item.first, (String) String.valueOf(newPos)));
            }
        });

        tv.setText(tvTitle);
        float val = Float.parseFloat(slValue);
        if (val > 1){
            val/=100;
        }
        slider.setPosition(val , false);


        return convertView;
    }
}
