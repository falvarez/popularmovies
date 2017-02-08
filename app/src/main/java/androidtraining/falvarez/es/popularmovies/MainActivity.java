package androidtraining.falvarez.es.popularmovies;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URL;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private MyRecyclerViewAdapter adapter;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private RecyclerView mMoviesGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMoviesGrid = (RecyclerView) findViewById(R.id.movie_covers_rv);
        int numberOfColumns = 2;
        mMoviesGrid.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this);
        adapter.setClickListener(this);
        mMoviesGrid.setAdapter(adapter);
        mErrorMessageDisplay = (TextView) findViewById(R.id.error_message_tv);
        mLoadingIndicator = (ProgressBar) findViewById(R.id.loading_indicator_pb);

        loadMoviesData();
    }

    private void loadMoviesData() {
        showMoviesGridView();

        new FetchMoviesDataTask().execute("/movie/popular");
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
            return true;
        }

        if (id == R.id.action_top_rated) {
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
            } else {
                showErrorMessage();
            }
        }
    }
}
