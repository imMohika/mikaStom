package ir.mohika.mikastom.api.minetools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mohika.mikastom.http.HttpClient;
import ir.mohika.mikastom.utils.Log;
import ir.mohika.mikastom.utils.UUIDUtils;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class MineToolsApi {
  public static Optional<MineToolsProfile> getPlayerProfile(String username) {
    Optional<UUID> uuid = getPlayerUUID(username);
    if (uuid.isEmpty()) {
      Log.getLogger().error("Failed to get uuid for {}", username);
      return Optional.empty();
    }

    return getPlayerProfile(uuid.get());
  }

  public static Optional<MineToolsProfile> getPlayerProfile(UUID uuid) {
    Request request =
        new Request.Builder().url("https://api.minetools.eu/profile/" + uuid.toString()).build();

    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (!response.isSuccessful()) {
        Log.getLogger().error("Fetching profile for {} gave {}", uuid, response.code());
        return Optional.empty();
      }
      if (response.body() == null) {
        Log.getLogger().error("Profile Response body for {} is empty", uuid);
        return Optional.empty();
      }

      String body = response.body().string();
      ObjectMapper mapper = new ObjectMapper();
      MineToolsProfile apiResponse = mapper.readValue(body, MineToolsProfile.class);
      return Optional.of(apiResponse);
    } catch (IOException e) {
      Log.getLogger().error("Error getting profile for {}", uuid, e);
      return Optional.empty();
    }
  }

  public static Optional<UUID> getPlayerUUID(String username) {
    Request request =
        new Request.Builder().url("https://api.minetools.eu/uuid/" + username).build();

    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (!response.isSuccessful()) {
        Log.getLogger().error("Fetching uuid for {} gave {}", username, response.code());
        return Optional.empty();
      }
      if (response.body() == null) {
        Log.getLogger().error("UUID Response body for {} is empty", username);
        return Optional.empty();
      }

      String body = response.body().string();
      ObjectMapper mapper = new ObjectMapper();
      JsonNode rootNode = mapper.readTree(body);
      String uuid = rootNode.path("id").asText();

      if (uuid.isEmpty()) {
        Log.getLogger().error("UUID {} is empty", username);
        return Optional.empty();
      }

      return Optional.of(UUID.fromString(UUIDUtils.formatUUID(uuid)));
    } catch (IOException e) {
      Log.getLogger().error("Error getting uuid for {}", username, e);
      return Optional.empty();
    }
  }
}
