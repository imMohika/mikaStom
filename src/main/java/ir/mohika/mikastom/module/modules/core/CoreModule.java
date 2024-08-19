package ir.mohika.mikastom.module.modules.core;

import dev.emortal.api.modules.annotation.ModuleData;
import dev.emortal.api.modules.env.ModuleEnvironment;
import ir.mohika.mikastom.module.MikaStomModule;
import ir.mohika.mikastom.module.modules.core.listeners.PlayerCommandListener;
import ir.mohika.mikastom.module.modules.core.listeners.PlayerConfigurationListener;
import org.jetbrains.annotations.NotNull;

@ModuleData(name = "core")
public class CoreModule extends MikaStomModule {
  private PlayerConfigurationListener configurationListener;
  private PlayerCommandListener commandListener;

  public CoreModule(@NotNull ModuleEnvironment environment) {
    super(environment);
  }

  @Override
  public boolean onLoad() {
    configurationListener = new PlayerConfigurationListener();
    eventNode.addListener(configurationListener);

    commandListener = new PlayerCommandListener();
    eventNode.addListener(commandListener);

    return true;
  }

  @Override
  public void onUnload() {
    eventNode.removeListener(configurationListener);
    eventNode.removeListener(commandListener);
  }
}
