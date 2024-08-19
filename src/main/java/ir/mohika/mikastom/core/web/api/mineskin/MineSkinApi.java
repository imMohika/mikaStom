package ir.mohika.mikastom.core.web.api.mineskin;

import com.google.gson.Gson;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.core.utils.UUIDUtils;
import ir.mohika.mikastom.core.web.HttpClient;
import ir.mohika.mikastom.core.web.api.mineskin.responses.UUIDApiResponse;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

public class MineSkinApi {
  public static @NotNull Optional<UUID> getPlayerUUID(String username) {
    Request request =
        new Request.Builder().url("https://api.mineskin.org/validate/name/" + username).build();
    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (response.isSuccessful()) {
        if (response.body() == null) {
          return Optional.empty();
        }
        String body = response.body().string();
        Gson gson = new Gson();
        UUIDApiResponse apiResponse = gson.fromJson(body, UUIDApiResponse.class);

        if (!apiResponse.isValid()) {
          return Optional.empty();
        }

        UUID uuid = UUID.fromString(UUIDUtils.formatUUID(apiResponse.getUuid()));
        return Optional.of(uuid);
      }
    } catch (IOException e) {
      Log.getLogger().error("Error getting {}'s skin from mineskin", username, e);
    }

    return Optional.empty();
  }
}
