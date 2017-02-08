package androidtraining.falvarez.es.popularmovies;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    public static MovieModel[] getModelsFromJson(String serviceJsonString) throws JSONException {

        final String API_RESULTS = "results";

        final String API_TITLE = "title";
        final String API_OVERVIEW = "overview";
        final String API_RELEASE_DATE = "release_date";
        final String API_VOTE_AVERAGE = "vote_average";
        final String API_POSTER_PATH = "poster_path";

        MovieModel[] models = null;
        JSONObject moviesJson = new JSONObject(serviceJsonString);


        JSONArray moviesArray = moviesJson.getJSONArray(API_RESULTS);

        models = new MovieModel[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            models[i] = new MovieModel(
                    movie.getString(API_TITLE),
                    movie.getString(API_OVERVIEW),
                    movie.getString(API_POSTER_PATH),
                    movie.getString(API_RELEASE_DATE),
                    movie.getString(API_VOTE_AVERAGE)
            );
        }

        return models;
    }
}
