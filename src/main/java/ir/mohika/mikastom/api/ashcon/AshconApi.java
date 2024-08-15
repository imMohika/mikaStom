package ir.mohika.mikastom.api.ashcon;

import com.fasterxml.jackson.databind.ObjectMapper;
import ir.mohika.mikastom.http.HttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.util.Optional;

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
