package ir.mohika.mikastom.api.mineskin.responses;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UUIDApiResponse {
  private boolean valid;
  private String uuid;
  private String name;
}
