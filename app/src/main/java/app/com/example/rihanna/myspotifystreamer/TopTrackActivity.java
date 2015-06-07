package app.com.example.rihanna.myspotifystreamer;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import app.com.example.rihanna.myspotifystreamer.utils.TrackInfo;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyError;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.Track;
import kaaes.spotify.webapi.android.models.Tracks;
import retrofit.RetrofitError;



public class TopTrackActivity extends ActionBarActivity {
    String artistID;
    String artistName;
    TopTrackFragment fragment=new TopTrackFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        artistID = intent.getStringExtra("idArtist");
        artistName=intent.getStringExtra("nameArtist");

        /*Costumize action bar adding artist name */
        ActionBar actionBar = getSupportActionBar();
        actionBar.setSubtitle(artistName);

        setContentView(R.layout.activity_top_track);

       // fragment=new TopTrackFragment();
        Bundle bundle=new Bundle();
        bundle.putString("idArtist",artistID);
        bundle.putString("nameArtist",artistName);

        if (savedInstanceState == null) {
            fragment=new TopTrackFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container,fragment)
                    .commit();
        }

        //Restore the fragment's instance
        if (savedInstanceState != null){
            fragment = (TopTrackFragment) getSupportFragmentManager().getFragment(
                savedInstanceState, "mContent");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_top_track, menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        return true;
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "mContent",fragment);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id==android.R.id.home){
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
