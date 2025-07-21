package gr.android.moviesapp.ui.detailsScreen;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;

import java.util.List;
import java.util.stream.Collectors;

import dagger.hilt.android.AndroidEntryPoint;
import gr.android.moviesapp.R;
import gr.android.moviesapp.common.DateTimeFormater;
import gr.android.moviesapp.common.TimeFormater;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.databinding.DetailsLayoutBinding;
import gr.android.moviesapp.domain.models.CastUi;
import gr.android.moviesapp.domain.models.GenreUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsAndCastUi;
import gr.android.moviesapp.domain.models.ReviewsUi;

@AndroidEntryPoint
public class DetailsScreen extends Fragment {

    private DetailsLayoutBinding binding;
    private DetailsViewModel detailsViewModel;
    private ReviewAdapter reviewAdapter;
    private SimilarMoviesAdapter similarMoviesAdapter;
    private MovieDao movieDao;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        binding = DetailsLayoutBinding.inflate(inflater);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        detailsViewModel = new ViewModelProvider(this).get(DetailsViewModel.class);

        if (getArguments() != null) {
            int movieId = getArguments().getInt("MOVIE_ID");
            if (movieId != -1) {
                detailsViewModel.fetchMovieDetailsWithReviewsAndCast(movieId);
                detailsViewModel.fetchSimilarMovies(movieId);
            }
        }

        initView();
        initObservers();
    }

    private void initView() {
        binding.backButton.setOnClickListener(v ->
                NavHostFragment.findNavController(this).popBackStack()
        );
        reviewAdapter = new ReviewAdapter();
        binding.reviewsRecyclerView.setLayoutManager(
                new LinearLayoutManager(getContext()));
        binding.reviewsRecyclerView.setAdapter(reviewAdapter);

        similarMoviesAdapter = new SimilarMoviesAdapter(movie -> {
            Bundle bundle = new Bundle();
            bundle.putInt("MOVIE_ID", movie.getId());
            bundle.putBoolean("IS_FAVORITE", movie.isFavorite());
            NavHostFragment.findNavController(this).navigate(R.id.action_detailsScreen_self, bundle);
        });

        binding.similarRecyclerView.setLayoutManager(new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.similarRecyclerView.setAdapter(similarMoviesAdapter);
    }

    private void initObservers() {
        detailsViewModel.movieDetailsLiveData.observe(getViewLifecycleOwner(), new Observer<MovieDetailsWithReviewsAndCastUi>() {
            @Override
            public void onChanged(@Nullable MovieDetailsWithReviewsAndCastUi movieDetailsWithReviewsAndCastUi) {
                if (movieDetailsWithReviewsAndCastUi != null) {
                    boolean isFav = getArguments().getBoolean("IS_FAVORITE", false);
                    movieDetailsWithReviewsAndCastUi.getMovieDetails().setFavorite(isFav);
                    updateUI(movieDetailsWithReviewsAndCastUi);
                }
            }
        });
        detailsViewModel.similarMovies.observe(getViewLifecycleOwner(), list -> {
            similarMoviesAdapter.submitList(list);
        });
    }

    private void updateUI(MovieDetailsWithReviewsAndCastUi movieDetailsWithReviewsAndCastUi) {
        //-----Image----------
        Glide.with(requireContext())
                .load("https://image.tmdb.org/t/p/w780" + movieDetailsWithReviewsAndCastUi.getMovieDetails().getBackdropPath())
                .into(binding.movieImage);
        //----Movie Title-------
        binding.movieTitle.setText(movieDetailsWithReviewsAndCastUi.getMovieDetails().getTitle());
        //-----Genre---------
        List<GenreUi> genres = movieDetailsWithReviewsAndCastUi.getMovieDetails().getGenres();
        //-------Categories--------
        binding.movieCategories.setText(
                genres.stream()
                        .map(GenreUi::getName)
                        .collect(Collectors.joining(", "))
        );
        //------Movie Release Date------
        binding.movieDate.setText(DateTimeFormater.formatDate(movieDetailsWithReviewsAndCastUi.getMovieDetails().getReleaseDate()));
        //------Movie Rating ----
        binding.movieRating.setRating((float) (movieDetailsWithReviewsAndCastUi.getMovieDetails().getVoteAverage() / 2));
        //------Movie Runtime----
        int runtime = movieDetailsWithReviewsAndCastUi.getMovieDetails().getRuntime();
        binding.RuntimeDurationText.setText(TimeFormater.formatTime(runtime));
        //------Movie Description -----
        binding.descriptionText.setText(movieDetailsWithReviewsAndCastUi.getMovieDetails().getOverview());


        String homepageUrl = movieDetailsWithReviewsAndCastUi.getMovieDetails().getHomepage();
        String movieTitle = movieDetailsWithReviewsAndCastUi.getMovieDetails().getTitle();

        if (homepageUrl != null && !homepageUrl.isEmpty()) {
            binding.shareButton.setVisibility(View.VISIBLE);
            binding.shareButton.setOnClickListener(v -> {
                String shareMessage = movieTitle + "\n" +
                        "Δες περισσότερα εδώ " + homepageUrl;

                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Movie Suggestion");
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);

                startActivity(Intent.createChooser(shareIntent, "Κοινοποίηση μέσω..."));
            });
        } else {
            binding.shareButton.setVisibility(View.GONE);
        }

        //------Movie Reviews--------
        List<ReviewsUi> reviews = movieDetailsWithReviewsAndCastUi.getReviews()
                .stream()
                .limit(3)
                .collect(Collectors.toList());
        reviewAdapter.submitList(reviews);

        //------Movie Cast ------------
        List<CastUi> castList = movieDetailsWithReviewsAndCastUi.getCast();
        if (castList != null && !castList.isEmpty()) {
            List<CastUi> limitedCast = castList.stream()
                    .limit(8)
                    .collect(Collectors.toList());

            String castNames = limitedCast.stream()
                    .map(CastUi::getName)
                    .collect(Collectors.joining(", "));

            binding.castText.setText(castNames);
        } else {
            binding.castText.setText("No cast information");
        }
        binding.favoriteIcon.setVisibility(View.VISIBLE);
        boolean isFavorite = movieDetailsWithReviewsAndCastUi.getMovieDetails().isFavorite();
        binding.favoriteIcon.setImageDrawable(ContextCompat.getDrawable(
                requireContext(),
                isFavorite ? R.drawable.ic_favorite_selected : R.drawable.ic_favorite_unselect
        ));
    }
}

