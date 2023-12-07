import com.mikolka9144.worldcraft.common.api.level.World;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ScrapWorld {
    @Test
    public void scrapWorld() throws IOException {
        World wrd = World.fromTarGzBin(Files.readAllBytes(Path.of("./zz.tar.gz")));
    }
}
