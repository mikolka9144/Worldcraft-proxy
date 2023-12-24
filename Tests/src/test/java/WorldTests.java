import com.mikolka9144.worldcraft.backend.level.Level;
import com.mikolka9144.worldcraft.backend.level.Terrain;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.Assert.assertEquals;


public class WorldTests {
    private static final int LEVEL_CHUNK_SIZE = 16;

    @Test
    public void testCountingWorldLEnght(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,10);

        assertEquals(trn.getMaxX(),10* LEVEL_CHUNK_SIZE);
        assertEquals(trn.getMaxZ(),10*LEVEL_CHUNK_SIZE);
    }
    @Test
    public void testCountingNotStandardWorldLnght(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,20);


        assertEquals(trn.getMaxX(),10*LEVEL_CHUNK_SIZE);
        assertEquals(trn.getMaxZ(),20*LEVEL_CHUNK_SIZE);
    }
    @Test
    public void testLightPlacing(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,20);

        trn.at(5,5,5).setSkyLight((byte) 13);
        trn.at(15,5,5).setSkyLight((byte) 6);
        trn.at(0,0,0).setSkyLight((byte) 2);
        trn.at(0,1,0).setSkyLight((byte) 3);
        assertEquals(13,trn.at(5,5,5).getSkyLight());
        assertEquals(6,trn.at(15,5,5).getSkyLight());
        assertEquals(2,trn.at(0,0,0).getSkyLight());
        assertEquals(3,trn.at(0,1,0).getSkyLight());
    }
    @Test
    public void testLightException(){
        Level lvl = new Level();
        Terrain trn = new Terrain(10,20);
        assertThatCode(() -> trn.at(5,5,5).setSkyLight((byte) 17)).hasNoCause();
    }
}
