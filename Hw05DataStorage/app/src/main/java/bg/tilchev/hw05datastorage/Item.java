package bg.tilchev.hw05datastorage;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {

    private int mId;
    private String mText;
    private String mBase64ImgStr;

    public Item(int id, String text, String base64ImgStr) {
        this.mId = id;
        this.mText = text;
        this.mBase64ImgStr = base64ImgStr;
    }

    protected Item(Parcel in) {
        this.mId = in.readInt();
        this.mText = in.readString();
        this.mBase64ImgStr = in.readString();
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

    public String getText() {
        return this.mText;
    }

    public String getBase64ImgStr() {
        return this.mBase64ImgStr;
    }

    public int getId() {
        return this.mId;
    }

    public void setText(String text) {
        this.mText = text;
    }

    public void setBase64ImgStr(String base64ImgStr) {
        this.mBase64ImgStr = base64ImgStr;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.mId);
        dest.writeString(this.mText);
        dest.writeString(this.mBase64ImgStr);
    }
}
