import com.mikolka9144.worldcraft.common.PacketDataBuilder;
import com.mikolka9144.worldcraft.common.PacketDataReader;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentDeserializer;
import com.mikolka9144.worldcraft.socket.logic.packetParsers.PacketContentSerializer;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.PurchasesList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
public class PacketParsingTest {
    @Test
    public void testPurchaseParsing(){
        String input = """
                {
                Coins: 50,
                MarketOperationVersion: 0,
                Resurrections: 10,
                PurchaseId: ,
                }""";
        byte[] bytes = new PacketDataBuilder().append(input).build();
        // Act
        PurchasesList purchasesList = PacketContentDeserializer.decodePurchaseLoadResponse(bytes);
        // Assert
        assertEquals("50",purchasesList.getCoins());
        assertEquals("",purchasesList.getPurchaseId());
        assertEquals("0",purchasesList.getMarketOperationVersion());
        assertEquals("10",purchasesList.getResurrections());
    }
    @Test
    public void testPurchaseEncoding(){
        // Prepare
        PurchasesList input = new PurchasesList("kupno","100","24","50");
        // Act
        byte[] testOutput = PacketContentSerializer.encodePurchaseLoadResponse(input);
        PacketDataReader reader = new PacketDataReader(testOutput);
        String testStr = reader.getString();
        log.info(testStr);
        // Assert
        PurchasesList output = PacketContentDeserializer.decodePurchaseLoadResponse(testOutput);
        assertEquals(input,output);
//        assertEquals(
//                """
//                {
//                Coins: 50,
//                MarketOperationVersion: 0,
//                Resurrections: 10,
//                PurchaseId: kupno,
//                }
//                """
//                ,testStr
//        );
    }

}
