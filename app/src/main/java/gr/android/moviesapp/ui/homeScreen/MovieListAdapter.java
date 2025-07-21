package gr.android.moviesapp.ui.homeScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.time.LocalDateTime;

import gr.android.moviesapp.R;
import gr.android.moviesapp.common.DateTimeFormater;
import gr.android.moviesapp.databinding.ItemMovieBinding;
import gr.android.moviesapp.domain.models.MovieUi;

public class MovieListAdapter extends ListAdapter<MovieUi, MovieListAdapter.MovieViewHolder> {

    private OnFavoriteClickListener favoriteClickListener;
    private OnItemClickListener itemClickListener;
    private MovieViewModel viewModel;


    protected MovieListAdapter(OnFavoriteClickListener favoriteClickListener, OnItemClickListener itemClickListener, MovieViewModel viewModel) {
        super(new DiffCallback());
        this.favoriteClickListener = favoriteClickListener;
        this.itemClickListener = itemClickListener;
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public MovieViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemMovieBinding binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new MovieViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieViewHolder holder, int position) {
        MovieUi movie = getItem(position);
        holder.bind(movie);
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        private final ItemMovieBinding binding;

        MovieViewHolder(ItemMovieBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(MovieUi movie) {
            Glide.with(binding.backdropImage.getContext())
                    .load("https://image.tmdb.org/t/p/w780" + movie.getBackdrop_path())
                    .apply(new RequestOptions().placeholder(R.drawable.loading))
                    .into(binding.backdropImage);
            binding.movieTitle.setText(movie.getTitle());
            binding.movieRating.setRating((float) (movie.getVoteAverage() / 2));
            binding.date.setText(DateTimeFormater.formatDate(movie.getRelease_date()));

            if(movie.isFavorite()) {
                binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_favorite_selected));
            } else {
                binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(binding.getRoot().getContext(), R.drawable.ic_favorite_unselect));
            }

            binding.favoriteIcon.setOnClickListener(v -> {
                favoriteClickListener.onFavoriteClick(movie);
                viewModel.toggleFavorite(movie);
            });

            binding.getRoot().setOnClickListener( view -> {
                itemClickListener.onItemClick(movie.getId(), movie.isFavorite());
            });
        }
    }

    static class DiffCallback extends DiffUtil.ItemCallback<MovieUi> {
        @Override
        public boolean areItemsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.isFavorite() == newItem.isFavorite();
        }

        @Override
        public boolean areContentsTheSame(@NonNull MovieUi oldItem, @NonNull MovieUi newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.isFavorite() == newItem.isFavorite();
        }
    };

    public interface OnFavoriteClickListener {
        void onFavoriteClick(MovieUi movie);
    }

    public interface OnItemClickListener {
        void onItemClick(int movieId, boolean isFavorite);
    }
}

