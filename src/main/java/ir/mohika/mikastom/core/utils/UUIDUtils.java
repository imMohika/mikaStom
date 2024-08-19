package ir.mohika.mikastom.core.utils;

import java.util.regex.Pattern;
import org.jetbrains.annotations.NotNull;

public class UUIDUtils {
  private static final Pattern uuidPattern =
      Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

  public static String formatUUID(@NotNull String str) {
    return uuidPattern.matcher(str).replaceFirst("$1-$2-$3-$4-$5");
  }
}
