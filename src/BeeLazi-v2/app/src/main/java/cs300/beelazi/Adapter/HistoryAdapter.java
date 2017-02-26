package cs300.beelazi.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import cs300.beelazi.DetailActivity;
import cs300.beelazi.FilmDetailActivity;
import cs300.beelazi.Model.History;
import cs300.beelazi.R;


public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final ArrayList<History> historyList;
    Context mContext;

    public HistoryAdapter(Context mContext, ArrayList<History> historyList) {
        this.mContext = mContext;
        this.historyList = historyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.history_item_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.hisName.setText(historyList.get(position).getName());
        Picasso.with(mContext).load(historyList.get(position).getImgUrl()).centerCrop().fit().into(holder.hisImage);
        holder.hisImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (historyList.get(position).getType().equals("Food")) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    mContext.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(mContext, FilmDetailActivity.class);
                    intent.putExtra("position", historyList.get(position).getCardPosition());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView hisName;
        ImageView hisImage;

        public ViewHolder(final View itemView) {
            super(itemView);
            hisImage = (ImageView) itemView.findViewById(R.id.his_Image);
            hisName = (TextView) itemView.findViewById(R.id.his_Name);
        }
    }
}