package androidtraining.falvarez.es.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

import androidtraining.falvarez.es.popularmovies.data.MovieContract;
import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private static final String INSTANCE_STATE_CURRENT_API_URL = "currentApiUrl";
    private static final String INSTANCE_STATE_CURRENT_TITLE = "currentTitle";

    @BindView(R.id.movie_covers_rv) RecyclerView mMoviesGrid;
    @BindView(R.id.error_message_tv) TextView mErrorMessageDisplay;
    @BindView(R.id.loading_indicator_pb) ProgressBar mLoadingIndicator;
    @BindView(R.id.swipe_refresh_layout) SwipeRefreshLayout mSwipeRefreshLayout;

    private MyRecyclerViewAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    private String mCurrentApiUrl;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(INSTANCE_STATE_CURRENT_API_URL, mCurrentApiUrl);
        outState.putString(INSTANCE_STATE_CURRENT_TITLE, mCurrentTitle);
    }

    private String mCurrentTitle;

    public static int getGridNumberOfColumns(Context context) {
        // @see http://stackoverflow.com/questions/33575731/gridlayoutmanager-how-to-auto-fit-columns
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (dpWidth / 180);
    }

    private void initGridLayoutManager() {
        gridLayoutManager = new GridLayoutManager(this, getGridNumberOfColumns(this));
        mMoviesGrid.setLayoutManager(gridLayoutManager);
    }

    private void setGridAdapter() {
        adapter = new MyRecyclerViewAdapter(this);
        adapter.setClickListener(this);
        mMoviesGrid.setAdapter(adapter);
    }

    private void initSwipeRefresh() {
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshMoviesGrid(mCurrentApiUrl, mCurrentTitle);
            }
        });
    }

    private void init(Bundle savedInstanceState) {

        ButterKnife.bind(this);
        initGridLayoutManager();
        setGridAdapter();
        initSwipeRefresh();

        String apiMethod;
        String title;

        if (null != savedInstanceState) {
            apiMethod = savedInstanceState.getString(INSTANCE_STATE_CURRENT_API_URL);
            title = savedInstanceState.getString(INSTANCE_STATE_CURRENT_TITLE);
        } else {
            apiMethod = TheMovieDbApiClient.API_METHOD_MOVIE_POPULAR;
            title = getResources().getString(R.string.most_popular_movies);
        }
        refreshMoviesGrid(apiMethod, title);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (null == mCurrentApiUrl) {
            return;
        }
        if (mCurrentApiUrl.equals(TheMovieDbApiClient.API_METHOD_MOVIE_FAVOURITES)) {
            refreshMoviesGrid(
                    TheMovieDbApiClient.API_METHOD_MOVIE_FAVOURITES,
                    getResources().getString(R.string.favourite_movies)
            );
        }
    }

    private void refreshMoviesGrid(String apiUrl, String title) {
        if (NetworkUtils.isOnline()
                || apiUrl.equals(TheMovieDbApiClient.API_METHOD_MOVIE_FAVOURITES)) {
            new FetchMoviesDataTask().execute(apiUrl, title);
        } else {
            mSwipeRefreshLayout.setRefreshing(false);
            Toast.makeText(this, R.string.network_down, Toast.LENGTH_LONG).show();
        }
    }

    private void showMoviesGridView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesGrid.setVisibility(View.VISIBLE);
        gridLayoutManager.scrollToPositionWithOffset(0, 0);
    }

    private void showErrorMessage() {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(getString(R.string.error_message));
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    private void showNoFavouritesEmptyMessage() {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setText(getString(R.string.no_favourites_message));
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        ImageView mMoviePoster = (ImageView) view.findViewById(R.id.movie_cover_iv);

        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_MOVIE_MODEL, adapter.getItem(position));
        intent.putExtra(DetailActivity.EXTRA_ACTIVITY_TITLE, this.mCurrentTitle);

        // Configure image transitions for API Level >= 21
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            ActivityOptionsCompat options = ActivityOptionsCompat.
                    makeSceneTransitionAnimation(this, mMoviePoster, "movie_poster_transition");
            startActivity(intent, options.toBundle());
        } else {
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_most_popular) {
            refreshMoviesGrid(
                    TheMovieDbApiClient.API_METHOD_MOVIE_POPULAR,
                    getResources().getString(R.string.most_popular_movies)
            );
            return true;
        }

        if (id == R.id.action_top_rated) {
            refreshMoviesGrid(
                    TheMovieDbApiClient.API_METHOD_MOVIE_TOP_RATED,
                    getResources().getString(R.string.top_rated_movies)
            );
            return true;
        }

        if (id == R.id.action_favourites) {
            refreshMoviesGrid(
                    TheMovieDbApiClient.API_METHOD_MOVIE_FAVOURITES,
                    getResources().getString(R.string.favourite_movies)
            );
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // TODO move to a single file
    public class FetchMoviesDataTask extends AsyncTask<String, Void, MovieModel[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieModel[] doInBackground(String... params) {
            if (params.length == 0) {
                return null;
            }

            String serviceUrl = params[0];
            String title = params[1];

            MovieModel[] movieModels;

            try {
                if (serviceUrl.equals(TheMovieDbApiClient.API_METHOD_MOVIE_FAVOURITES)) {
                    // Fetch movies from content provider
                    Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MovieContract.MovieEntry.COLUMN_TITLE
                    );
                    movieModels = MovieModel.createModelsFromCursor(cursor);
                    //cursor.close();
                } else {
                    // Fetch movies from API
                    URL moviesRequestUrl = TheMovieDbApiClient.buildUrl(serviceUrl);
                    String jsonMoviesResponse = NetworkUtils.getResponseFromHttpUrl(moviesRequestUrl);
                    movieModels = MovieModel.createModelsFromJson(jsonMoviesResponse);
                }

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

            mCurrentTitle = title;
            mCurrentApiUrl = serviceUrl;
            return movieModels;
        }

        @Override
        protected void onPostExecute(MovieModel[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {

                if (moviesData.length == 0) {
                    showNoFavouritesEmptyMessage();
                } else {
                    showMoviesGridView();
                    setTitle(mCurrentTitle);
                    adapter.setMoviesData(moviesData);
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            } else {
                showErrorMessage();
            }
        }
    }
}
