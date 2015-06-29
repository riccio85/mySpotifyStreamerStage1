package app.com.example.rihanna.myspotifystreamer;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;

import java.util.ArrayList;
import java.util.List;

import app.com.example.rihanna.myspotifystreamer.utils.MyTracks;
import app.com.example.rihanna.myspotifystreamer.utils.TrackInfo;


public class PlayerActivity extends ActionBarActivity {

    List<MyTracks> trackList;
    String artist;
    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(artist);


        Intent intent = getIntent();
        trackList=intent.getParcelableArrayListExtra("TrackList");
        artist=intent.getStringExtra("artistName");
        position=intent.getIntExtra("position", -1);

        PlayerFragment fragment=new PlayerFragment();

        Bundle bundle=new Bundle();
        bundle.putParcelableArrayList("TrackList", (ArrayList<? extends Parcelable>) trackList);
        bundle.putString("artistName", artist);
        bundle.putInt("position", position);

        if (savedInstanceState == null) {
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                           .replace(R.id.containerPlay, fragment).commit();

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_player, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            finish();
            return true;

        }
        if(id==android.R.id.home){
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
