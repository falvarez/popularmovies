package androidtraining.falvarez.es.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieModel implements Parcelable {

    private String title;
    private String description;
    private String posterUrl;
    private String launchDate;
    private String rating;

    public MovieModel(String title, String description, String posterUrl, String launchDate, String rating) {
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.launchDate = launchDate;
        this.rating = rating;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getLaunchDate() {
        return launchDate;
    }

    public String getRating() {
        return rating;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringArray(new String[] {
                this.title,
                this.description,
                this.posterUrl,
                this.launchDate,
                this.rating
        });
    }

    public MovieModel(Parcel in) {
        String[] data = new String[5];
        in.readStringArray(data);
        this.title = data[0];
        this.description = data[1];
        this.posterUrl = data[2];
        this.launchDate = data[3];
        this.rating = data[4];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };
}
