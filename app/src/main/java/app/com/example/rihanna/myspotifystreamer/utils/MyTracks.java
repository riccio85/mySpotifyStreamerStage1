package app.com.example.rihanna.myspotifystreamer.utils;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Rihanna on 29/06/2015.
 */
public class MyTracks implements Parcelable {
    String title, album, smallImgUrl, bigImgUrl, previewUrl, duration;

    public MyTracks(String title, String album, String smallImgUrl, String bigImgUrl, String previewUrl, String duration) {
        this.title = title;
        this.album = album;
        this.smallImgUrl = smallImgUrl;
        this.bigImgUrl = bigImgUrl;
        this.previewUrl = previewUrl;
        this.duration = duration;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(album);
        dest.writeString(smallImgUrl);
        dest.writeString(bigImgUrl);
        dest.writeString(previewUrl);
        dest.writeString(duration);
    }

    public static final Parcelable.Creator<MyTracks> CREATOR = new Parcelable.Creator<MyTracks>() {
        public MyTracks createFromParcel(Parcel in) {
            return new MyTracks(in);
        }

        public MyTracks[] newArray(int size) {
            return new MyTracks[size];
        }
    };

    private MyTracks(Parcel parcel) {
        title = parcel.readString();
        album = parcel.readString();
        smallImgUrl = parcel.readString();
        bigImgUrl = parcel.readString();
        previewUrl = parcel.readString();
        duration = parcel.readString();
    }

    public String getTitle() {
        return title;
    }

    public String getAlbum() {
        return album;
    }

    public String getSmallImgUrl() {
        return smallImgUrl;
    }

    public String getBigImgUrl() {
        return bigImgUrl;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public String getDuration() {
        return duration;
    }
}
