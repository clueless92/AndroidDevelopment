package bg.tilchev.hw04task1;

import android.os.Parcel;
import android.os.Parcelable;

public class Song implements Parcelable {

    private String mSongName;
    private int mResID;

    public Song(String songName) {
        this.mSongName = songName;
    }

    public Song(String songName, int resID) {
        this(songName);
        this.mResID = resID;
    }

    protected Song(Parcel in) {
        this.mSongName = in.readString();
        this.mResID = in.readInt();
    }

    public static final Creator<Song> CREATOR = new Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel in) {
            return new Song(in);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String getSongName() {
        return this.mSongName;
    }

    public int getResID() {
        return this.mResID;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mSongName);
        dest.writeInt(this.mResID);
    }
}
