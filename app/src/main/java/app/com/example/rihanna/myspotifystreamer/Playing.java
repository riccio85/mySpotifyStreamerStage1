package app.com.example.rihanna.myspotifystreamer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.PowerManager;

import java.io.IOException;
import java.util.ArrayList;

import app.com.example.rihanna.myspotifystreamer.utils.MyTracks;

/**
 * Created by Rihanna on 24/06/2015.
 */
public class Playing extends Service {
    public static final String MY_ACTION_PLAY = "app.com.example.rihanna.myspotifystreamer.action.PLAY";
    public static final String MY_ACTION_PAUSE = "app.com.example.rihanna.myspotifystreamer.action.PAUSE";

    public static final int NOTIFICATION_ID = 1;
    public static final String MY_TRACK_LIST = "LIST";
    public static final String ARTIST_NAME = "ARTIST_NAME";
    public static final String POSITION = "POSITION";
    public static final String URL = "URL";
    private MediaPlayer mediaPlayer;
    private String currentUrl;
    private ArrayList<MyTracks> tracksList;
    private int position;
    private String artistName;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent.getParcelableArrayListExtra(MY_TRACK_LIST) != null) {
            if (intent.getAction().equals(MY_ACTION_PLAY)) {

                tracksList = intent.getParcelableArrayListExtra(MY_TRACK_LIST);
                position = intent.getIntExtra(POSITION, 0);
                artistName = intent.getStringExtra(ARTIST_NAME);
                String url = tracksList.get(position).getPreviewUrl();
                if (url.equals(currentUrl)) {
                    if (mediaPlayer != null) {
                        mediaPlayer.start();
                    }
                } else {
                    currentUrl = url;
                    play(url);
                }
                startForeground();
            }
            if (intent.getAction().equals(MY_ACTION_PAUSE)) {
                if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                }
            }
        }else {
            onDestroy();
        }
        return (START_NOT_STICKY);
    }

    private void startForeground()  {
        String songName = tracksList.get(position).getTitle();
        Intent intent = new Intent(Playing.this, PlayerActivity.class);
        intent.putParcelableArrayListExtra(Playing.this.MY_TRACK_LIST, tracksList);
        intent.putExtra(Playing.ARTIST_NAME, artistName);
        intent.putExtra(Playing.this.POSITION, position);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
       PendingIntent pi = PendingIntent.getActivity(Playing.this, 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification();
        notification.tickerText = songName;
        notification.icon = R.mipmap.ic_launcher;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;
        notification.setLatestEventInfo(getApplicationContext(), tracksList.get(position).getAlbum(),
                "Playing: " + songName, pi);
        startForeground(NOTIFICATION_ID, notification);
    }


    private void play(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    mediaPlayer.start();
                }
            });
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }


}