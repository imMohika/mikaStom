package ir.mohika.mikastom.core.web;

import lombok.Getter;
import okhttp3.OkHttpClient;

public class HttpClient {
  @Getter private static final OkHttpClient client = new OkHttpClient();
}
