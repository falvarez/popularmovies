package androidtraining.falvarez.es.popularmovies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TrailerModel {

    public static final String TYPE_TRAILER = "Trailer";
    public static final String SITE_YOUTUBE = "YouTube";

    private String id;
    private String iso639_1;
    private String iso3166_1;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;

    public TrailerModel(String id, String iso639_1, String iso3166_1, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.iso639_1 = iso639_1;
        this.iso3166_1 = iso3166_1;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }

    public static String getTypeTrailer() {
        return TYPE_TRAILER;
    }

    public static String getSiteYoutube() {
        return SITE_YOUTUBE;
    }

    public String getId() {
        return id;
    }

    public String getIso639_1() {
        return iso639_1;
    }

    public String getIso3166_1() {
        return iso3166_1;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getSite() {
        return site;
    }

    public Integer getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public String getVideoUrl() {
        if (site.equals(SITE_YOUTUBE)) {
            return "https://www.youtube.com/v/" + key;
        } else {
            return null;
        }
    }

    public String getThumbnailUrl() {
        if (site.equals(SITE_YOUTUBE)) {
            return "http://img.youtube.com/vi/" + key + "/hqdefault.jpg";
        } else {
            return null;
        }
    }

    public static TrailerModel[] createModelsFromJson(String serviceJsonString) throws JSONException {
        final String API_RESULTS = "results";
        final String API_ID = "id";
        final String API_ISO_639_1 = "iso_639_1";
        final String API_ISO_3166_1 = "iso_3166_1";
        final String API_KEY = "key";
        final String API_NAME = "name";
        final String API_SITE = "site";
        final String API_SIZE = "size";
        final String API_TYPE = "type";

        JSONObject trailersJson = new JSONObject(serviceJsonString);

        JSONArray trailersArray = trailersJson.getJSONArray(API_RESULTS);

        TrailerModel[] models = new TrailerModel[trailersArray.length()];

        for (int i = 0; i < trailersArray.length(); i++) {
            JSONObject trailer = trailersArray.getJSONObject(i);
            models[i] = new TrailerModel(
                    trailer.getString(API_ID),
                    trailer.getString(API_ISO_639_1),
                    trailer.getString(API_ISO_3166_1),
                    trailer.getString(API_KEY),
                    trailer.getString(API_NAME),
                    trailer.getString(API_SITE),
                    trailer.getInt(API_SIZE),
                    trailer.getString(API_TYPE)
            );
        }

        return models;
    }
}
