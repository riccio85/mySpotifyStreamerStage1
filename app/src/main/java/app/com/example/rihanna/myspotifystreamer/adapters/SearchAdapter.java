package app.com.example.rihanna.myspotifystreamer.adapters;
/**
 * Created by Rihanna on 02/06/2015.
 */
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import app.com.example.rihanna.myspotifystreamer.R;
import kaaes.spotify.webapi.android.models.Artist;
import kaaes.spotify.webapi.android.models.ArtistSimple;
import kaaes.spotify.webapi.android.models.Image;

public class SearchAdapter extends ArrayAdapter<Artist> {

    Context context;
    private static final int ALAYOUT =R.layout.artist_item;
    LayoutInflater inflater;


    public SearchAdapter (Context context) {
        super(context, ALAYOUT, new ArrayList<Artist>());
        this.inflater = LayoutInflater.from(context);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView name;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Artist artistItem = getItem(position);
        String nameArtist;

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.artist_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            convertView.setTag(holder);
        }
        else
             { holder = (ViewHolder) convertView.getTag();}


            nameArtist= artistItem.name;
            holder.name.setText(nameArtist);


            List<Image> imagesList=artistItem.images;

        //TODO see the image sizing and if there is no image put a default image
        try {
           // String uri=artistItem.uri; nn da nessun imagine
            // Picasso.with(this.context).load(uri).into(holder.imageView);
            if(!artistItem.images.isEmpty()){
                Image img = imagesList.get(0);
                String url = img.url;
                Picasso.with(this.context).load(url).into(holder.imageView);
            } else {// put a default image}

            }
        }catch(Exception e){}


        return convertView;

    }
}