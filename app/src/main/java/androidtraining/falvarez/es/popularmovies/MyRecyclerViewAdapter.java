package androidtraining.falvarez.es.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = MyRecyclerViewAdapter.class.getSimpleName();

    private MovieModel[] movies;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context mContext;

    public MyRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.movies = null;
        this.mContext = context;
    }

    // inflates the cell layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_item, parent, false);
        int width = parent.getMeasuredWidth() / 2;
        view.setMinimumWidth(width);
        view.setMinimumHeight(width * 4 / 3);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the textview in each cell
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picasso
            .with(mContext)
            .load("http://image.tmdb.org/t/p/w780" + movies[position].getPosterUrl())
            .into(holder.myImageView);
    }

    // total number of cells
    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.length;
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.movie_cover_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    public MovieModel getItem(int id) {
        return movies[id];
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setMoviesData(MovieModel[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
