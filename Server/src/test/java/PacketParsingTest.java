import com.mikolka9144.packet.packet.build.PacketDataBuilder;
import com.mikolka9144.packet.packet.build.PacketDataReader;
import com.mikolka9144.packet.packet.encodings.PacketDataDecoder;
import com.mikolka9144.packet.packet.encodings.PacketDataEncoder;
import com.mikolka9144.packet.packet.codecs.PurchasesList;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
@Slf4j
class PacketParsingTest {
    @Test
    void testPurchaseParsing(){
        String input = """
                {
                Coins: 50,
                MarketOperationVersion: 0,
                Resurrections: 10,
                PurchaseId: ,
                }""";
        byte[] bytes = new PacketDataBuilder().append(input).build();
        // Act
        PurchasesList purchasesList = PacketDataDecoder.decodePurchaseLoadResponse(bytes);
        // Assert
        assertEquals("50",purchasesList.getCoins());
        assertEquals("",purchasesList.getPurchaseId());
        assertEquals("0",purchasesList.getMarketOperationVersion());
        assertEquals("10",purchasesList.getResurrections());
    }
    @Test
    void testPurchaseEncoding(){
        // Prepare
        PurchasesList input = new PurchasesList("kupno","100","24","50");
        // Act
        byte[] testOutput = PacketDataEncoder.purchaseLoadResponse(input);
        PacketDataReader reader = new PacketDataReader(testOutput);
        String testStr = reader.getString();
        log.info(testStr);
        // Assert
        PurchasesList output = PacketDataDecoder.decodePurchaseLoadResponse(testOutput);
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
