package se.michaelthelin.spotify.methods;

import com.google.common.util.concurrent.SettableFuture;
import net.sf.json.JSONObject;
import se.michaelthelin.spotify.JsonUtil;
import se.michaelthelin.spotify.exceptions.EmptyResponseException;
import se.michaelthelin.spotify.exceptions.NoCredentialsException;
import se.michaelthelin.spotify.exceptions.WebApiException;
import se.michaelthelin.spotify.models.Track;

import java.io.IOException;

public class TrackRequest extends AbstractRequest {

  public TrackRequest(Builder builder) {
    super(builder);
  }

  public SettableFuture<Track> getAsync() {
    SettableFuture<Track> trackFuture = SettableFuture.create();

    try {
      final String jsonString = getJson();
      final JSONObject jsonObject = JSONObject.fromObject(jsonString);

      throwIfErrorsInResponse(jsonObject);

      trackFuture.set(JsonUtil.createTrack(jsonString));
    } catch (Exception e) {
      trackFuture.setException(e);
    }

    return trackFuture;
  }

  public Track get() throws IOException, WebApiException {
    final String jsonString = getJson();
    final JSONObject jsonObject = JSONObject.fromObject(jsonString);

    throwIfErrorsInResponse(jsonObject);

    return JsonUtil.createTrack(getJson());
  }

  public static Builder builder() {
    return new Builder();
  }


  public static final class Builder extends AbstractRequest.Builder<Builder> {

    /**
     * The track with the given id.
     *
     * @param id The id for the track.
     * @return Track Request
     */
    public Builder id(String id) {
      assert (id != null);
      return path(String.format("/v1/tracks/%s", id));
    }

    public TrackRequest build() {
      return new TrackRequest(this);
    }

  }

}
