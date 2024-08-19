package ir.mohika.mikastom.core.web.api.minetools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mohika.mikastom.core.utils.Log;
import ir.mohika.mikastom.core.utils.UUIDUtils;
import ir.mohika.mikastom.core.web.HttpClient;
import java.io.IOException;
import java.util.Optional;
import java.util.UUID;
import okhttp3.Request;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class MineToolsApi {
  public static CompletableFuture<Optional<MineToolsProfile>> getPlayerProfile(String username) {
    return getPlayerUUID(username)
        .thenCompose(
            uuidOptional ->
                uuidOptional
                    .map(MineToolsApi::getPlayerProfile)
                    .orElseGet(() -> CompletableFuture.completedFuture(Optional.empty())));
  }

  public static CompletableFuture<Optional<UUID>> getPlayerUUID(String username) {
    Request request =
        new Request.Builder().url("https://api.minetools.eu/uuid/" + username).build();

    CompletableFuture<Optional<UUID>> future = new CompletableFuture<>();
    HttpClient.getClient()
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(Call call, IOException e) {
                Log.getLogger().error("Error getting uuid for {}", username, e);
                future.complete(Optional.empty());
              }

              @Override
              public void onResponse(@NotNull Call call, @NotNull Response response)
                  throws IOException {
                if (!response.isSuccessful()) {
                  Log.getLogger().error("Fetching uuid for {} gave {}", username, response.code());
                  future.complete(Optional.empty());
                  return;
                }

                if (response.body() == null) {
                  Log.getLogger().error("UUID Response body for {} is empty", username);
                  future.complete(Optional.empty());
                  return;
                }

                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(body);
                String uuid = rootNode.path("id").asText();

                if (uuid.isEmpty()) {
                  Log.getLogger().error("UUID of {} is empty", username);
                  future.complete(Optional.empty());
                  return;
                }

                future.complete(Optional.of(UUID.fromString(UUIDUtils.formatUUID(uuid))));
              }
            });
    return future;
  }

  public static CompletableFuture<Optional<MineToolsProfile>> getPlayerProfile(@NotNull UUID uuid) {
    Request request = new Request.Builder().url("https://api.minetools.eu/profile/" + uuid).build();

    CompletableFuture<Optional<MineToolsProfile>> future = new CompletableFuture<>();
    HttpClient.getClient()
        .newCall(request)
        .enqueue(
            new Callback() {
              @Override
              public void onFailure(@NotNull Call call, IOException e) {
                Log.getLogger().error("Error getting profile for {}", uuid, e);
                future.complete(Optional.empty());
              }

              @Override
              public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                  Log.getLogger().error("Fetching profile for {} gave {}", uuid, response.code());
                  future.complete(Optional.empty());
                  return;
                }

                if (response.body() == null) {
                  Log.getLogger().error("Profile Response body for {} is empty", uuid);
                  future.complete(Optional.empty());
                  return;
                }

                String body = response.body().string();
                ObjectMapper mapper = new ObjectMapper();
                MineToolsProfile apiResponse = mapper.readValue(body, MineToolsProfile.class);
                future.complete(Optional.of(apiResponse));
              }
            });
    return future;
  }
}
