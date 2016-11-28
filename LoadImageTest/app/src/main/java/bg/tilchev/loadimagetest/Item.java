package bg.tilchev.loadimagetest;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private String mText;
    private String mImgSrc;

    public Item(String songName) {
        this.mText = songName;
    }

    public Item(String songName, String imgSrc) {
        this(songName);
        this.mImgSrc = imgSrc;
    }

    protected Item(Parcel in) {
        this.mText = in.readString();
        this.mImgSrc = in.readString();
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    public String getSongName() {
        return this.mText;
    }

    public String getImgSrc() {
        return this.mImgSrc;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mText);
        dest.writeString(this.mImgSrc);
    }
}
