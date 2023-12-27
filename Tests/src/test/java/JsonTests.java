import com.mikolka9144.worldcraft.backend.packets.codecs.PurchasesList;
import org.assertj.core.api.Assertions;
import org.junit.Test;

public class JsonTests {
    @Test
    public void testJson() {
        String test = """
                {
                PurchaseId: 123,
                Coins: 123,
                MarketOperationVersion: 123,
                Resurrections: 123
                }""";
        PurchasesList list = new PurchasesList("123", "123", "123", "123");
        Assertions.assertThat(list.encodeToJson()).isEqualTo(test);
    }
}
