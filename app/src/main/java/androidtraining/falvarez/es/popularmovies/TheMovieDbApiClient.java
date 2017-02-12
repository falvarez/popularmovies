package androidtraining.falvarez.es.popularmovies;

import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URL;

public class TheMovieDbApiClient {

    private static final String API_BASE_URL = "https://api.themoviedb.org/3";
    private static final String API_KEY = BuildConfig.MOVIEDB_API_KEY;

    public static final String API_METHOD_MOVIE_POPULAR = "/movie/popular";
    public static final String API_METHOD_MOVIE_TOP_RATED = "/movie/top_rated";

    public static URL buildUrl(String serviceUrl) {
        Uri builtUri = Uri.parse(API_BASE_URL + serviceUrl).buildUpon()
                .appendQueryParameter("api_key", API_KEY)
                .build();

        URL url = null;
        try {
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return url;
    }
}
