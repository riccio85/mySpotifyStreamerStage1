package app.com.example.rihanna.myspotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import app.com.example.rihanna.myspotifystreamer.adapters.SearchAdapter;
import kaaes.spotify.webapi.android.*;
import kaaes.spotify.webapi.android.models.*;


public class SearchActivity extends ActionBarActivity{
    private final String INPUT_STATUS="input";

    EditText edited;
    Pager<Artist> artistList;
    Context context;
    ListView mListView;
    String query;
    SearchAdapter artistAdapter;
    ArtistSearch artists;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

            setContentView(R.layout.activity_search);
            context = this;
            edited = (EditText) findViewById(R.id.search_text);
            mListView = (ListView) findViewById(R.id.artist_list);
            artistAdapter = new SearchAdapter(context);
            mListView.setAdapter(artistAdapter);
            t=Toast.makeText(context, "Sorry no artist with this name. Refine search!", Toast.LENGTH_SHORT);
            t.setGravity(Gravity.TOP|Gravity.CENTER, 0, 350);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (artistList != null) {
                        Artist art = artistList.items.get(position);
                        Intent intent = new Intent(context, TopTrackActivity.class);
                        intent.putExtra("idArtist", art.id);
                        intent.putExtra("nameArtist", art.name);
                        startActivity(intent);
                    }
                }
            });
        if(checkInternetConnection()) {
            edited.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    artistAdapter.clear();
                    mListView.clearChoices();//clear list view choices for not showing selected case new search
                    if (charSequence.length() > 0) {
                        artists = new ArtistSearch();
                        query = charSequence.toString();
                        artists.execute(query);
                        try {
                            artists.get(500, TimeUnit.MILLISECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (TimeoutException e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }else{
            Toast.makeText(this,"You don't have internet connection Retry Later",Toast.LENGTH_SHORT).show();
        }


    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(INPUT_STATUS,query);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
       /* if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }
    public boolean checkInternetConnection(){
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    class ArtistSearch extends AsyncTask<String, Void,  Pager<Artist>>  {
        @Override
        protected  Pager<Artist> doInBackground(String... strings) {
            Pager<Artist> response;
            SpotifyApi api = new SpotifyApi();
            try {
                api.setAccessToken(null); // i don't need authoraisation
                SpotifyService spotify = api.getService();
                ArtistsPager artists = spotify.searchArtists(strings[0]);
                if (artists != null && artists.artists.total > 0 ) {
                    response = artists.artists;
                }else{ //artists.artists.total == 0
                    return null;
                }
            }catch(Exception e) {
                    return null;
            }
                return response;
        }

        @Override
        protected void onPostExecute(Pager<Artist> result) {
            if(result != null && result.total>0){
                t.cancel();
                artistList=result;
                artistAdapter.clear();
                artistAdapter.addAll(result.items);
            }
          if(result == null || result.total == 0 ){
                t.show();
                artistAdapter.clear();
            }

        }

   }
}
