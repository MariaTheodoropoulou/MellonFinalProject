package gr.android.moviesapp.ui.homeScreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.R;
import gr.android.moviesapp.databinding.FragmentMovieBinding;

@AndroidEntryPoint
public class MovieListFragment extends Fragment {

    private MovieViewModel viewModel;
    private MovieListAdapter adapter;
    private FragmentMovieBinding binding;
    private SkeletonAdapter skeletonAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentMovieBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        skeletonAdapter = new SkeletonAdapter(10); // ✅ 10 skeleton items

        adapter = new MovieListAdapter(
                movie -> viewModel.toggleFavorite(movie),
                (movieId, isfavorite) -> {
                    Bundle bundle = new Bundle();
                    bundle.putInt("MOVIE_ID", movieId);
                    bundle.putBoolean("IS_FAVORITE", isfavorite);
                    Navigation.findNavController(binding.getRoot()).navigate(R.id.action_movieListFragment_to_detailsScreen, bundle);
                },
                viewModel
        );

        viewModel = new ViewModelProvider(this).get(MovieViewModel.class);

        // ✅ Switch μεταξύ skeleton και κανονικού adapter
        viewModel.getIsLoading().observe(getViewLifecycleOwner(), isLoading -> {
            if (isLoading) {
                binding.recyclerView.setAdapter(skeletonAdapter);
            } else {
                binding.recyclerView.setAdapter(adapter);
            }
        });

        viewModel.getMovies().observe(getViewLifecycleOwner(), movies -> {
            adapter.submitList(movies);
            binding.swipeRefreshLayout.setRefreshing(false);
        });

        binding.swipeRefreshLayout.setOnRefreshListener(() -> {
            binding.swipeRefreshLayout.setRefreshing(true);
            viewModel.swipeRefreshMovies();
        });

        binding.recyclerView.setAdapter(skeletonAdapter);
    }
}

