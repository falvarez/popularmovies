package androidtraining.falvarez.es.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

import java.net.URL;

import androidtraining.falvarez.es.popularmovies.data.MovieContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_ACTIVITY_TITLE = "activityTitle";
    public static final String EXTRA_MOVIE_MODEL = "movieModel";

    @BindView(R.id.detail_title_tv) TextView mMovieTitle;
    @BindView(R.id.detail_description_tv) TextView mMovieDescription;
    @BindView(R.id.detail_poster_iv) ImageView mMoviePoster;
    @BindView(R.id.detail_launch_date_tv) TextView mMovieLaunchDate;
    @BindView(R.id.detail_rating_tv) TextView mMovieRating;
    @BindView(R.id.trailers_list) LinearLayout mTrailersList;
    @BindView(R.id.reviews_list) LinearLayout mReviewsList;
    @BindView(R.id.favourite_ib) ImageButton mFavouriteButton;
    @BindView(R.id.share_ib) ImageButton mShareButton;

    MovieModel mMovie;
    Boolean mIsFavourite;

    private void init() {
        ButterKnife.bind(this);

        Intent intentThatStartedThisActivity = getIntent();

        if (intentThatStartedThisActivity != null) {
            if (intentThatStartedThisActivity.hasExtra(EXTRA_MOVIE_MODEL)) {
                MovieModel movie = intentThatStartedThisActivity.getParcelableExtra(DetailActivity.EXTRA_MOVIE_MODEL);
                showMovie(movie);
            }
            if (intentThatStartedThisActivity.hasExtra(EXTRA_ACTIVITY_TITLE)) {
                setTitle(intentThatStartedThisActivity.getStringExtra(EXTRA_ACTIVITY_TITLE));
            }
        }
    }

    private void showMovie(final MovieModel movie) {

        String[] dateParts = movie.getLaunchDate().split("-");

        mMovie = movie;

        mMovieTitle.setText(movie.getTitle());
        mMovieDescription.setText(movie.getDescription());
        mMovieLaunchDate.setText(dateParts[0]);
        mMovieRating.setText(String.format(getResources().getString(R.string.user_rating), movie.getRating()));

        // TODO manage errors
        RequestCreator requestCreator = Picasso
                .with(this)
                .load(movie.getPosterFullUrl(MovieModel.MEASURE_W342));
        requestCreator.fetch();
        requestCreator.into(mMoviePoster);

        refreshTrailersInfo(mMovie.getId());
        refreshReviewsInfo(mMovie.getId());

        // Favourite handling
        new FetchFavouriteDataTask().execute(mMovie.getId());

        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton imageButton = (ImageButton) view;
                if (mIsFavourite) {
                    mIsFavourite = false;
                    imageButton.setImageResource(android.R.drawable.btn_star_big_off);
                    // Delete movie from content provider
                    unsetMovieAsFavourite();
                } else {
                    mIsFavourite = true;
                    imageButton.setImageResource(android.R.drawable.btn_star_big_on);
                    // Add movie to content provider
                    setMovieAsFavourite();
                }
            }
        });

        final Activity activity = this;


    }

    private void setMovieAsFavourite() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieEntry._ID, mMovie.getId());
        contentValues.put(MovieContract.MovieEntry.COLUMN_TITLE, mMovie.getTitle());
        contentValues.put(MovieContract.MovieEntry.COLUMN_DESCRIPTION, mMovie.getDescription());
        contentValues.put(MovieContract.MovieEntry.COLUMN_POSTER_URL, mMovie.getPosterUrl());
        contentValues.put(MovieContract.MovieEntry.COLUMN_LAUNCH_DATE, mMovie.getLaunchDate());
        contentValues.put(MovieContract.MovieEntry.COLUMN_RATING, mMovie.getRating());
        Uri uri = getContentResolver().insert(MovieContract.MovieEntry.CONTENT_URI, contentValues);
        if(uri != null) {
            Toast.makeText(getBaseContext(), String.format(getString(R.string.marked_as_favourite), mMovie.getTitle()), Toast.LENGTH_LONG).show();
        }
    }

    private void unsetMovieAsFavourite() {
        Uri uri = MovieContract.MovieEntry.CONTENT_URI;
        uri = uri.buildUpon().appendPath(mMovie.getId()).build();
        if(uri != null) {
            Toast.makeText(getBaseContext(), String.format(getString(R.string.unmarked_as_favourite), mMovie.getTitle()), Toast.LENGTH_LONG).show();
        }
        getContentResolver().delete(uri, null, null);
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

    private void refreshTrailersInfo(String id) {
        if (NetworkUtils.isOnline()) {
            new DetailActivity.FetchTrailersDataTask().execute(TheMovieDbApiClient.API_METHOD_MOVIE_TRAILERS, id);
        } else {
            Toast.makeText(this, R.string.network_down, Toast.LENGTH_LONG).show();
        }
    }

    private void refreshReviewsInfo(String id) {
        if (NetworkUtils.isOnline()) {
            new DetailActivity.FetchReviewsDataTask().execute(TheMovieDbApiClient.API_METHOD_MOVIE_REVIEWS, id);
        } else {
            Toast.makeText(this, R.string.network_down, Toast.LENGTH_LONG).show();
        }
    }

    private void showShareButton(final String trailerUrl)
    {
        final Activity activity = this;
        final String movieTitle = mMovie.getTitle();
        mShareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ShareCompat.IntentBuilder
                        .from(activity)
                        .setType("text/plain")
                        .setChooserTitle(String.format(getString(R.string.share_movie_trailer_title), movieTitle))
                        .setText(String.format(getString(R.string.share_movie_trailer_text), movieTitle, trailerUrl))
                        .startChooser();
            }
        });
        mShareButton.setVisibility(View.VISIBLE);
    }

    public class FetchTrailersDataTask extends AsyncTask<String, Void, TrailerModel[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected TrailerModel[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String serviceUrl = params[0];
            String id = params[1];
            URL reviewsRequestUrl = TheMovieDbApiClient.buildUrl(serviceUrl, id);

            try {
                String jsonTrailersResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
                return TrailerModel.createModelsFromJson(jsonTrailersResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        private ImageView createPosterImageView(final TrailerModel trailer) {

            Context context = getApplicationContext();
            ImageView trailerPoster = new ImageView(context);

            RequestCreator requestCreator = Picasso
                    .with(context)
                    .load(trailer.getThumbnailUrl());
            requestCreator.fetch();
            requestCreator.into(trailerPoster);

            trailerPoster.setPadding(16, 16, 16, 16);

            trailerPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = trailer.getVideoUrl();
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }
            });

            return trailerPoster;
        }

        @Override
        protected void onPostExecute(TrailerModel[] trailers) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            String firstTrailerUrl = null;
            if (trailers != null) {
                for (TrailerModel trailer : trailers) {
                    if (trailer.getType().equals(TrailerModel.TYPE_TRAILER)
                            && (null != trailer.getVideoUrl())) {
                        mTrailersList.addView(createPosterImageView(trailer));
                        if (null == firstTrailerUrl) {
                            firstTrailerUrl = trailer.getVideoUrl();
                        }
                    }
                    showShareButton(firstTrailerUrl);
                }
            } else {
                //showErrorMessage();
            }
        }
    }

    public class FetchReviewsDataTask extends AsyncTask<String, Void, ReviewModel[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected ReviewModel[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String serviceUrl = params[0];
            String id = params[1];
            URL reviewsRequestUrl = TheMovieDbApiClient.buildUrl(serviceUrl, id);

            try {
                String jsonReviewsResponse = NetworkUtils.getResponseFromHttpUrl(reviewsRequestUrl);
                return ReviewModel.createModelsFromJson(jsonReviewsResponse);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(ReviewModel[] reviews) {
            //mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (reviews != null) {
                for (ReviewModel review : reviews) {

                    final ReviewModel reviewModel = review;

                    Context context = getApplicationContext();
                    TextView reviewTitle = new TextView(context);
                    reviewTitle.setText(String.format(getResources().getString(R.string.review_author_title), review.getAuthor()));
                    reviewTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    reviewTitle.setPadding(16, 16, 16, 16);
                    reviewTitle.setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    reviewTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String url = reviewModel.getUrl();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse(url));
                            startActivity(intent);
                        }
                    });

                    TextView reviewContent = new TextView(context);
                    reviewContent.setText(review.getContent());
                    reviewContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
                    reviewContent.setPadding(16, 16, 16, 16);

                    View separator = new View(context);
                    separator.setLayoutParams(new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            1
                    ));
                    separator.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
                    separator.setPadding(0, 0, 0, 32);

                    mReviewsList.addView(reviewTitle);
                    mReviewsList.addView(reviewContent);
                    mReviewsList.addView(separator);
                }
            } else {
                //showErrorMessage();
            }
        }
    }

    public class FetchFavouriteDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String movieId = params[0];
            try {
                Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                        null,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{movieId},
                        null);
                Boolean result = (cursor.getCount() > 0);
                cursor.close();
                return result;

            } catch (Exception e) {
                Log.e("DetailActivity", "Failed to asynchronously load data.");
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Boolean isFavourite) {
            if (null == isFavourite) {
                return;
            }

            mIsFavourite = isFavourite;
            if (mIsFavourite) {
                mFavouriteButton.setImageResource(android.R.drawable.btn_star_big_on);
            } else {
                mFavouriteButton.setImageResource(android.R.drawable.btn_star_big_off);
            }
            mFavouriteButton.setVisibility(View.VISIBLE);
        }
    }
}
