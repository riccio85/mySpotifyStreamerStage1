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
import app.com.example.rihanna.myspotifystreamer.utils.TrackInfo;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Rihanna on 04/06/2015.
 */
public class TopTrackAdapter extends ArrayAdapter<TrackInfo> {
    Context context;
    private static final int TRACK_LAYOUT = R.layout.track_item;

    public TopTrackAdapter (Context context) {
        super(context,TRACK_LAYOUT,new ArrayList<TrackInfo>());
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView album;
        TextView track;
    }
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        TrackInfo trackItem=getItem(position);

        String albumName;
        String trackName;

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


        albumName= trackItem.getAlbumName();
        trackName= trackItem.getTrackName();
        List<Image> imagesList=trackItem.getImagesList();
        holder.album.setText(albumName);
        holder.track.setText(trackName);

        //TODO see the image sizing and if there is no image put a default image
        try {
            // String uri=artistItem.uri; nn da nessun imagine
            // Picasso.with(this.context).load(uri).into(holder.imageView);
            Image img = imagesList.get(0);
            String url = img.url;
            Picasso.with(this.context).load(url).into(holder.imageView);

        }catch(Exception e){}

        return convertView;

    }
     /*
        old version that takes List<Track>
        Context context;
        private static final int TRACK_LAYOUT =R.layout.track_item;

public TopTrackAdapter (Context context) {
        super(context,TRACK_LAYOUT,new ArrayList<Track>());
        this.context = context;
        }

private class ViewHolder {
    ImageView imageView;
    TextView album;
    TextView track;
}
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        Track trackItem=getItem(position);

        String albumName;
        String trackName;

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


        albumName= trackItem.album.name;
        trackName= trackItem.name;
        holder.album.setText(albumName);
        holder.track.setText(trackName);



        List<Image> imagesList=trackItem.album.images;

        //TODO see the image sizing and if there is no image put a default image
        try {
            // String uri=artistItem.uri; nn da nessun imagine
            // Picasso.with(this.context).load(uri).into(holder.imageView);
            Image img = imagesList.get(0);
            String url = img.url;
            Picasso.with(this.context).load(url).into(holder.imageView);

        }catch(Exception e){}

        return convertView;

    }*/
}