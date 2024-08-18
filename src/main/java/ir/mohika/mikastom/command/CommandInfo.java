package ir.mohika.mikastom.command;

import ir.mohika.mikastom.constants.Rank;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {
  String name();

  String aliases() default "";

  String usage();

  String permission();

  Rank rank() default Rank.DEFAULT;

  boolean allowConsole();
}
