package ir.mohika.mikastom.http;

import lombok.Getter;
import okhttp3.OkHttpClient;

public class HttpClient {
  @Getter private static final OkHttpClient client = new OkHttpClient();
}
