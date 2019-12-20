import io.github.zeroone3010.yahueapi.Hue;
import io.github.zeroone3010.yahueapi.Room;
import io.github.zeroone3010.yahueapi.discovery.UpnpDiscoverer;

import java.util.HashSet;

public class app {

  public static void main(String[] args) throws InterruptedException {
    Hue hue = new Hue("192.168.178.40:80", "8zb-2l3INpGC-hG0OzRd0DDbV5fnIsISRVBkj8iZ");
    new HashSet<>(hue.getRooms()).forEach(room -> room.getLights().forEach(light -> light.turnOn() ));
  }


}
