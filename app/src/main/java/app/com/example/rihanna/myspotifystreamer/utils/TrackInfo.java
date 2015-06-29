package app.com.example.rihanna.myspotifystreamer.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import kaaes.spotify.webapi.android.models.Image;
import kaaes.spotify.webapi.android.models.Track;

/**
 * Created by Rihanna on 06/06/2015.
 */
public class TrackInfo  implements Parcelable {
    TrackInfo trackInfo;
    ArrayList<TrackInfo> tarckList;

    String artist;
    String trackName;
    String albumName;
    List<Image> imagesList;
    List<Track> trackList;
    List<TrackInfo> myTracks;

    public TrackInfo(){
    }
    public TrackInfo(String artist,String trackName,String albumName,List<Image> list ){
        this.artist=artist;
        this.trackName=trackName;
        this.albumName=albumName;
        this.imagesList=list;
    }

    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public String getTrackName() {
        return trackName;
    }
    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }
    public String getAlbumName() {
        return albumName;
    }
    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }
    public List<Image> getImagesList() {
        return imagesList;
    }
    public void setImagesList(List<Image> imagesList) {
        this.imagesList = imagesList;
    }

    public List<TrackInfo> filterTrackList(String artist,List<Track> tracks){
        this.myTracks=new ArrayList<TrackInfo>();
        for(int i=0;i<tracks.size();i++){
            Track trackItem=tracks.get(i);
            String trName=trackItem.name;
            String alName=trackItem.album.name;
            List<Image> images=trackItem.album.images;
            TrackInfo info=new TrackInfo(artist,trName,alName,images);
            myTracks.add(i,info);
        }
        return myTracks;
    }

    /*For Parcelable*/
    protected TrackInfo(Parcel in) {
        trackInfo = (TrackInfo) in.readValue(TrackInfo.class.getClassLoader());
        if (in.readByte() == 0x01) {
            tarckList = new ArrayList<TrackInfo>();
            in.readList(tarckList, TrackInfo.class.getClassLoader());
        } else {
            tarckList = null;
        }
        artist = in.readString();
        trackName = in.readString();
        albumName = in.readString();
        if (in.readByte() == 0x01) {
            trackList = new ArrayList<Track>();
            in.readList(trackList, Track.class.getClassLoader());
        } else {
            trackList = null;
        }
        if (in.readByte() == 0x01) {
            myTracks = new ArrayList<TrackInfo>();
            in.readList(myTracks, TrackInfo.class.getClassLoader());
        } else {
            myTracks = null;
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(trackInfo);
        if (tarckList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(tarckList);
        }
        dest.writeString(artist);
        dest.writeString(trackName);
        dest.writeString(albumName);
        if (trackList == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(trackList);
        }
        if (myTracks == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(myTracks);
        }
    }

    public static final Parcelable.Creator<TrackInfo> CREATOR = new Parcelable.Creator<TrackInfo>() {
        @Override
        public TrackInfo createFromParcel(Parcel in) {
            return new TrackInfo(in);
        }
        @Override
        public TrackInfo[] newArray(int size) {
            return new TrackInfo[size];
        }
    };
}