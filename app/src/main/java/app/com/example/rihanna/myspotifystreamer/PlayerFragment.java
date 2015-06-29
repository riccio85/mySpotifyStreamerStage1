package app.com.example.rihanna.myspotifystreamer;

import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.List;
import app.com.example.rihanna.myspotifystreamer.utils.*;
import kaaes.spotify.webapi.android.models.Image;

/**
 * Created by Rihanna on 26/06/2015.
 */
public class PlayerFragment extends DialogFragment {
    List<MyTracks> trackList;
    int resetPlay, resetPause;
    String artist;
    int position;
    TextView t_artist;
    TextView t_album;
    ImageView t_image;
    TextView t_track;
    ProgressBar progressBar;
    TextView t_time;
    TextView t_duration;
    ImageButton play;
    ImageButton next;
    ImageButton previous;
    private boolean playing;

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        Bundle arguments = getArguments();
        if (arguments != null) {
            trackList = getArguments().getParcelableArrayList("TrackList");
            artist = getArguments().getString("artistName");
            position = getArguments().getInt("position");
        }
        Intent intent = getActivity().getIntent();
        if (intent.hasExtra(Playing.MY_TRACK_LIST)) {
            trackList = intent.getParcelableArrayListExtra(Playing.MY_TRACK_LIST);
            artist = intent.getStringExtra(Playing.ARTIST_NAME);
            position = intent.getIntExtra(Playing.POSITION, 0);
        }

        View rootView = inflater.inflate(R.layout.fragment_player, container, false);
        resetPause = getResources(). getIdentifier("@android:drawable/ic_media_pause", null, getActivity().getPackageName());
        resetPlay = getResources().getIdentifier("@android:drawable/ic_media_play", null, getActivity().getPackageName());
        t_artist=(TextView)rootView.findViewById(R.id.t_artist);
        t_album=(TextView)rootView.findViewById(R.id.t_album);
        t_image=(ImageView)rootView.findViewById(R.id.t_imgAlbum);
        t_track=(TextView)rootView.findViewById(R.id.t_track);
        progressBar=(ProgressBar)rootView.findViewById(R.id.progressBar);
        t_time=(TextView)rootView.findViewById(R.id.t_time);
        t_duration=(TextView)rootView.findViewById(R.id.t_duration);
        next=(ImageButton)rootView.findViewById(R.id.btnNext);
        previous=(ImageButton)rootView.findViewById(R.id.btnPrevious);
        play=(ImageButton)rootView.findViewById(R.id.btnPlay);


        if(trackList!=null){
        setTrackInfo();
        setButtonsClickListener();
        playThisTrack();
    }
        return rootView;
    }

    private void setTrackInfo() {
        if(trackList!=null) {
            MyTracks trackInfo=trackList.get(position);
            t_artist.setText(artist);
            t_album.setText(trackInfo.getAlbum());
            String imgUrl =trackInfo.getBigImgUrl();
            if (imgUrl.length() > 0) {
                Picasso.with(getActivity()).load(imgUrl).into(t_image);
            }
            t_track.setText(trackInfo.getTitle());
            t_duration.setText(formatDuration(trackInfo.getDuration()));
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        play.setImageResource(resetPlay);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPause() {
        super.onDestroy();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    private void setButtonsClickListener() {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.btnPlay:
                        playOrPause();
                        break;
                    case R.id.btnNext:
                        if (position < trackList.size() - 1) {
                            position++;
                            setTrackInfo();
                        }
                        playThisTrack();
                        break;
                    case R.id.btnPrevious:
                        if (position > 0) {
                            position--;
                            setTrackInfo();
                        }
                        playThisTrack();
                        break;
                }
            }
        };
        next.setOnClickListener(clickListener);
        play.setOnClickListener(clickListener);
        previous.setOnClickListener(clickListener);
    }
    private void playThisTrack() {
        String url = trackList.get(position).getPreviewUrl();
        Intent intent = new Intent(getActivity(),Playing.class);
        intent.setAction(Playing.MY_ACTION_PLAY);
        intent.putParcelableArrayListExtra(Playing.MY_TRACK_LIST, (ArrayList<? extends Parcelable>) trackList);
        intent.putExtra(Playing.ARTIST_NAME, artist);
        intent.putExtra(Playing.POSITION, position);
        playing = true;
        play.setImageResource(resetPause);
        getActivity().startService(intent);
    }

    private void playOrPause() {
        if (playing) {
            play.setImageResource(resetPlay);
            playing = false;
            Intent intent = new Intent(getActivity(), Playing.class);
            intent.setAction(Playing.MY_ACTION_PAUSE);
            getActivity().startService(intent);
        } else {
            play.setImageResource(resetPause);
            playing = true;
            playThisTrack();
        }
    }
    private String formatDuration(String duration) {
        int mili = Integer.valueOf(duration);
        int secs = mili/ 1000;
        int minutes = secs / 60;
        int seconds = secs - 60 * minutes;
        String zero = "";
        if (seconds < 10) zero = "0";
        return minutes + ":" + zero + seconds;
    }


}