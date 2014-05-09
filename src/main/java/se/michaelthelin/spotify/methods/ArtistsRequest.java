package se.michaelthelin.spotify.methods;

import com.google.common.base.Joiner;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import com.google.common.util.concurrent.SettableFuture;
import net.sf.json.JSONObject;
import se.michaelthelin.spotify.JsonUtil;
import se.michaelthelin.spotify.SpotifyProtos.Artist;
import se.michaelthelin.spotify.exceptions.BadFieldException;
import se.michaelthelin.spotify.exceptions.NotFoundException;
import se.michaelthelin.spotify.exceptions.UnexpectedResponseException;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

public class ArtistsRequest extends AbstractRequest {

  protected ArtistsRequest(Builder builder) {
    super(builder);
  }

  public static Builder builder() {
    return new Builder();
  }

  public SettableFuture<List<Artist>> getArtistsAsync() {
    SettableFuture<List<Artist>> artistsFuture = SettableFuture.create();

    try {
      String jsonString = getJson();
      JSONObject jsonObject = JSONObject.fromObject(jsonString);
      if (errorInJson(jsonObject)) {
        Exception exception = getExceptionFromJson(jsonObject);
        artistsFuture.setException(exception);
      } else {
        artistsFuture.set(JsonUtil.createArtists(getJson()));
      }
    } catch (IOException e) {
      artistsFuture.setException(e);
    } catch (UnexpectedResponseException e) {
      artistsFuture.setException(e);
    }

    return artistsFuture;
  }

  public List<Artist> getArtists() throws IOException, UnexpectedResponseException, NotFoundException, BadFieldException {
    String jsonString = getJson();
    JSONObject jsonObject = JSONObject.fromObject(jsonString);
    throwIfErrorsInResponse(jsonObject);
    return JsonUtil.createArtists(getJson());
  }

  public static final class Builder extends AbstractRequest.Builder<Builder> {

    public Builder id(String... ids) {
      assert (ids != null);
      String idsParameter = Joiner.on(",").join(ids).toString();
      path("/v1/artists");
      return parameter("ids", idsParameter);
    }

    public ArtistsRequest build() {
      return new ArtistsRequest(this);
    }

  }
}