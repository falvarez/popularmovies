package androidtraining.falvarez.es.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private TextView mMovieDescription;
    private ImageView mMoviePoster;
    private TextView mMovieLaunchDate;
    private TextView mMovieRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mMovieTitle = (TextView) findViewById(R.id.detail_title_tv);
        mMovieDescription = (TextView) findViewById(R.id.detail_description_tv);
        mMoviePoster = (ImageView) findViewById(R.id.detail_poster_iv);
        mMovieLaunchDate = (TextView) findViewById(R.id.detail_launch_date_tv);
        mMovieRating = (TextView) findViewById(R.id.detail_rating_tv);

        Intent intentThatStartedThisActivity = getIntent();

        // COMPLETED (2) Display the weather forecast that was passed from MainActivity
//        if (intentThatStartedThisActivity != null) {
//            if (intentThatStartedThisActivity.hasExtra(Intent.EXTRA_TEXT)) {
//
//            }
//        }

        MovieModel movie = new MovieModel(
                "Jurassic World",
                "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.",
                "http://image.tmdb.org/t/p/w342/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                "2015",
                "6.5"
        );
        showMovie(movie);
    }

    private void showMovie(MovieModel movie) {
        mMovieTitle.setText(movie.getTitle());
        mMovieDescription.setText(movie.getDescription());
        mMovieLaunchDate.setText(movie.getLaunchDate());
        mMovieRating.setText(movie.getRating());

        Picasso.with(this).setLoggingEnabled(true);
        Picasso
                .with(this)
                .load(movie.getPosterUrl())
                .into(mMoviePoster);
    }
}
