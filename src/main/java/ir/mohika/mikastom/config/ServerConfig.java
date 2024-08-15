package ir.mohika.mikastom.config;

import de.exlll.configlib.Comment;
import de.exlll.configlib.Configuration;
import de.exlll.configlib.Ignore;
import de.exlll.configlib.PostProcess;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

@Configuration
@Data
@NoArgsConstructor
public class ServerConfig {
  private String host = "0.0.0.0";
  private int port = 25565;

  @Comment({
    "Username of players that should have 'op' permission",
    "Note: All username should be in lower case"
  })
  private List<String> operators = new ArrayList<>();

  @Ignore private SocketAddress address;

  @PostProcess
  private void postProcess() {
    System.out.println("Server config post process");
    this.address = new InetSocketAddress(host, port);
  }
}
