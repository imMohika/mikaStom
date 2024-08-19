package ir.mohika.mikastom.core.web.api.ashcon;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.core.web.HttpClient;
import java.io.IOException;
import java.util.Optional;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class AshconApi {
  public static @NotNull Optional<AshconApiResponse> getPlayer(String username) {
    System.out.println("requesting player skin");
    Request request =
        new Request.Builder().url("https://api.ashcon.app/mojang/v2/user/" + username).build();
    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (response.isSuccessful()) {
        if (response.body() == null) {
          return Optional.empty();
        }

        String body = response.body().string();
        ObjectMapper mapper = new ObjectMapper();

        AshconApiResponse apiResponse = mapper.readValue(body, AshconApiResponse.class);

        return Optional.of(apiResponse);
      }
    } catch (IOException e) {
      Log.getLogger().error("Error getting {}'s skin from ashcon", username, e);
    }

    return Optional.empty();
  }
}
