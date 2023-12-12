import com.mikolka9144.level.Level;
import com.mikolka9144.level.Terrain;
import com.mikolka9144.level.nbt.ChunksMCR;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class WorldTests {
    @Test
    public void testCountingWorldLEnght(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,10);

        assertThat(trn.getMaxX()).isEqualTo(10* ChunksMCR.CHUNK_SIZE);
        assertThat(trn.getMaxZ()).isEqualTo(10*ChunksMCR.CHUNK_SIZE);
    }
    @Test
    public void testCountingNotStandardWorldLnght(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,20);


        assertThat(trn.getMaxX()).isEqualTo(10*ChunksMCR.CHUNK_SIZE);
        assertThat(trn.getMaxZ()).isEqualTo(20*ChunksMCR.CHUNK_SIZE);
    }
}
