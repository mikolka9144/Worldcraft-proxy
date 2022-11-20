import com.mikolka9144.Models.EventCodecs.RoomsPacket;
import com.mikolka9144.Utills.PacketParsers.PacketDataReader;
import org.junit.Test;

import java.io.IOException;

public class Tests {
    @Test
    public void testPacket() throws IOException {
        byte[] data = new byte[]{0, 0, 0, 0, 0, 0, 0, 5, 0, 50, 1, 1, 97, 20, 31, 0, 5, 72, 97, 114, 108, 111, 0, 0, 4, 0, 80, 0, 0, 1, 92, 0, 0, 0, 48, 1, 88, -56, -103, 0, 12, 106, +111, 114, 100, 97, 110, 115, 119, 111, 114, 108, 100, 0, 0, 9, 0, 80, 0, 0, 1, 85, 0, 0, 0, 54, 1, 97, 2, -44, 0, 4, 48, 49, 56, 52, 0, 0, 2, 0, 80, 0, 0, 6, -118, 0, 0, 1, 1, 1, 97, 25, -124, 0, 11, 107, 106, 107, 108, 122, 120, 99, 118, 98, 110, 109, 0, 0, 2, 0, 80, 0, 0, 0, 6, 0, 0, 0,2, 1, 91, -127, 11, 0, 9, 114, 111, 106, 111, 118, 101, 114, 100, 101, 1, 0, 2, 0, 80, 0, 0, 6, 86, 0, 0, 3, 107, 1, 90, 121, 34, 0, 5, 78, 65, 68, 65, 49, 0, 0, 6, 0, 80, 0, 0, 0, 47, 0, 0, 0, 12, 1, 97, 26, 122, 0, 6, 89, 97, 115, 97, 97, 110, 0, 0, 4, 0, 80, 0, 0, 0, 24, 0, 0, 0, 4, 1, 97, 26, -102, 0, 7, 55, 55, 55, 49, 55, 55, 55, 0, 0, 2, 0, 80, 0, 0, 0, 5, 0, 0, 0, 0, 1, 93, 67, 117, 0, 10, 117, 110, 105, 99, 111, 114, 110, 105, 111, 53, 0, 0, 25, 0, 80, 0, 0, 0, -30, 0, 0, 0, 49, 1, 97, 26, -103, 0, 11, 69, 76, 71, 65, 84, 79, 77, 73, 67, 72, 73, 1, 0, 2, 0, 80, 0, 0, 0, 2, 0, 0, 0, 0, 1, 97, 26, -114, 0, 5, 121, 111, 98, 111, 121, 0, 0, 7, 0, 80, 0, 0, 0, 20, 0, 0, 0, 2};
        var result = convert(data);
    }
    private RoomsPacket convert(byte[] data) throws IOException {
        PacketDataReader reader = new PacketDataReader(data);
        RoomsPacket out = new RoomsPacket(
                reader.getInt(),
                reader.getInt(),
                reader.getShort(),
                reader.getByte());
        while (reader.hasNext(20)){
            RoomsPacket.Room room = new RoomsPacket.Room(
                    reader.getInt(),
                    reader.getString(),
                    reader.getBoolean(),
                    reader.getShort(),
                    reader.getShort(),
                    reader.getInt(),
                    reader.getInt(),
                    false);
            out.getRooms().add(room);
        }
        return out;
    }
}
