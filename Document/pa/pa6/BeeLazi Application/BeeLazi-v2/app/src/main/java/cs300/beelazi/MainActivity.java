package cs300.beelazi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

import cs300.beelazi.CardUI.CardFragment;
import cs300.beelazi.CustomFragment.HistoryFragment;
import cs300.beelazi.CustomFragment.PersonalFragment;
import cs300.beelazi.TabViewPager.ViewPagerAdapter;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private TabLayout tabLayout;
    private int[] tabIcons = {
            R.mipmap.ic_fire,
            R.mipmap.ic_beer,
            R.mipmap.ic_face
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_main);

        initLayout();
    }

    private void initLayout() {
        initViewPager();
        initTabLayout();
    }

    private void initTabLayout() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setupTabIcons();
    }

    private void setupTabIcons() {
        for (int i = 0; i < tabIcons.length; i++){
            tabLayout.getTabAt(i).setIcon(tabIcons[i]);
        }
    }

    private void initViewPager() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new HistoryFragment());
        adapter.addFrag(new CardFragment());
        adapter.addFrag(new PersonalFragment());
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(1);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}