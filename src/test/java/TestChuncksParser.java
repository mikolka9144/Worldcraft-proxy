public class TestChuncksParser {
//    @Test
//    void tryParseMcrFormat() throws IOException {
//        //given
//        byte[] mcr = Files.readAllBytes(Path.of("region.mcr"));
//        // act
//        ChunksMCR chunks = new ChunksMCR(mcr);
//        chunks..getChunks()[3][1].setBlock(5,5,5, BlockData.BlockType.CROPS_BLOCK_ID);
//        byte[] mcrOut = chunks.build();
//        //debug
//        Files.write(Path.of("./reg.mcr"),mcrOut);
//        // assert
//        ChunksMCR newRegion = new ChunksMCR(mcrOut);
//        ChunkData chunkData = chunks.getChunks()[3][1];
//        ChunkData chunkData1 = newRegion.getChunks()[3][1];
//
//        assertEquals(BlockData.BlockType.CROPS_BLOCK_ID, chunkData.getBlock(5,5,5));
//        assertEquals(BlockData.BlockType.CROPS_BLOCK_ID, chunkData1.getBlock(5,5,5));
//
//    }
}