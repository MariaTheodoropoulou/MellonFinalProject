package gr.android.moviesapp.ui.detailsScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import gr.android.moviesapp.databinding.DetailsLayoutBinding;
import gr.android.moviesapp.databinding.ItemReviewBinding;
import gr.android.moviesapp.domain.models.ReviewsUi;

public class ReviewAdapter extends ListAdapter<ReviewsUi, ReviewAdapter.ReviewsViewHolder> {

    protected ReviewAdapter() {
        super(new DiffCallback());
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemReviewBinding binding = ItemReviewBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ReviewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        ReviewsUi reviewsUi = getItem(position);
        holder.bind(reviewsUi);
    }

    class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private final ItemReviewBinding binding;

        ReviewsViewHolder(ItemReviewBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(ReviewsUi reviewsUi){
            binding.reviewsAuthor.setText(reviewsUi.getAuthor());
            binding.reviews.setText(reviewsUi.getContent());
        }
    }

    static class DiffCallback extends DiffUtil.ItemCallback<ReviewsUi> {
        @Override
        public boolean areItemsTheSame(@NonNull ReviewsUi oldItem, @NonNull ReviewsUi newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getUpdatedAt() == newItem.getUpdatedAt();
        }

        @Override
        public boolean areContentsTheSame(@NonNull ReviewsUi oldItem, @NonNull ReviewsUi newItem) {
            return oldItem.getId().equals(newItem.getId()) && oldItem.getUpdatedAt().equals(newItem.getUpdatedAt());
        }
    }
}
