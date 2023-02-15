import com.mikolka9144.worldcraft.http.logic.level.Chunks.ChunksMCR;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class TestChuncksParser {
    @Test
    void tryParseMcrFormat() throws IOException {
        //given
        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
        // act
        ChunksMCR chunks = new ChunksMCR(mcr);
        // assert
        chunks.getChunks()[0][1].execute(System.out::println);
        Files.write(Path.of("./reg.mcr"),chunks.build());
    }
}
