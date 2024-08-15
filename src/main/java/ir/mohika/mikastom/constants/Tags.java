package ir.mohika.mikastom.constants;

import lombok.Getter;
import net.minestom.server.coordinate.Pos;
import net.minestom.server.tag.Tag;

public class Tags {
  @Getter private static final Tag<Pos> spawnPos = Tag.Transient("spawnPos");
  @Getter private static final Tag<String> minigame = Tag.String("minigame");
}
