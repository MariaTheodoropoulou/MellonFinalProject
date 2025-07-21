package gr.android.moviesapp.ui.detailsScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import gr.android.moviesapp.databinding.ItemMovieBinding;
import gr.android.moviesapp.databinding.ItemSimilarMovieBinding;
import gr.android.moviesapp.domain.models.MovieUi;

public class SimilarMoviesAdapter extends ListAdapter<MovieUi, SimilarMoviesAdapter.MovieViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(MovieUi movie);
    }

    private final OnItemClickListener listener;

    public SimilarMoviesAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemSimilarMovieBinding binding = ItemSimilarMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        holder.bind(getItem(position));
    }

    static final DiffUtil.ItemCallback<MovieUi> DIFF_CALLBACK = new DiffUtil.ItemCallback<MovieUi>() {
        @Override
        public boolean areItemsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            return oldItem.equals(newItem);
        }
    };

    class MovieViewHolder extends RecyclerView.ViewHolder {
        private final ItemSimilarMovieBinding binding;

        public MovieViewHolder(ItemSimilarMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MovieUi movie) {
            Glide.with(binding.getRoot().getContext())
                    .load("https://image.tmdb.org/t/p/w342" + movie.getPosterPath())
                    .into(binding.moviePoster);

            itemView.setOnClickListener(v -> listener.onItemClick(movie));
        }
    }
}
