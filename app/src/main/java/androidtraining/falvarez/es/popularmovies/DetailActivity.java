package androidtraining.falvarez.es.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class DetailActivity extends AppCompatActivity {

    private TextView mMovieTitle;
    private TextView mMovieDescription;
    private ImageView mMoviePoster;
    private TextView mMovieLaunchDate;
    private TextView mMovieRating;

    private void configureViews() {
        mMovieTitle = (TextView) findViewById(R.id.detail_title_tv);
        mMovieDescription = (TextView) findViewById(R.id.detail_description_tv);
        mMoviePoster = (ImageView) findViewById(R.id.detail_poster_iv);
        mMovieLaunchDate = (TextView) findViewById(R.id.detail_launch_date_tv);
        mMovieRating = (TextView) findViewById(R.id.detail_rating_tv);
    }

    private void init() {
        configureViews();

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(MovieModel.EXTRA_MOVIE_MODEL)) {
                MovieModel movie = intentThatStartedThisActivity.getParcelableExtra(MovieModel.EXTRA_MOVIE_MODEL);
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
        mMovieRating.setText(String.format(getResources().getString(R.string.user_rating), movie.getRating()));

        RequestCreator requestCreator = Picasso
                .with(this)
                .load(movie.getPosterFullUrl(MovieModel.MEASURE_W342));
        requestCreator.fetch();
        requestCreator.into(mMoviePoster);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        init();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // Mimic back button behaviour
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
