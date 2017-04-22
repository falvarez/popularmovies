package androidtraining.falvarez.es.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ReviewModel {

    private String id;
    private String author;
    private String content;
    private String url;

    public ReviewModel(String id, String author, String content, String url) {
        this.id = id;
        this.author = author;
        this.content = content;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }

    public static ReviewModel[] createModelsFromJson(String serviceJsonString) throws JSONException {
        final String API_RESULTS = "results";
        final String API_ID = "id";
        final String API_AUTHOR = "author";
        final String API_CONTENT = "content";
        final String API_URL = "url";

        JSONObject moviesJson = new JSONObject(serviceJsonString);

        JSONArray moviesArray = moviesJson.getJSONArray(API_RESULTS);

        ReviewModel[] models = new ReviewModel[moviesArray.length()];

        for (int i = 0; i < moviesArray.length(); i++) {
            JSONObject movie = moviesArray.getJSONObject(i);
            models[i] = new ReviewModel(
                    movie.getString(API_ID),
                    movie.getString(API_AUTHOR),
                    movie.getString(API_CONTENT),
                    movie.getString(API_URL)
            );
        }

        return models;
    }
}
