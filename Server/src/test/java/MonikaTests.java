import com.mikolka9144.packet.Vector3;
import com.mikolka9144.worldcraft.modules.simba.backend.Monika.Monika;
import com.mikolka9144.worldcraft.modules.simba.backend.Monika.MonikaCommandReader;
import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
public class MonikaTests {

    @Test
    public void testSimpleScrepping(){
        String command = "np   100";
        //
        List<String> result = MonikaCommandReader.splitInputCommands(command);
        //
        assertThat(result).element(0).isEqualTo("np");
        assertThat(result).element(1).isEqualTo("100");
    }
    @Test
    public void testMultipleCommandScrapping(){
        String command = "np 100 lw 50";
        //
        List<String> result = MonikaCommandReader.splitInputCommands(command);
        //
        assertThat(result).element(0).isEqualTo("np");
        assertThat(result).element(1).isEqualTo("100");
        assertThat(result).element(2).isEqualTo("lw");
        assertThat(result).element(3).isEqualTo("50");
    }

    @Test
    public void testNestedCommands(){
        String command = "powtórz 4 [np 50 gr 90]";
        //
        List<String> result = MonikaCommandReader.splitInputCommands(command);
        //
        assertThat(result).hasSameElementsAs(List.of("powtórz","4","[","np","50","gr","90","]"));
    }
    @Test
    public void testWeirdNestedCommands(){
        String command = "powtórz 4 [  np 50 gr 90 ] ";
        //
        List<String> result = MonikaCommandReader.splitInputCommands(command);
        //
        assertThat(result).hasSameElementsAs(List.of("powtórz","4","[","np","50","gr","90","]"));
    }
    @Test
    public void testCodeBlocksTest(){
        List<String> command = List.of("powtórz","4","[","np","50","gr","90","]");
        MonikaCommandReader reader = new MonikaCommandReader(command.iterator());
        //
        assertThat(reader.readNext()).isEqualTo("powtórz");
        assertThat(reader.readNext()).isEqualTo("4");
        assertThat(reader.readNextCodeBlock()).hasSameElementsAs(List.of("np","50","gr","90"));
    }
    @Test
    public void testInvalidCodeBlocksTest(){
        List<String> command = List.of("powtórz","4","[","np","50","gr","90");
        MonikaCommandReader reader = new MonikaCommandReader(command.iterator());
        //
        assertThat(reader.readNext()).isEqualTo("powtórz");
        assertThat(reader.readNext()).isEqualTo("4");
        assertThatExceptionOfType(NoSuchElementException.class).isThrownBy(reader::readNextCodeBlock);
    }
    @Test
    public void testMissingCodeBlocksTest(){
        List<String> command = List.of("powtórz","4");
        MonikaCommandReader reader = new MonikaCommandReader(command.iterator());
        //
        assertThat(reader.readNext()).isEqualTo("powtórz");
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(reader::readNextCodeBlock);
    }
    @Test
    public void KnownRotationsXTest(){
        Monika monikasConsole = new Monika();
        Condition<Vector3> isUp = new Condition<>(s -> s.getX() == 0,"Up rotation");
        Condition<Vector3> isRight = new Condition<>(s -> s.getX() == 1,"Right rotation");
        Condition<Vector3> isLeft = new Condition<>(s -> s.getX() == -1,"Left rotation");
        //

        assertThat(monikasConsole.calculateAngle(90)).satisfies(isRight);
        assertThat(monikasConsole.calculateAngle(0)).satisfies(isUp);
        assertThat(monikasConsole.calculateAngle(180)).satisfies(isUp);
        assertThat(monikasConsole.calculateAngle(270)).satisfies(isLeft);
    }
}
