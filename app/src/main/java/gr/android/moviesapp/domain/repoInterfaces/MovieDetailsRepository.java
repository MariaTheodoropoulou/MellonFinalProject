package gr.android.moviesapp.domain.repoInterfaces;

import java.util.List;

import gr.android.moviesapp.domain.models.CastUi;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public interface MovieDetailsRepository {

    Observable<DetailsBasicUi> getMovieById(Integer movie_id);
    Observable<List<ReviewsUi>> getReviewsById(Integer movie_id);
    Observable<List<CastUi>> getCastById(Integer movie_id);
    Single<List<MovieUi>> getSimilarMovies(Integer movie_id);
}
