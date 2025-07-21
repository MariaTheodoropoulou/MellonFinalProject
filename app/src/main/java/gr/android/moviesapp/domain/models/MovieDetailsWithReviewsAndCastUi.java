package gr.android.moviesapp.domain.models;


import java.util.List;

public class MovieDetailsWithReviewsAndCastUi {

    private final DetailsBasicUi movieDetails;
    private final List<ReviewsUi> reviews;
    private final List<CastUi> cast;

    public MovieDetailsWithReviewsAndCastUi(DetailsBasicUi movieDetails, List<ReviewsUi> reviews, List<CastUi> cast) {
        this.movieDetails = movieDetails;
        this.reviews = reviews;
        this.cast = cast;
    }

    public DetailsBasicUi getMovieDetails() {
        return movieDetails;
    }

    public List<ReviewsUi> getReviews() {
        return reviews;
    }

    public List<CastUi> getCast() {return cast;}
}
