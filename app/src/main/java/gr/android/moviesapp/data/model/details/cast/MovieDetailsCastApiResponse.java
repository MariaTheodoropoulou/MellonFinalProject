package gr.android.moviesapp.data.model.details.cast;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieDetailsCastApiResponse {
    @SerializedName("cast")
    private List<CastRemote> cast;

    public List<CastRemote> getCast() {
        return cast;
    }
}
