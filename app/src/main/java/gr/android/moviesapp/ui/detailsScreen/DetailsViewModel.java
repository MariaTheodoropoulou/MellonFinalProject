package gr.android.moviesapp.ui.detailsScreen;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsAndCastUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.repoInterfaces.MovieDetailsRepository;
import gr.android.moviesapp.domain.usecase.DetailsUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class DetailsViewModel extends ViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private DetailsUseCase detailsUseCase;
    private MovieDetailsRepository movieDetailsRepository;
    private MutableLiveData<MovieDetailsWithReviewsAndCastUi> movieDetailsWithReviewsLiveData = new MutableLiveData<>();
    public LiveData<MovieDetailsWithReviewsAndCastUi> movieDetailsLiveData = movieDetailsWithReviewsLiveData;
    private final MutableLiveData<List<MovieUi>> _similarMovies = new MutableLiveData<>();
    public LiveData<List<MovieUi>> similarMovies = _similarMovies;

    @Inject
    public DetailsViewModel(DetailsUseCase detailsUseCase, MovieDetailsRepository movieDetailsRepository) {
        this.detailsUseCase = detailsUseCase;
        this.movieDetailsRepository = movieDetailsRepository;
    }


    public void fetchMovieDetailsWithReviewsAndCast(Integer movieId) {
        disposable.add(detailsUseCase.execute(movieId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        detailsWithReviews -> movieDetailsWithReviewsLiveData.postValue(detailsWithReviews),
                        throwable -> Log.e("MovieViewModel", "Error fetching movie details with reviews", throwable)
                ));
    }

    public void fetchSimilarMovies(int movieId) {
        disposable.add(
                movieDetailsRepository.getSimilarMovies(movieId)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(movies -> movies.stream().limit(6).collect(Collectors.toList()))
                        .subscribe(
                                movies -> _similarMovies.setValue(movies),
                                error -> Log.e("DetailsViewModel", "Error fetching similar movies", error)
                        )
        );
    }

    public void toggleFavorite(DetailsBasicUi movie) {
        MovieEntity movieEntity = new MovieEntity(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.isFavorite(),
                movie.getBackdropPath(),
                movie.getReleaseDate()
        );
    }

    @Override
    protected void onCleared() {
        disposable.clear();
        super.onCleared();
    }
}
