package ir.mohika.mikastom.utils;

import java.util.regex.Pattern;

public class UUIDUtils {
  private static Pattern uuidPattern = Pattern.compile("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})");

  public static String formatUUID(String str) {
    return uuidPattern.matcher(str).replaceFirst("$1-$2-$3-$4-$5");
  }
}
