import com.mikolka9144.worldcraft.http.logic.level.Chunks.ChunksMCR;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestChuncksParser {
    @Test
    void tryParseMcrFormat() throws IOException {
        //given
        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
        // act
        ChunksMCR chunks = new ChunksMCR(mcr);
        chunks.getChunks()[3][1].execute(s -> s.getNbt().putString("Name","Level"));
        byte[] mcrOut = chunks.build();
        //debug
        Files.write(Path.of("./reg.mcr"),mcrOut);
        // assert
        ChunksMCR newRegion = new ChunksMCR(mcrOut);
        newRegion.getChunks()[3][1].execute(s -> {
            assertEquals("Level", s.getNbt().getString("Name").getValue());
        });
    }
}
