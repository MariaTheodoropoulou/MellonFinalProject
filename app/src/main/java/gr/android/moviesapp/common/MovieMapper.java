package gr.android.moviesapp.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import gr.android.moviesapp.data.model.details.basicDetails.GenreRemote;
import gr.android.moviesapp.data.model.details.basicDetails.MovieDetailsRemote;
import gr.android.moviesapp.data.model.details.cast.CastRemote;
import gr.android.moviesapp.data.model.details.reviews.ReviewsRemote;
import gr.android.moviesapp.data.model.home.MovieRemote;
import gr.android.moviesapp.domain.models.CastUi;
import gr.android.moviesapp.domain.models.DetailsBasicUi;
import gr.android.moviesapp.domain.models.GenreUi;
import gr.android.moviesapp.domain.models.MovieUi;
import gr.android.moviesapp.domain.models.ReviewsUi;

public class MovieMapper {
    public static MovieUi mapToUiMovie(MovieRemote movie) {
        return new MovieUi(
                movie.getId(),
                movie.getTitle(),
                movie.getOverview(),
                movie.getPosterPath(),
                movie.getVoteAverage(),
                movie.isFavorite(),
                movie.getBackdrop_path(),
                movie.getRelease_date()
        );
    }

    public static List<MovieUi> mapToUiMovieList(List<MovieRemote> movies) {
        List<MovieUi> uiMovies = new ArrayList<>();
        for (MovieRemote movie : movies) {
            uiMovies.add(mapToUiMovie(movie));
        }
        return uiMovies;
    }

    public static CastUi mapToUiCast(CastRemote remote) {
        return new CastUi(
                remote.isAdult(),
                remote.getGender(),
                remote.getId(),
                remote.getKnown_for_department(),
                remote.getName(),
                remote.getOriginal_name(),
                remote.getPopularity(),
                remote.getProfile_path(),
                remote.getCast_id(),
                remote.getCharacter(),
                remote.getCredit_id(),
                remote.getOrder()
        );
    }

    public static ReviewsUi mapToUiModel(ReviewsRemote remote) {
        return new ReviewsUi(
                remote.getAuthor(),
                remote.getContent(),
                remote.getCreatedAt(),
                remote.getId(),
                remote.getUpdatedAt(),
                remote.getUrl()
        );
    }

    public static GenreUi mapToUiModel(GenreRemote remote) {
        return new GenreUi(
                remote.getId(),
                remote.getName()
        );
    }

    public static DetailsBasicUi mapToUiModel(MovieDetailsRemote remote) {
        return new DetailsBasicUi(
                remote.getBackdropPath(),
                remote.getGenres().stream()
                        .map(MovieMapper::mapToUiModel)
                        .collect(Collectors.toList()),
                remote.getId(),
                remote.getOverview(),
                remote.getPosterPath() != null ? remote.getPosterPath().toString() : null,
                remote.getReleaseDate(),
                remote.getTitle(),
                remote.getVoteAverage(),
                remote.getHomepage(),
                remote.getRuntime(),
                remote.isFavorite()
        );
    }
}

