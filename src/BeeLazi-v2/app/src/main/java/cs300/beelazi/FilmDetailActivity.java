package cs300.beelazi;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cs300.beelazi.Model.FilmModel;
import cs300.beelazi.Model.StaticData;

public class FilmDetailActivity extends AppCompatActivity {

    private CollapsingToolbarLayout collapsingToolbar;
    private ImageView userCoverView;
    private FilmModel filmModel;
    private AppBarLayout appBarLayout;
    TextView animeTitle, numEps, tvDescription;
    Button playButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_film_detail);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        int position = Integer.parseInt(getIntent().getStringExtra("position"));
        Log.d("tudeptrai", String.valueOf(position));
        filmModel = (FilmModel) StaticData.listSuggest.getListModel().get(position);
        initLayouts();
        initListeners();
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
                    collapsingToolbar.setTitle(filmModel.getName());
                    isShow = true;
                } else if (isShow) {
                    collapsingToolbar.setTitle(" ");//carefull there should a space between double quote otherwise it wont work
                    isShow = false;
                }
            }
        });
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilmDetailActivity.this, WatchMovieActivity.class);
                intent.putExtra("filmPath", StaticData.filmLink + filmModel.getFilmURL());
                startActivity(intent);
            }
        });
    }

    private void initLayouts() {
        collapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        animeTitle = (TextView) findViewById(R.id.animeTitle);
        tvDescription = (TextView) findViewById(R.id.animeDescription);
        numEps = (TextView) findViewById(R.id.episode);
        userCoverView = (ImageView) findViewById(R.id.anime_cover);
        playButton = (Button) findViewById(R.id.playButton);
        Picasso.with(this).load(filmModel.getImgURL()).into(userCoverView);
        animeTitle.setText(filmModel.getName());
        tvDescription.setText(filmModel.getDescription());
        numEps.setText(filmModel.getDuration() + " minutes");

    }
}
