package ir.mohika.mikastom.api.ashcon;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AshconApiResponse {
  private String uuid;
  private String username;
  private Textures textures;

  @Data
  @NoArgsConstructor
  public static class Textures {
    private boolean slim;
    private boolean custom;
    private Raw raw;
  }

  @Data
  @NoArgsConstructor
  public static class Raw {
    private String value;
    private String signature;
  }
}
