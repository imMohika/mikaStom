package ir.mohika.mikastom.api.mineskin;

import com.google.gson.Gson;
import ir.mohika.mikastom.api.mineskin.responses.UUIDApiResponse;
import ir.mohika.mikastom.http.HttpClient;
import ir.mohika.mikastom.utils.UUIDUtils;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class MineSkinApi {
  public static Optional<UUID> getPlayerUUID(String username) {
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
      e.printStackTrace();
    }

    return Optional.empty();
  }
}
