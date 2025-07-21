package gr.android.moviesapp.domain.models;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class MovieUi {
    private int id;

    private String title;

    private String overview;

    private String posterPath;

    private String backdrop_path;

    private double voteAverage;
    private String release_date;

    private boolean favorite;

    public MovieUi(int id, String title, String overview, String posterPath, double voteAverage, boolean isFavorite, String backdrop_path, String release_date) {
        this.id = id;
        this.title = title;
        this.overview = overview;
        this.posterPath = posterPath;
        this.voteAverage = voteAverage;
        this.favorite = isFavorite;
        this.backdrop_path = backdrop_path;
        this.release_date = release_date;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public String getRelease_date() {
        return release_date;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        MovieUi movie = (MovieUi) obj;
        return id == movie.id &&
                Double.compare(movie.voteAverage, voteAverage) == 0 &&
                favorite == movie.favorite &&
                Objects.equals(title, movie.title) &&
                Objects.equals(posterPath, movie.posterPath) &&
                Objects.equals(backdrop_path, movie.backdrop_path) &&
                Objects.equals(release_date, movie.release_date) &&
                Objects.equals(overview, movie.overview);
    }


    @Override
    public int hashCode() {
        return Objects.hash(id, title, posterPath, backdrop_path, voteAverage, release_date, overview, favorite);
    }
}

