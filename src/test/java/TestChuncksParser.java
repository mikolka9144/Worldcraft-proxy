import com.mikolka9144.worldcraft.common.level.Chunks.ChunkData;
import com.mikolka9144.worldcraft.common.level.Chunks.ChunksMCR;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static junit.framework.Assert.assertTrue;
import static junit.framework.TestCase.assertEquals;

public class TestChuncksParser {
    @Test
    public void Mcr_testChunkIntegrityUsingChunks_BlockTest() throws IOException {
        //given
        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
        // act
        ChunksMCR chunks = new ChunksMCR(mcr);
        chunks.getChunks()[3][1].setBlock(5,5,5, BlockData.BlockType.CROPS_BLOCK_ID);
        byte[] mcrOut = chunks.build();
        ChunkData chunkData = chunks.getChunks()[3][1];

        ChunksMCR newRegion = new ChunksMCR(mcrOut);
        ChunkData chunkData1 = newRegion.getChunks()[3][1];

        assertEquals(BlockData.BlockType.CROPS_BLOCK_ID, chunkData.getBlock(5,5,5));
        assertEquals(BlockData.BlockType.CROPS_BLOCK_ID, chunkData1.getBlock(5,5,5));

    }
    @Test
    public void Mcr_testChunkIntegrityUsingXYZ_DataTest() throws IOException {
        //given
        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
        // act
        ChunksMCR chunks = new ChunksMCR(mcr);
        chunks.setData(5,5,5, (byte) 15);
        byte[] mcrOut = chunks.build();

        ChunksMCR newRegion = new ChunksMCR(mcrOut);

        assertEquals(15, chunks.getData(5,5,5));
        assertEquals(15, newRegion.getData(5,5,5));

    }
    @Test
    public void Mcr_DataTest_IsValueWritten() throws IOException {
        //given
        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
        // act
        ChunksMCR chunks = new ChunksMCR(mcr);
        chunks.setData(5,5,5, (byte) 15);
        boolean hasValue = false;
        var table = chunks.getChunks()[0][0].getRawNBT().getNbt().getCompound("Level").getByteArray("Data");
        for (int i = 0; i < table.size(); i++) {
            if(table.get(i) == (byte)15){
                hasValue = true;
                System.out.println(i);
            }
        }
        assertTrue("Written value is not present in required chunk's data",hasValue);
    }
}
