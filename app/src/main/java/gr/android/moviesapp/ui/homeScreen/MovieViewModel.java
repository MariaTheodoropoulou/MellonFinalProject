package gr.android.moviesapp.ui.homeScreen;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import gr.android.moviesapp.common.MovieEntityMapper;
import gr.android.moviesapp.common.MovieMapper;
import gr.android.moviesapp.domain.repoInterfaces.MovieRepository;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.usecase.DetailsUseCase;
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@HiltViewModel
public class MovieViewModel extends ViewModel {
    private MovieRepository repository;
    private CompositeDisposable disposable = new CompositeDisposable();
    private final ExecutorService executorService;

    private Boolean hasError = false;
    private boolean firstLoadDone = false;
    private MutableLiveData<List<MovieUi>> moviesLiveData = new MutableLiveData<>();
private MutableLiveData<Boolean> isLoading = new MutableLiveData<>(true); // ✅

    public LiveData<Boolean> getIsLoading() { // ✅
        return isLoading;
    }

    @Inject
    public MovieViewModel(MovieRepository repository) {
        this.repository = repository;
        this.executorService = Executors.newSingleThreadExecutor();
        observeDatabaseChanges();
    }


    public LiveData<List<MovieUi>> getMovies() {
        refreshMovies();
        return moviesLiveData;
    }

    private void refreshMovies() {
        //------skeleton-----//
        isLoading.postValue(true); // ✅ φόρτωση ξεκινά

        disposable.add(repository.getPopularMovies()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(MovieMapper::mapToUiMovieList)
                .subscribe(
                        movies -> {
                            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                                moviesLiveData.postValue(movies);
                                isLoading.postValue(false);  // δείξε τα κανονικά
                            }, 2000); // καθυστέρηση 2s για να εμφανιστεί ο skeleton
                        },
                        throwable -> {
                            Log.e("MovieViewModel", "Error fetching movies", throwable);
                            hasError = true;
                            isLoading.postValue(false); // ✅ και στο error
                            observeDatabaseChanges();
                        }
                ));
    }

    private void observeDatabaseChanges() {
        LiveData<List<MovieEntity>> allMovies = repository.getAllMovies();
        allMovies.observeForever(movieEntities -> {
            List<MovieUi> dbMovies = MovieEntityMapper.mapToUiMovieList(movieEntities);
            List<MovieUi> currentMovies = moviesLiveData.getValue();
            if (currentMovies == null) {
                currentMovies = new ArrayList<>();
            }

            Map<Integer, MovieUi> currentMoviesMap = new HashMap<>();
            for (MovieUi movie : currentMovies) {
                currentMoviesMap.put(movie.getId(), movie);
            }

            ArrayList<MovieUi> newMovieList = new ArrayList<>(currentMovies);

            if (!hasError) {
                for (MovieUi dbMovie : dbMovies) {
                    MovieUi currentMovie = currentMoviesMap.get(dbMovie.getId());
                    if (currentMovie != null) {
                        if (dbMovie.isFavorite() != currentMovie.isFavorite()) {
                            // Create a new instance of MovieUi with the updated favorite status
                            MovieUi updatedMovie = new MovieUi(
                                    currentMovie.getId(),
                                    currentMovie.getTitle(),
                                    currentMovie.getOverview(),
                                    currentMovie.getPosterPath(),
                                    currentMovie.getVoteAverage(),
                                    dbMovie.isFavorite(),
                                    dbMovie.getBackdrop_path(),
                                    dbMovie.getRelease_date());
                            int index = newMovieList.indexOf(currentMovie);
                            newMovieList.set(index, updatedMovie);
                        }
                    }
                }
            } else {
                newMovieList = new ArrayList<>(dbMovies);
            }

            moviesLiveData.postValue(newMovieList);
        });
    }

    public void swipeRefreshMovies() {
        refreshMovies();
    }

    public void toggleFavorite(MovieUi movie) {
        executorService.execute(() -> {
            repository.toggleFavorite(MovieEntityMapper.mapToMovieEntity(movie));
            refreshMovies();
        });

    }

    @Override
    protected void onCleared() {
        disposable.dispose();
        super.onCleared();
    }
}

