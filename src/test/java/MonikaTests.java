import com.mikolka9144.worldcraft.programs.simba.Monika;
import com.mikolka9144.worldcraft.programs.simba.MonikaCommandReader;
import com.mikolka9144.worldcraft.socket.logic.APIcomponents.PacketBuilder;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.PacketProtocol;
import com.mikolka9144.worldcraft.socket.model.Vector3;
import com.mikolka9144.worldcraft.socket.model.Vector3Short;
import org.assertj.core.api.Condition;
import org.junit.Test;

import java.util.List;
import java.util.NoSuchElementException;

import static org.assertj.core.api.Assertions.*;

public class MonikaTests {
    @Test
    public void testSimpleScrepping(){
        String command = "np   100";
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        //
        List<String> result = monika.splitInputCommands(command);
        //
        assertThat(result).element(0).isEqualTo("np");
        assertThat(result).element(1).isEqualTo("100");
    }
    @Test
    public void testMultipleCommandScrapping(){
        String command = "np 100 lw 50";
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        //
        List<String> result = monika.splitInputCommands(command);
        //
        assertThat(result).element(0).isEqualTo("np");
        assertThat(result).element(1).isEqualTo("100");
        assertThat(result).element(2).isEqualTo("lw");
        assertThat(result).element(3).isEqualTo("50");
    }

    @Test
    public void testNestedCommands(){
        String command = "powtórz 4 [np 50 gr 90]";
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        //
        List<String> result = monika.splitInputCommands(command);
        //
        assertThat(result).hasSameElementsAs(List.of("powtórz","4","[","np","50","gr","90","]"));
    }
    @Test
    public void testWeirdNestedCommands(){
        String command = "powtórz 4 [  np 50 gr 90 ] ";
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        //
        List<String> result = monika.splitInputCommands(command);
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
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        Condition<Vector3> isUp = new Condition<>(s -> s.getX() == 0,"Up rotation");
        Condition<Vector3> isRight = new Condition<>(s -> s.getX() == 1,"Right rotation");
        Condition<Vector3> isLeft = new Condition<>(s -> s.getX() == -1,"Left rotation");
        //

        assertThat(monika.calculateAngle(90)).satisfies(isRight);
        assertThat(monika.calculateAngle(0)).satisfies(isUp);
        assertThat(monika.calculateAngle(180)).satisfies(isUp);
        assertThat(monika.calculateAngle(270)).satisfies(isLeft);
    }
    @Test
    public void DrawLine(){
        Monika monika = new Monika(new Vector3Short((short) 0, (short) 0, (short) 0),new PacketBuilder(PacketProtocol.SERVER));
        List<BlockData> blocks = monika.drawBlockLine(new Vector3(0,0,0),new Vector3(5.5f,0,2));
        blocks.forEach(s -> System.out.println(s.getX()+" "+s.getY()+" "+s.getZ()));
    }
}
