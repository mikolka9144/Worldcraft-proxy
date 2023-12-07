import com.mikolka9144.worldcraft.common.api.packet.build.PacketDataReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;
import java.util.Arrays;

class TestLegacyRoomsInterpreter {
    @Test
    void testWorldcraftConversionForRooms()  {
        PacketDataReader reader = getDataReader();
        var packetIndex = reader.getInt();
                var allPackets = reader.getInt();
                var initRoom = reader.getShort();
                var roomType = reader.getByte();
        while (reader.hasNext(20)){
                    var id = reader.getInt();
                    var name = reader.getString();
                    var protect = reader.getBoolean();
                    var players = reader.getShort();
                    var roomCap = reader.getShort();
                    Assertions.assertEquals(80,roomCap);
                    var entr = reader.getInt();
                    var likes = reader.getInt();
                    System.out.println(entr);
                    System.out.println(Arrays.toString(ByteBuffer.allocate(4).putInt(likes).array()));
                    //var readOlny = reader.getBoolean();
        }
    }

    private static PacketDataReader getDataReader() {
        byte[] payload = new byte[]{0, 0, 0, 0, 0, 0, 0, 4, 0, 40, 1, 1, 97, 84, -113, 0, 5, 72, 111, 105, 116, 111, 0, 0, 2, 0, 80, 0, 0, 0, 84, 0, 0, 0, 11, 1, 94, 10, 108, 0, 7, 84, 104, 101, 111, 49, 50, 51, 0, 0, 6, 0, 80, 0, 0, 0, 84, 0, 0, 0, 19, 1, 97, 87, 3, 0, 6, 50, 48, 49, 48, 48, 53, 0, 0, 2, 0, 80, 0, 0, 0, 4, 0, 0, 0, 1, 1, 97, 86, -45, 0, 10, -47, -124, -47, -128, -48, -66, -48, -67, -47, -113, 0, 0, 2, 0, 80, 0, 0, 0, 15, 0, 0, 0, 0, 1, 97, 86, -42, 0, 13, 76, 105, 111, 112, 111, 112, 97, 110, 105, 109, 97, 108, 115, 0, 0, 16, 0, 80, 0, 0, 0, -113, 0, 0, 0, 22, 1, 97, 86, -122, 0, 5, 57, 57, 53, 52, 51, 0, 0, 1, 0, 80, 0, 0, 1, -111, 0, 0, 0, 42, 1, 97, 86, -11, 0, 8, 114, 101, 100, 112, 111, 119, 101, 114, 0, 0, 8, 0, 80, 0, 0, 0, 11, 0, 0, 0, 0, 1, 97, 86, 24, 0, 7, 49, 50, 51, 53, 55, 57, 56, 0, 0, 3, 0, 80, 0, 0, 0, -23, 0, 0, 0, 19, 1, 97, 87, 10, 0, 4, 49, 53, 48, 56, 0, 0, 1, 0, 80, 0, 0, 0, 1, 0, 0, 0, 0, 1, 97, 79, -50, 0, 8, 51, 52, 53, 54, 51, 52, 53, 54, 0, 0, 10, 0, 80, 0, 0, 2, 89, 0, 0, 0, 73, 1, 97, 87, 13, 0, 4, 99, 99, 101, 114, 0, 0, 1, 0, 80, 0, 0, 0, 1, 0, 0, 0, 0};

        PacketDataReader reader = new PacketDataReader(payload);
        return reader;
    }
}
