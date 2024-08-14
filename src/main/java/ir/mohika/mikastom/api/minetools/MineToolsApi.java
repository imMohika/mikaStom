package ir.mohika.mikastom.api.minetools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mohika.mikastom.http.HttpClient;
import ir.mohika.mikastom.utils.UUIDUtils;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class MineToolsApi {
  public static Optional<MineToolsProfile> getPlayerProfile(String username) {
    Optional<UUID> uuid = getPlayerUUID(username);
    if (uuid.isPresent()) {
      return getPlayerProfile(uuid.get());
    }
    return Optional.empty();
  }

  public static Optional<MineToolsProfile> getPlayerProfile(UUID uuid) {
    Request request =
        new Request.Builder().url("https://api.minetools.eu/profile/" + uuid.toString()).build();

    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (response.isSuccessful()) {
        if (response.body() == null) {
          return Optional.empty();
        }
        String body = response.body().string();
        ObjectMapper mapper = new ObjectMapper();
        MineToolsProfile apiResponse = mapper.readValue(body, MineToolsProfile.class);

        return Optional.of(apiResponse);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }

  public static Optional<UUID> getPlayerUUID(String username) {
    Request request =
        new Request.Builder().url("https://api.minetools.eu/uuid/" + username).build();

    try (Response response = HttpClient.getClient().newCall(request).execute()) {
      if (response.isSuccessful()) {
        if (response.body() == null) {
          return Optional.empty();
        }
        String body = response.body().string();

        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(body);

        String uuid = rootNode.path("id").asText();

        if (uuid.isEmpty()) {
          return Optional.empty();
        }

        return Optional.of(UUID.fromString(UUIDUtils.formatUUID(uuid)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    return Optional.empty();
  }
}
