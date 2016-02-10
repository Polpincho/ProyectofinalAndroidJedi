package com.example.polpincho.proyectofinal; /**
 * Created by polpincho on 02/02/2016.
 */
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class PagerHolder extends AppCompatActivity implements OnFragmentInteractionListener {

    ViewPager viewPager;
    FragmentManager fm;
    PagerAdapter pa;
    String tags[] = new String[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pager_holder);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_page_holder);
        setSupportActionBar(toolbar);
        setTitle("Memory");

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        fm = getSupportFragmentManager();
        pa = new PagerAdapter(fm,PagerHolder.this);
        viewPager.setAdapter(pa);

        // Give the TabLayout the ViewPager (material)
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setTabTextColors(Color.parseColor("#8b8b8b"), Color.WHITE);
        tabLayout.setupWithViewPager(viewPager);
    }

    public void onFragmentInteraction(String text, Integer from) {

        if (from == 4 && text.equals("end")){
            Intent i = new Intent(getApplicationContext(), InMenu.class);
            startActivity(i);
            finish();
        }
        if (from == 5){

            Log.e("reload","pre");
            Fragment frg = getSupportFragmentManager().findFragmentByTag(tags[1]);
            final FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.detach(frg);
            ft.attach(frg);
            ft.commit();
            Log.e("reload","post");
        }
        if (from == 1) {
            tags[0]=text;
        }
        if (from == 2){
            tags[1]=text;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_pager_holder, menu);
        return true;
    }

}
