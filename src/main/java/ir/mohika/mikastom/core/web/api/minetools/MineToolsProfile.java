package ir.mohika.mikastom.core.web.api.minetools;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class MineToolsProfile {
  @JsonProperty("raw")
  private Raw raw;

  @Data
  @NoArgsConstructor
  @JsonIgnoreProperties(ignoreUnknown = true)
  public static class Raw {
    @JsonProperty("properties")
    private Property[] properties;

    @Data
    @NoArgsConstructor
    public static class Property {
      private String name;
      private String value;
      private String signature;
    }
  }
}
