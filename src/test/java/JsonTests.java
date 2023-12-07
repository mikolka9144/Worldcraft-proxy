import com.mikolka9144.worldcraft.common.api.packet.codecs.PurchasesList;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class JsonTests {
    @Test
    public void testJson(){
        String test = """
                {
                PurchaseId: 123,
                Coins: 123,
                MarketOperationVersion: 123,
                Resurrections: 123
                }""";
        PurchasesList list = new PurchasesList("123","123","123","123");
        assertThat(list.encodeToJson()).isEqualTo(test);
    }
}
