package bg.tilchev.hw03task02;

import android.os.Parcel;
import android.os.Parcelable;

public class City implements Parcelable {

    public static final Creator<City> CREATOR = new Creator<City>() {
        @Override
        public City createFromParcel(Parcel in) {
            return new City(in);
        }

        @Override
        public City[] newArray(int size) {
            return new City[size];
        }
    };

    private String name;

    public City(String name) {
        this.name = name;
    }

    public City(Parcel in) {
        this.name = in.readString();
    }

    public String getName() {
        return this.name;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
