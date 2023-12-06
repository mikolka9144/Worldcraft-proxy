import com.mikolka9144.worldcraft.common.api.packet.codecs.PurchasesList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTests {
    @Test
    public void testJson(){
        String test = "{\n" +
                "PurchaseId: 123,\n" +
                "Coins: 123,\n" +
                "MarketOperationVersion: 123,\n" +
                "Resurrections: 123\n" +
                "}";
        PurchasesList list = new PurchasesList("123","123","123","123");
        assertThat(list.encodeToJson()).isEqualTo(test);
    }
}
