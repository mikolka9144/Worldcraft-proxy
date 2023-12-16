package com.mikolka9144.worldcraft.level;

import com.mikolka9144.worldcraft.utills.Vector3Short;
import com.mikolka9144.worldcraft.utills.Vector3;
import dev.dewy.nbt.tags.collection.CompoundTag;
import dev.dewy.nbt.tags.primitive.*;
import lombok.Getter;

import java.util.List;

@Getter
public class Level {
    public static final String SPAWN_X = "SpawnX";
    public static final String SPAWN_Y = "SpawnX";
    public static final String SPAWN_Z = "SpawnX";
    public static final String PLAYER = "Player";
    public static final String BUILD_VERSION = "BuildVersion";
    public static final String SERVER_VERSION = "ServerVersion";
    public static final String MAP_TYPE = "MapType";
    public static final String TIME = "Time";
    private final CompoundTag nbt;

    public Level(CompoundTag open) {nbt = open;}

    /**
     * Creates new level.dat file to serve as a manifest for a world
     */
    public Level(){
        this.nbt = new CompoundTag("");
        CompoundTag player = new CompoundTag(PLAYER);
        player.putList("Rotation", List.of(new FloatTag(0),new FloatTag(0)));
        player.putList("Pos", List.of(
                new DoubleTag(10),
                new DoubleTag(10),
                new DoubleTag(10)
        ));

        CompoundTag data = new CompoundTag("Data");
        data.putInt(MAP_TYPE,1);
        data.putInt(SPAWN_X,10);
        data.putLong(TIME,10);
        data.putInt(SPAWN_Y,10);
        data.putInt(SPAWN_Z,10);
        data.putString(BUILD_VERSION,"2.5_u");
        data.putString(SERVER_VERSION,"1.0");
        data.putLong("LastPlayed",0); // xdD
        data.put(player);
        getNbt().put(data);
    }
    /**
     * Gets world time
     */
    public LongTag time(){
        return getData().getLong(TIME);
    }
    /**
     * Gets Position of a global spawn in a world
     */
    public Vector3Short getSpawn(){
        return new Vector3Short(
                getInt(SPAWN_X).shortValue(),
                getInt(SPAWN_Y).shortValue(),
                getInt(SPAWN_Z).shortValue()
        );
    }
    /**
     * Sets a position of a global spawn in a world
     */
    public void setSpawn(Vector3Short spawnPos){
        getData().getInt(SPAWN_X).setValue(spawnPos.getX());
        getData().getInt(SPAWN_Y).setValue(spawnPos.getY());
        getData().getInt(SPAWN_Z).setValue(spawnPos.getZ());
    }
    /**
     * Gets position of a player in a world
     */
    public Vector3 getPosition(){
        List<DoubleTag> coords = getData()
                .getCompound(PLAYER).getList("Pos")
                .getValue().stream().map(s -> (DoubleTag)s).toList();
        return new Vector3(
                coords.get(0).floatValue(),
                coords.get(1).floatValue(),
                coords.get(2).floatValue()
        );
    }
    /**
     * Sets a position of a player in a world
     */
    public void setPosition(Vector3 position){
        getData()
                .getCompound(PLAYER).getList("Pos").setValue(List.of(
                new DoubleTag(position.getX()),
                new DoubleTag(position.getY()),
                new DoubleTag(position.getZ())
        ));
    }
    public IntTag mapType(){
        return getInt(MAP_TYPE);
    }
    /**
     * Obtains a game version used to generate a world
     */
    public StringTag buildVersion(){
        return getData().getString(BUILD_VERSION);
    }
    /**
     * Obtains a server version hosting a world (or sth else)
     */
    public StringTag serverVersion(){
        return getData().getString(SERVER_VERSION);
    }
    /**
     * Obtains raw NBT tag.
     * Use this if you want to alter other undocumented properties of a world.
     */
    public CompoundTag getData(){
        return nbt.getCompound("Data");
    }
    private IntTag getInt(String key){
        return getData().getInt(key);
    }
}
