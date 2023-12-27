import com.mikolka9144.worldcraft.backend.packets.codecs.PurchasesList;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataDecoder;
import com.mikolka9144.worldcraft.backend.packets.encodings.PacketDataEncoder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataBuilder;
import com.mikolka9144.worldcraft.utills.builders.PacketDataReader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@Slf4j
class PacketParsingTest {
    @Test
    void testPurchaseParsing() {
        String input = """
                {
                Coins: 50,
                MarketOperationVersion: 0,
                Resurrections: 10,
                PurchaseId: ,
                }""";
        byte[] bytes = new PacketDataBuilder().append(input).build();
        // Act
        PurchasesList purchasesList = PacketDataDecoder.decodePurchaseData(bytes);
        // Assert
        Assertions.assertEquals("50", purchasesList.getCoins());
        Assertions.assertEquals("", purchasesList.getPurchaseId());
        Assertions.assertEquals("0", purchasesList.getMarketOperationVersion());
        Assertions.assertEquals("10", purchasesList.getResurrections());
    }

    @Test
    void testPurchaseEncoding() {
        // Prepare
        PurchasesList input = new PurchasesList("kupno", "100", "24", "50");
        // Act
        byte[] testOutput = PacketDataEncoder.purchaseLoadResponse(input);
        PacketDataReader reader = new PacketDataReader(testOutput);
        String testStr = reader.getString();
        log.info(testStr);
        // Assert
        PurchasesList output = PacketDataDecoder.decodePurchaseData(testOutput);
        Assertions.assertEquals(input, output);
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
