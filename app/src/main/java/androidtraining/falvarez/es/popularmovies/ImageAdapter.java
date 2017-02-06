package androidtraining.falvarez.es.popularmovies;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {

    private static final String TAG = ImageAdapter.class.getSimpleName();

    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mPosterUrls.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;

        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
        } else {
            imageView = (ImageView) convertView;
        }

        Picasso.with(mContext).setLoggingEnabled(true);
        Picasso
                .with(mContext)
                .load(mPosterUrls[position])
                .into(imageView);

        return imageView;
    }

    private String[] mPosterUrls = {
        "http://image.tmdb.org/t/p/w500/WLQN5aiQG8wc9SeKwixW7pAR8K.jpg",
        "http://image.tmdb.org/t/p/w500/e1mjopzAS2KNsvpbpahQ1a6SkSn.jpg",
        "http://image.tmdb.org/t/p/w500/z09QAf8WbZncbitewNk6lKYMZsh.jpg",
        "http://image.tmdb.org/t/p/w500/hLudzvGfpi6JlwUnsNhXwKKg4j.jpg",
        "http://image.tmdb.org/t/p/w500/jjBgi2r5cRt36xF6iNUEhzscEcb.jpg",
        "http://image.tmdb.org/t/p/w500/ylXCdC106IKiarftHkcacasaAcb.jpg",
        "http://image.tmdb.org/t/p/w500/uSHjeRVuObwdpbECaXJnvyDoeJK.jpg",
        "http://image.tmdb.org/t/p/w500/yNsdyNbQqaKN0TQxkHMws2KLTJ6.jpg"
    };
}
