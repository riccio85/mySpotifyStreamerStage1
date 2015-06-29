package app.com.example.rihanna.myspotifystreamer;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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
    private static final String SEARCH_TAG = "SEARCHTAG";

    int mPosition=ListView.INVALID_POSITION;
    String artistID;
    String artistName;
    List<MyTracks> topTracks;
    Context context;
    ListView mListView;
    ImageView mImageView;
    TopTrackAdapter trackAdapter;
    boolean twoPane;
    private List<MyTracks> tracksList = new ArrayList<>();

    public TopTrackFragment()  {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            artistID = getArguments().getString("idArtist");
            artistName = getArguments().getString("nameArtist");
            twoPane=getArguments().getBoolean("isTwoPane");
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
        outState.putParcelableArrayList(LOCAL_LIST, (ArrayList<? extends android.os.Parcelable>) tracksList);
     }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {
              tracksList=savedInstanceState.getParcelableArrayList(LOCAL_LIST);
               trackAdapter.addAll(tracksList);
            // Restore last state for checked position
               if(savedInstanceState.getInt(SELECTED_KEY) != ListView.INVALID_POSITION){
                    mPosition = savedInstanceState.getInt(SELECTED_KEY);
            }
        }
    }
    @Override
    public void onResume() {
        super.onResume();
        if(tracksList!=null){
            trackAdapter.addAll(tracksList);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mPosition = position;
            /*highlight the selected list item */
        view.getFocusables(position);
        view.setSelected(true);

        if(twoPane){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            Bundle args = new Bundle();
            args.putParcelableArrayList("TrackList", (ArrayList<? extends android.os.Parcelable>) tracksList);
            args.putString("artistName", artistName);
            args.putInt("position", position);
            PlayerFragment fragment = new PlayerFragment();
            fragment.setArguments(args);
                  fragmentManager.beginTransaction()
                        .replace(R.id.containerPlay,fragment,SEARCH_TAG)
                        .commit();

        }else {
            Intent intent = new Intent(getActivity(), PlayerActivity.class);
            intent.putParcelableArrayListExtra("TrackList", (ArrayList<? extends android.os.Parcelable>) tracksList);
            intent.putExtra("artistName", artistName);
            intent.putExtra("position", position);
            startActivity(intent);
        }

    }

    class TopTrackAsyncTask extends AsyncTask<String, Void, List<MyTracks>> {
        @Override
        protected  List<MyTracks> doInBackground(String... strings) {
            SpotifyApi api = new SpotifyApi();
            SpotifyService spotify = api.getService();
            Map<String, Object> options = new HashMap<>();
            options.put(spotify.COUNTRY, MY_COUNTRY);
            Tracks tracks = spotify.getArtistTopTrack(strings[0], options);
            if (tracks.tracks != null && tracks.tracks.size() > 0) {
                for (Track track : tracks.tracks) {
                    List<Image> imageList = track.album.images;
                    String bigImgUrl = "";
                    String smallImgUrl = "";
                    if (imageList != null && imageList.size() != 0) {
                        bigImgUrl = "";
                        for (Image image : imageList) {
                            if (image.width < 700 && bigImgUrl.length() == 0) {
                                bigImgUrl = image.url;
                            }
                        }
                        smallImgUrl = imageList.get(imageList.size() - 1).url;
                    }
                    tracksList.add(new MyTracks(track.name, track.album.name, smallImgUrl, bigImgUrl,
                            track.preview_url, String.valueOf(track.duration_ms)));
                }
            }

            return tracksList;
        }

        @Override
        protected void onPostExecute(List<MyTracks> result) {
            topTracks=result;
            if(result != null) {
                tracksList = result;
                trackAdapter.clear();
                trackAdapter.addAll(tracksList);
            }
            if(result.size() == 0){
               trackAdapter.clear();
               mImageView.setVisibility(View.VISIBLE);
            }
        }


    }
}//end fragment
