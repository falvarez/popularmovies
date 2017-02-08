package androidtraining.falvarez.es.popularmovies;

public class MovieModel {

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
}
