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

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra("movieModel")) {
                MovieModel movie = intentThatStartedThisActivity.getParcelableExtra("movieModel");
                showMovie(movie);
            }
        }
    }

    private void showMovie(MovieModel movie) {

        setTitle(movie.getTitle());

        String[] dateParts = movie.getLaunchDate().split("-");

        mMovieTitle.setText(movie.getTitle());
        mMovieDescription.setText(movie.getDescription());
        mMovieLaunchDate.setText(dateParts[0]);
        mMovieRating.setText("User rating: " + movie.getRating());

        Picasso.with(this).setLoggingEnabled(true);
        Picasso
                .with(this)
                .load("http://image.tmdb.org/t/p/w342" + movie.getPosterUrl())
                .into(mMoviePoster);
    }
}
