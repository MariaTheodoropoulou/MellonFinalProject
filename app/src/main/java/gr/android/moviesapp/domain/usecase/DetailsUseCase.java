package gr.android.moviesapp.domain.usecase;

import java.util.List;

import javax.inject.Inject;

import gr.android.moviesapp.domain.models.CastUi;
import gr.android.moviesapp.domain.repoInterfaces.MovieDetailsRepository;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieDetailsWithReviewsAndCastUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;

public class DetailsUseCase {

    private MovieDetailsRepository movieDetailsRepository;

    @Inject
    public DetailsUseCase(MovieDetailsRepository movieDetailsRepository) {
        this.movieDetailsRepository = movieDetailsRepository;
    }

    public Observable<MovieDetailsWithReviewsAndCastUi> execute(Integer movieId) {
        Observable<DetailsBasicUi> movieDetailsObservable = movieDetailsRepository.getMovieById(movieId);
        Observable<List<ReviewsUi>> reviewsObservable = movieDetailsRepository.getReviewsById(movieId);
        Observable<List<CastUi>> castObservable = movieDetailsRepository.getCastById(movieId);

        return Observable.combineLatest(
                movieDetailsObservable,
                reviewsObservable,
                castObservable,
                MovieDetailsWithReviewsAndCastUi::new
        );
    }

}
