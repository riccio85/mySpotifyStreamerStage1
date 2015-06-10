package app.com.example.rihanna.myspotifystreamer;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import app.com.example.rihanna.myspotifystreamer.adapters.TopTrackAdapter;
import app.com.example.rihanna.myspotifystreamer.utils.*;
import kaaes.spotify.webapi.android.SpotifyApi;
import kaaes.spotify.webapi.android.SpotifyService;
import kaaes.spotify.webapi.android.models.*;
import kaaes.spotify.webapi.android.models.Track;
/**
 * Created by Rihanna on 03/06/2015.
 */
public class TopTrackFragment extends Fragment implements AdapterView.OnItemClickListener  {

    private final String SELECTED_KEY="mPosition";
    private final String LOCAL_LIST="myList";
    private final String MY_COUNTRY="IT";

    int mPosition=ListView.INVALID_POSITION;
    String artistID;
    String artistName;
    List<Track> topTracks;
    List<TrackInfo> myTrackList=new ArrayList<TrackInfo>();
    Context context;
    ListView mListView;
    ImageView mImageView;
    TopTrackAdapter trackAdapter;

    public TopTrackFragment()  {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            artistID = getArguments().getString("idArtist");
            artistName = getArguments().getString("nameArtist");
            context = getActivity();
            View rootView = inflater.inflate(R.layout.fragment_top_track, container, false);
            mImageView = (ImageView) rootView.findViewById(R.id.noTrack);
            mListView = (ListView) rootView.findViewById(R.id.track_list);
            trackAdapter = new TopTrackAdapter(context);
            mListView.setAdapter(trackAdapter);
            mListView.setOnItemClickListener(this);
            bindMyView();
        return rootView;
    }


    public void bindMyView() {
        TopTrackAsyncTask artists=new TopTrackAsyncTask();
        artists.execute(artistID);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPosition != ListView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        outState.putParcelableArrayList(LOCAL_LIST, (ArrayList<? extends android.os.Parcelable>) myTrackList);
     }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
               myTrackList=savedInstanceState.getParcelableArrayList(LOCAL_LIST);
               trackAdapter.addAll(myTrackList);
            // Restore last state for checked position
               if(savedInstanceState.getInt(SELECTED_KEY) != ListView.INVALID_POSITION){
                    mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(myTrackList!=null){
            trackAdapter.addAll(myTrackList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
            /*highlight the selected list item */
        view.getFocusables(position);
        view.setSelected(true);
        Toast.makeText(getActivity(), "selected item "+(position+1), Toast.LENGTH_SHORT).show();
    }

    class TopTrackAsyncTask extends AsyncTask<String, Void, List<Track>> {
        @Override
        protected  List<Track> doInBackground(String... strings) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(spotify.COUNTRY, MY_COUNTRY);
            Tracks tracks = spotify.getArtistTopTrack(strings[0], options);
            return tracks.tracks;
        }

        @Override
        protected void onPostExecute(List<Track> result) {
            topTracks=result;
            if(result != null) {
                /*Saving in myTrackList the parcelable local arrayList */
                TrackInfo mine = new TrackInfo();
                myTrackList = mine.filterTrackList(artistName,result);
                trackAdapter.clear();
                trackAdapter.addAll(myTrackList);
            }
            if(result.size() == 0){
               trackAdapter.clear();
               mImageView.setVisibility(View.VISIBLE);
            }
        }


    }
}//end fragment
