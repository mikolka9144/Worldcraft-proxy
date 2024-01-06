package com.mikolka9144.worldcraft.backend.level;

import com.mikolka9144.worldcraft.backend.level.nbt.ChunksMCR;
import com.mikolka9144.worldcraft.utills.Vector3Short;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.stream.IntStream;

@Getter

public class Terrain {
    public static final int MAX_Y = 128;
    @Getter
    private final ChunkData[][] chunks = new ChunkData[32][32];
    private byte[] timestamps = new byte[ChunksMCR.SECTOR_SIZE];

    public Terrain(byte[] timestamps) {
        if (timestamps.length != ChunksMCR.SECTOR_SIZE)
            throw new IllegalArgumentException("Timestamps must be EXACTLY 4096 bytes in size");
        this.timestamps = timestamps;
    }

    public Terrain(int xChunks, int zChunks) {
        if (xChunks > 32 || zChunks > 32)
            throw new IllegalArgumentException("You can have only 32 chunks in one axis.");
        Terrain.enumerate2D(xChunks, 0, zChunks, s -> createChunk(s.getX(), s.getZ()));
    }

    /**
     * Retrieves block for manipulation
     *
     * @param x X position of requested block.
     * @param y Y position of requested block.
     * @param z Z position of requested block.
     * @return Object representing said block.
     */
    public ChunkData.StepBlock at(int x, int y, int z) {
        int xPos = x & 15;   // mod by CHUNK_SIZE
        int zPos = z & 15;   // mod by CHUNK_SIZE
        int chunkX = x >> 4; // dividing by CHUNK_SIZE
        int chunkZ = z >> 4; // dividing by CHUNK_SIZE
        return atChunk(chunkX, chunkZ).at(xPos, y, zPos);
    }

    /**
     * Retrieves chunk in given position
     *
     * @param x X position of requested chunk.
     * @param z Z position of requested chunk.
     * @return Object representing that chunk.
     */
    public ChunkData atChunk(int x, int z) {
        return chunks[x][z];
    }

    /**
     * Retrieves block for manipulation
     *
     * @param vector Position of requested block.
     * @return Object representing said block.
     */
    public ChunkData.StepBlock at(Vector3Short vector) {
        return at(vector.getX(), vector.getY(), vector.getZ());
    }

    /**
     * Manually creates chunk.
     * Use this function at your own risk.
     * It breaks enumeration functions
     * and world size calculations.
     */
    public void createChunk(int x, int z) {
        chunks[x][z] = new ChunkData(x, z);
    }

    /**
     * Get maximum X value of avaliable chunks.
     */
    public int getMaxChunkX() {
        return (int) (Arrays.stream(chunks).takeWhile(s -> s[0] != null).count());
    }

    /**
     * Get maximum X value, that block can take.
     * It's based on created chunks.
     */
    public int getMaxX() {
        return getMaxChunkX() * ChunksMCR.CHUNK_SIZE;
    }

    /**
     * Get maximum Z value, that block can take.
     * It's based on created chunks.
     */
    public int getMaxZ() {
        return getMaxChunkZ() * ChunksMCR.CHUNK_SIZE;
    }

    /**
     * Get maximum Z value of avaliable chunks.
     */
    private int getMaxChunkZ() {
        return (int) Arrays.stream(chunks[0]).takeWhile(Objects::nonNull).count();
    }

    /**
     * Iterates through every block at the given Y.
     *
     * @param constantY Y position of blocks to iterate
     * @param step      action to run
     */
    public void enumerateWorld2D(int constantY, Consumer<Vector3Short> step) {
        enumerate2D(getMaxX(), constantY, getMaxZ(), step);
    }

    /**
     * Iterates through every block in world.
     *
     * @param step action to run
     */
    public void enumerateWorld3D(Consumer<Vector3Short> step) {
        enumerate3D(getMaxX(), MAX_Y, getMaxZ(), step);
    }

    /**
     * Iterates through every chunk in the world.
     * {@code DO NOT USE  Y as it's always 0.}
     *
     * @param step action to run
     */
    public void enumerateChunks(Consumer<Vector3Short> step) {
        enumerate2D(getMaxChunkX(), 0, getMaxChunkZ(), step);
    }

    public static void enumerate2D(int maxX, int constantY, int maxZ, Consumer<Vector3Short> step) {
        IntStream.range(0, maxX).forEach(x ->
                IntStream.range(0, maxZ).forEach(z -> step.accept(new Vector3Short(x, constantY, z))
                ));
    }

    public static void enumerate3D(int maxX, int maxY, int maxZ, Consumer<Vector3Short> step) {
        IntStream.range(0, maxY).forEach(y -> enumerate2D(maxX, y, maxZ, step));
    }
}
