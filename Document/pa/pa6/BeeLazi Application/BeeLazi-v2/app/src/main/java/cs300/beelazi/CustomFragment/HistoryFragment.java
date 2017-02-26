package cs300.beelazi.CustomFragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cleveroad.pulltorefresh.firework.FireworkyPullToRefreshLayout;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

import cs300.beelazi.Adapter.HistoryAdapter;
import cs300.beelazi.JSONHelper.JsonHelper;
import cs300.beelazi.Model.History;
import cs300.beelazi.Model.StaticData;
import cs300.beelazi.R;
import cs300.beelazi.ServerWidget.RequestToServer;


public class HistoryFragment extends Fragment implements RequestToServer.RequestResult{

    private static final int REFRESH_DELAY = 4500;

    Context mContext;
    private View clickEvents;
    private RecyclerView rvHistory;
    private ArrayList<History> HistoryList;
    private RequestToServer requestToServer;
    private HistoryAdapter adapter;

    FireworkyPullToRefreshLayout mPullRefreshView;
    private boolean mIsRefreshing;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.history_fragment, container, false);
        mContext = rootView.getContext();
        initLayouts(rootView);
        initFireWorksLayout(rootView);
        initListeners(rootView);
        loadData();
        return rootView;
    }

    private void loadData() {
        JsonHelper jsonHelper = new JsonHelper();
        String action = "SEL";
        String table = "History";
        ArrayList<Pair<String, String>> condition = new ArrayList<>();
        condition.add(new Pair("username", StaticData.user.getUsername()));
        try {
            String query = jsonHelper.writeQuery(action, table, condition);
            makeQuery(query);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initFireWorksLayout(View rootView){
        mPullRefreshView = (FireworkyPullToRefreshLayout) rootView.findViewById(R.id.pullToRefresh);
        mPullRefreshView.setOnRefreshListener(new FireworkyPullToRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mIsRefreshing = true;
                mPullRefreshView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mPullRefreshView.setRefreshing(mIsRefreshing = false);
                        loadData();
                    }
                }, REFRESH_DELAY);
            }
        });
    }

    private void initListeners(View rootView) {

    }

    private void initLayouts(View rootView) {
        rvHistory = (RecyclerView) rootView.findViewById(R.id.rv_history);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(rootView.getContext(), 2);
        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(gridLayoutManager);
        HistoryList = new ArrayList<>();
        adapter = new HistoryAdapter(mContext, HistoryList);
        rvHistory.setAdapter(adapter);
    }

    private void makeQuery(String query){
        requestToServer = new RequestToServer();
        requestToServer.delegate = this;
        requestToServer.execute(query, StaticData.serverLink);
    }

    @Override
    public void processFinish(String result) {

        try {
            JSONArray jsonArray = new JSONArray(result);
            HistoryList.clear();
            for (int i=0; i<jsonArray.length(); i++){
                JSONArray object = (JSONArray) jsonArray.get(i);
                String itemName = object.getString(0);
                String imagePath = object.getString(1);
                String itemType = object.getString(2);
                String itemPosition = object.getString(3);
                HistoryList.add(new History(itemName, imagePath, itemType, itemPosition));
            }
            adapter.notifyDataSetChanged();

        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Set adapter

    }

    @Override
    public void onResume() {
        super.onResume();
        loadData();

    }

    @Override
    public void onStart() {
        super.onStart();
        loadData();
    }
}