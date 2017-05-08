package androidtraining.falvarez.es.popularmovies;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.RequestCreator;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private MovieModel[] movies;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    private Context mContext;

    public MyRecyclerViewAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.movies = null;
        this.mContext = context;
    }

    @Override
    /**
     * Inflates the cell layout from XML when needed
     */
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.grid_item, parent, false);
        int width = parent.getMeasuredWidth() / MainActivity.getGridNumberOfColumns(mContext);
        view.setMinimumWidth(width);
        view.setMinimumHeight(width * 4 / 3);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    /**
     * Binds the data to the text view in each cell
     */
    public void onBindViewHolder(ViewHolder holder, int position) {
        // TODO manage errors
        RequestCreator requestCreator = Picasso
                .with(mContext)
                .load(movies[position].getPosterFullUrl(MovieModel.MEASURE_W342));
        requestCreator.fetch();
        requestCreator.into(holder.myImageView);
        holder.myImageView.setContentDescription(movies[position].getTitle());
    }

    @Override
    public int getItemCount() {
        if (movies == null) {
            return 0;
        }
        return movies.length;
    }

    /**
     * Stores and recycles views as they are scrolled off screen
     */
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView myImageView;

        public ViewHolder(View itemView) {
            super(itemView);
            myImageView = (ImageView) itemView.findViewById(R.id.movie_cover_iv);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public MovieModel getItem(int id) {
        return movies[id];
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public void setMoviesData(MovieModel[] movies) {
        this.movies = movies;
        notifyDataSetChanged();
    }
}
