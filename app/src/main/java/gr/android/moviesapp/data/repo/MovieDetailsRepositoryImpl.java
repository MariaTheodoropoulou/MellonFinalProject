package gr.android.moviesapp.data.repo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import gr.android.moviesapp.common.MovieMapper;
import gr.android.moviesapp.data.database.MovieDao;
import gr.android.moviesapp.data.database.MovieEntity;
import gr.android.moviesapp.data.model.details.cast.CastRemote;
import gr.android.moviesapp.data.model.details.cast.MovieDetailsCastApiResponse;
import gr.android.moviesapp.data.model.details.reviews.MovieDetailsReviewApiResponse;
import gr.android.moviesapp.data.networkServices.MovieApiService;
import gr.android.moviesapp.domain.models.CastUi;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;
import gr.android.moviesapp.domain.repoInterfaces.MovieDetailsRepository;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class MovieDetailsRepositoryImpl implements  MovieDetailsRepository {

    private MovieApiService apiService;
    private final MovieDao movieDao;
    public MovieDetailsRepositoryImpl(MovieApiService apiService, MovieDao movieDao) {
        this.apiService = apiService;
        this.movieDao = movieDao;
    }

    @Override
    public Observable<DetailsBasicUi> getMovieById(Integer movie_id) {
        return apiService.getMovieById(movie_id)
                .map(MovieMapper::mapToUiModel)
                .flatMap(detailsFromApi -> Observable.fromCallable(() -> {
                    try {
                        MovieEntity entity = movieDao.getMovieById(movie_id);
                        if (entity != null) {
                            detailsFromApi.setFavorite(entity.isFavorite());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return detailsFromApi;
                }));
    }

    @Override
    public Observable<List<ReviewsUi>> getReviewsById(Integer movie_id) {
        return apiService.getReviewsById(movie_id)
                .map(MovieDetailsReviewApiResponse::getResults)
                .map(movies ->
                    movies.stream()
                            .map(MovieMapper::mapToUiModel)
                            .collect(Collectors.toList())
                );
    }

    public Observable<List<CastUi>> getCastById(Integer movie_id) {
        return apiService.getCastById(movie_id)
                .map(resp -> {
                    List<CastRemote> list = (resp == null) ? null : resp.getCast();
                    if (list == null || list.isEmpty()) return List.of();
                    return list.stream()
                            .map(MovieMapper::mapToUiCast)
                            .filter(Objects::nonNull)
                            .collect(Collectors.toList());
                });
    }

    @Override
    public Single<List<MovieUi>> getSimilarMovies(Integer movie_id) {
        return apiService.getSimilarMovies(movie_id)
                .map(response -> MovieMapper.mapToUiMovieList(response.getResults()));
    }
}
