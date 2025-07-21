package gr.android.moviesapp.ui.homeScreen;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import gr.android.moviesapp.databinding.ItemMovieSkeletonBinding;


public class SkeletonAdapter extends RecyclerView.Adapter<SkeletonAdapter.SkeletonViewHolder> {

    private ItemMovieSkeletonBinding binding;
    private final int itemCount;

    public SkeletonAdapter(int itemCount) {
        this.itemCount = itemCount;
    }

    @NonNull
    @Override
    public SkeletonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemMovieSkeletonBinding binding = ItemMovieSkeletonBinding.inflate(inflater, parent, false);
        return new SkeletonViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SkeletonViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        return itemCount;
    }

    static class SkeletonViewHolder extends RecyclerView.ViewHolder {

        ItemMovieSkeletonBinding binding;

        public SkeletonViewHolder(@NonNull ItemMovieSkeletonBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}