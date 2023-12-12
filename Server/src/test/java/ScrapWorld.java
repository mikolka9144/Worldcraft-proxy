import com.mikolka9144.level.World;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class ScrapWorld {
    @Test
    public void scrapWorld() throws IOException {
        World wrd = World.fromTarGzBin(Files.readAllBytes(Path.of("./zz.tar.gz")));
        World brandNew = new World(wrd.getLevel(),wrd.getChunks());
        Files.write(Path.of("./testzz.tar.gz"),brandNew.toTarGzBin(), StandardOpenOption.CREATE);
    }
}
