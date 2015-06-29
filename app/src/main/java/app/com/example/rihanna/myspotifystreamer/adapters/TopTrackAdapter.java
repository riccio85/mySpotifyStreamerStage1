package app.com.example.rihanna.myspotifystreamer.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.example.rihanna.myspotifystreamer.R;
import app.com.example.rihanna.myspotifystreamer.utils.MyTracks;
import app.com.example.rihanna.myspotifystreamer.utils.TrackInfo;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Rihanna on 04/06/2015.
 */
public class TopTrackAdapter extends ArrayAdapter<MyTracks> {
    Context context;
    private static final int TRACK_LAYOUT = R.layout.track_item;

    public TopTrackAdapter (Context context) {
        super(context,TRACK_LAYOUT,new ArrayList<MyTracks>());
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView album;
        TextView track;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        MyTracks trackItem=getItem(position);

        String albumName;
        String trackName;
        String imgUrl;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.track_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.album_image);
            holder.album = (TextView) convertView.findViewById(R.id.album_name);
            holder.track = (TextView) convertView.findViewById(R.id.track_name);
            convertView.setTag(holder);
        }
        else
        { holder = (ViewHolder) convertView.getTag();}


        albumName= trackItem.getAlbum();
        trackName= trackItem.getTitle();
        imgUrl=trackItem.getSmallImgUrl();

        holder.album.setText(albumName);
        holder.track.setText(trackName);

        try {
            Picasso.with(this.context).load(imgUrl).into(holder.imageView);

        }catch(Exception e){}

        return convertView;

    }

}