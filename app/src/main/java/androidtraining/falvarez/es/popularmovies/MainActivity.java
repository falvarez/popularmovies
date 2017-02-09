package androidtraining.falvarez.es.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.net.URL;

import static android.content.res.Configuration.ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private static final String API_POPULAR = "/movie/popular";
    private static final String API_TOP_RATED = "/movie/top_rated";

    private MyRecyclerViewAdapter adapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mMoviesGrid;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private String mApiUrl = API_POPULAR;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviesGrid = (RecyclerView) findViewById(R.id.movie_covers_rv);
        int orientation = getResources().getConfiguration().orientation;
        int numberOfColumns = (orientation == ORIENTATION_LANDSCAPE)? 3 : 2;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, numberOfColumns);
        mMoviesGrid.setLayoutManager(gridLayoutManager);
        adapter = new MyRecyclerViewAdapter(this);
        adapter.setClickListener(this);
        mMoviesGrid.setAdapter(adapter);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_tv);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_pb);

        mTitle = getResources().getString(R.string.most_popular_movies);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                loadMoviesData();
            }
        });

        loadMoviesData();
    }

    private void loadMoviesData() {
        showMoviesGridView();
        setTitle(mTitle);
        new FetchMoviesDataTask().execute(mApiUrl);
    }

    private void showMoviesGridView() {
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);
        mMoviesGrid.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mMoviesGrid.setVisibility(View.INVISIBLE);
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("movieModel", adapter.getItem(position));
        startActivity(intent);
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
            mApiUrl = API_POPULAR;
            mTitle = getResources().getString(R.string.most_popular_movies);
            loadMoviesData();
            return true;
        }

        if (id == R.id.action_top_rated) {
            mApiUrl = API_TOP_RATED;
            mTitle = getResources().getString(R.string.top_rated_movies);
            loadMoviesData();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public class FetchMoviesDataTask extends AsyncTask<String, Void, MovieModel[]> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mLoadingIndicator.setVisibility(View.VISIBLE);
        }

        @Override
        protected MovieModel[] doInBackground(String... params) {

            if(android.os.Debug.isDebuggerConnected())
                android.os.Debug.waitForDebugger();


            if (params.length == 0) {
                return null;
            }

            String serviceUrl = params[0];
            URL moviesRequestUrl = NetworkUtils.buildUrl(serviceUrl);

            try {
                String jsonMoviesResponse = NetworkUtils
                        .getResponseFromHttpUrl(moviesRequestUrl);

                MovieModel[] moviesData = MovieModel
                        .getModelsFromJson(jsonMoviesResponse);

                return moviesData;

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(MovieModel[] moviesData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (moviesData != null) {
                showMoviesGridView();
                adapter.setMoviesData(moviesData);
                mSwipeRefreshLayout.setRefreshing(false);
            } else {
                showErrorMessage();
            }
        }
    }
}
