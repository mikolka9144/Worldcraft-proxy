import com.mikolka9144.worldcraft.http.logic.level.Chunks.ChunksMCR;
import com.mikolka9144.worldcraft.http.logic.level.LevelNBT;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestChuncksParser {
    @Test
    void tryParseNbtFormat() throws IOException {
        //given
        byte[] level = Files.readAllBytes(Path.of("./test/resources/level.dat"));
        // act
        LevelNBT nbt = new LevelNBT(level);
        // assert
        return;
    }
}
