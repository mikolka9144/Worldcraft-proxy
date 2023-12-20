package com.mikolka9144.worldcraft.simba.backend.Monika;

import com.mikolka9144.worldcraft.utills.Vector3;
import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.utills.enums.BlockType;
import com.mikolka9144.worldcraft.simba.backend.models.SimbaFunction;
import com.mikolka9144.worldcraft.simba.backend.models.StepPacket;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class MonikasConsole {
    @Getter
    private final Monika monika;
    private final MonikasLovePen lovePen;
    private final List<SimbaFunction> querents = new ArrayList<>();
    private SimbaFunction RedirectFunction = null;
    private boolean isPlayerDrawing = true;
    @Autowired
    public MonikasConsole(Monika monika,MonikasLovePen pen){

        this.monika = monika;
        this.lovePen = pen;
    }
    public List<StepPacket> processInput(String input){
        return processInput(new MonikaCommandReader(input));
    }

    public List<StepPacket> processInput(MonikaCommandReader reader){
        List<StepPacket> results = new ArrayList<>();
        try {
            while (reader.hasNext()){
                results.addAll(processCommand(reader));
            }
            return results;
        }
        catch (Exception x){
            return List.of(new StepPacket("Error occured: "+x.getClass().getName()));
        }
    }

    public List<StepPacket> processCommand(MonikaCommandReader reader){
        if (RedirectFunction != null){
            registerSimbaFunction(reader);
            return List.of();
        }
        String command = reader.readNext();

        switch (command.toLowerCase()) {
            case "np" -> {
                int steps = reader.readNextInt();
                return moveMonikaWithPen(steps);
            }
            case "ws" -> {
                int steps = reader.readNextInt();
                return moveMonikaWithPen(-steps);
            }
            case "pw" -> {
                int rightAngle = reader.readNextInt();
                monika.rotateMonika(rightAngle);
                return List.of(new StepPacket(monika.getPositionData()));
            }
            case "lw" -> {
                int leftAngle = reader.readNextInt();
                monika.rotateMonika(-leftAngle);
                return List.of(new StepPacket(monika.getPositionData()));
            }
            case "gr","góra" -> {
                int angle = reader.readNextInt();
                monika.rotateUpMonika(angle);
                return List.of(new StepPacket(monika.getPositionData()));
            }
            case "dl","dół" -> {
                int angle = reader.readNextInt();
                monika.rotateUpMonika(-angle);
                return List.of(new StepPacket(monika.getPositionData()));
            }
            case "pod" -> isPlayerDrawing = false;
            case "opu" -> isPlayerDrawing = true;
            case "powtórz" -> {
                int repeats = reader.readNextInt();
                List<String> code = reader.readNextCodeBlock();
                List<StepPacket> packets = new ArrayList<>();
                for (int i = 0; i < repeats; i++) {
                    packets.addAll(processInput(new MonikaCommandReader(code.iterator())));
                }
                return packets;
            }
            case "wielokąt" -> {
                lovePen.setAuditPoint(monika.getCurrentPosition());
            }
            case "gotowy" -> {
                lovePen.resetAuditPoint();
            }
            case "oto" -> {
                String name = reader.readNext();
                RedirectFunction = new SimbaFunction(name,new ArrayList<>());
            }
            case "pisz" -> {
                String name = reader.readNext();
                return List.of(new StepPacket(name));
            }
            case "kolor","ukp" -> {
                String colorName = reader.readNext().toLowerCase();
                BlockType penColor = switch (colorName){
                    case "cytrynowy" -> BlockType.PAVING_SANDSTONE_ID;
                    case "granatowy" -> BlockType.SNOW_BLOCK_ID;
                    case "szafranowy" -> BlockType.WOOL_YELLOW_ID;
                    case "miętowy" -> BlockType.ICE_ID;
                    case "malachitowy" -> BlockType.LEAVES_JUNGLE_ID;
                    case "limonkowy" -> BlockType.EMERALD_ID;
                    case "zielony" -> BlockType.LEAVES_ID;
                    case "szafirowy" -> BlockType.LAPIS_LAZULI_BLOCK_ID;
                    case "tango" -> BlockType.SPONGE_ID;
                    case "złocisty" -> BlockType.GOLD_BLOCK_ID;
                    case "czerwony" -> BlockType.WOOL_RED_ID;
                    case "błękit_paryski" -> BlockType.DIAMOND_BLOCK_ID;
                    case "siena_palona" -> BlockType.BRONZE_PLATE_ID;
                    case "bursztynowy" -> BlockType.CHISELED_SANDSTONE_ID;
                    case "chabrowy" -> BlockType.MALACHITE_BLOCK_ID;
                    case "szkarlatny" -> BlockType.NETHER_BRICK_ID;
                    case "atramentowy" -> BlockType.OBSIDIAN_ID;
                    case "czerwień_wzrokowa" -> BlockType.PUMPKIN_ID;
                    default -> BlockType.UNKNOWN;
                };
                if(penColor.equals(BlockType.UNKNOWN)){
                    lovePen.setPlayerPen(BlockType.GLASS_ID);
                    return List.of(new StepPacket("Unknown color: " + colorName));
                }
                lovePen.setPlayerPen(penColor);
            }
            default -> {
                Optional<SimbaFunction> seq = querents.stream()
                        .filter(s -> s.getName().equals(command)).findFirst();
                if (seq.isPresent()){
                    return processInput(new MonikaCommandReader(
                            seq.get().getSegments().iterator()));
                }
                return List.of(new StepPacket("Unknown command: " + command));

            }
        }
        return List.of();
    }

    private void registerSimbaFunction(MonikaCommandReader reader) {
        while (reader.hasNext()){
            String segment = reader.readNext();
            if (segment.equals("już")){
                querents.add(RedirectFunction);
                RedirectFunction = null;
                return;
            }
            RedirectFunction.getSegments().add(segment);
        }
    }

    private List<StepPacket> moveMonikaWithPen(int steps) {
        Vector3 initPosition = monika.getCurrentPosition();
        monika.moveMonika(steps);
        Vector3 finalPosition = monika.getCurrentPosition();
        List<Block> blocks = isPlayerDrawing
                ? lovePen.drawBlockLine(initPosition,finalPosition)
                : List.of();
        return List.of(new StepPacket(monika.getPositionData(), blocks));
    }


}
