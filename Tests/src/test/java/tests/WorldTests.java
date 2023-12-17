package tests;

import com.mikolka9144.worldcraft.level.Level;
import com.mikolka9144.worldcraft.level.Terrain;
import org.junit.Test;

import static com.mikolka9144.worldcraft.utills.LevelConsts.LEVEL_CHUNK_SIZE;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class WorldTests {
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
}
