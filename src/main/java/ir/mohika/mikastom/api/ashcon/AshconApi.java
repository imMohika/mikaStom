package ir.mohika.mikastom.api.ashcon;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import ir.mohika.mikastom.api.mineskin.responses.UUIDApiResponse;
import ir.mohika.mikastom.http.HttpClient;
import ir.mohika.mikastom.utils.UUIDUtils;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public class AshconApi {
  public static Optional<AshconApiResponse> getPlayer(String username) {
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
      e.printStackTrace();
    }

    return Optional.empty();
  }
}
