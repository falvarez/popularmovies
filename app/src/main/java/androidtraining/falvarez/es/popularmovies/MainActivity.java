package androidtraining.falvarez.es.popularmovies;

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
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    MyRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] posterUrls = {
                "http://image.tmdb.org/t/p/w780/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
                "http://image.tmdb.org/t/p/w780/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
                "http://image.tmdb.org/t/p/w780/z09QAf8WbZncbitewNk6lKYMZsh.jpg",
                "http://image.tmdb.org/t/p/w780/hLudzvGfpi6JlwUnsNhXwKKg4j.jpg",
                "http://image.tmdb.org/t/p/w780/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                "http://image.tmdb.org/t/p/w780/ylXCdC106IKiarftHkcacasaAcb.jpg",
                "http://image.tmdb.org/t/p/w780/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg",
                "http://image.tmdb.org/t/p/w780/yNsdyNbQqaKN0TQxkHMws2KLTJ6.jpg",
                "http://image.tmdb.org/t/p/w780/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
                "http://image.tmdb.org/t/p/w780/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
                "http://image.tmdb.org/t/p/w780/z09QAf8WbZncbitewNk6lKYMZsh.jpg",
                "http://image.tmdb.org/t/p/w780/hLudzvGfpi6JlwUnsNhXwKKg4j.jpg",
                "http://image.tmdb.org/t/p/w780/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                "http://image.tmdb.org/t/p/w780/ylXCdC106IKiarftHkcacasaAcb.jpg",
                "http://image.tmdb.org/t/p/w780/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg",
                "http://image.tmdb.org/t/p/w780/yNsdyNbQqaKN0TQxkHMws2KLTJ6.jpg",
                "http://image.tmdb.org/t/p/w780/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
                "http://image.tmdb.org/t/p/w780/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
                "http://image.tmdb.org/t/p/w780/z09QAf8WbZncbitewNk6lKYMZsh.jpg",
                "http://image.tmdb.org/t/p/w780/hLudzvGfpi6JlwUnsNhXwKKg4j.jpg",
                "http://image.tmdb.org/t/p/w780/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
                "http://image.tmdb.org/t/p/w780/ylXCdC106IKiarftHkcacasaAcb.jpg",
                "http://image.tmdb.org/t/p/w780/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg",
                "http://image.tmdb.org/t/p/w780/yNsdyNbQqaKN0TQxkHMws2KLTJ6.jpg",
                "http://image.tmdb.org/t/p/w780/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
                "http://image.tmdb.org/t/p/w780/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg"
        };

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.movie_covers_rv);
        int numberOfColumns = 2;
        recyclerView.setLayoutManager(new GridLayoutManager(this, numberOfColumns));
        adapter = new MyRecyclerViewAdapter(this, posterUrls);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        Log.i(TAG, "You clicked cover " + adapter.getItem(position) + ", which is at cell position " + position);
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
}
