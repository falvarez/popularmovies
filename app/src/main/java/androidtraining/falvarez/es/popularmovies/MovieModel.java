package androidtraining.falvarez.es.popularmovies;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MovieModel implements Parcelable {

    public static final String EXTRA_MOVIE_MODEL = "movieModel";

    private static final String URL_FORMAT = "http://image.tmdb.org/t/p/%s/%s";

    public static final String MEASURE_W92 = "w92";
    public static final String MEASURE_W154 = "w154";
    public static final String MEASURE_W185 = "w185";
    public static final String MEASURE_W342 = "w342";
    public static final String MEASURE_W500 = "w500";
    public static final String MEASURE_W780 = "w780";
    public static final String MEASURE_ORIGINAL = "original";

    private String id;
    private String title;
    private String description;
    private String posterUrl;
    private String launchDate;
    private String rating;

    public MovieModel(String id, String title, String description, String posterUrl, String launchDate, String rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.posterUrl = posterUrl;
        this.launchDate = launchDate;
        this.rating = rating;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterFullUrl(String measure) {
        return String.format(URL_FORMAT, measure, posterUrl);
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
        dest.writeStringArray(new String[]{
                this.id,
                this.title,
                this.description,
                this.posterUrl,
                this.launchDate,
                this.rating
        });
    }

    public MovieModel(Parcel in) {
        String[] data = new String[6];
        in.readStringArray(data);
        this.id = data[0];
        this.title = data[1];
        this.description = data[2];
        this.posterUrl = data[3];
        this.launchDate = data[4];
        this.rating = data[5];
    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public MovieModel createFromParcel(Parcel in) {
            return new MovieModel(in);
        }

        public MovieModel[] newArray(int size) {
            return new MovieModel[size];
        }
    };

    public static MovieModel[] createModelsFromJson(String serviceJsonString) throws JSONException {
        final String API_RESULTS = "results";
        final String API_ID = "id";
        final String API_TITLE = "title";
        final String API_OVERVIEW = "overview";
        final String API_RELEASE_DATE = "release_date";
        final String API_VOTE_AVERAGE = "vote_average";
        final String API_POSTER_PATH = "poster_path";

        JSONObject moviesJson = new JSONObject(serviceJsonString);

        JSONArray moviesArray = moviesJson.getJSONArray(API_RESULTS);

        MovieModel[] models = new MovieModel[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            models[i] = new MovieModel(
                    movie.getString(API_ID),
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
