package com.mikolka9144.worldcraft.simba.backend.models;

import com.mikolka9144.worldcraft.backend.packets.codecs.Block;
import com.mikolka9144.worldcraft.backend.packets.codecs.MovementPacket;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data

public class StepPacket {
    private MovementPacket newPosition = null;
    private List<Block> newBlocks = new ArrayList<>();
    private String textOutput = null;

    public StepPacket(String textOutput) {
        this.textOutput = textOutput;
    }

    public StepPacket(MovementPacket newPosition) {
        this.newPosition = newPosition;
    }

    public StepPacket(MovementPacket newPosition, List<Block> newBlocks) {
        this.newPosition = newPosition;
        this.newBlocks = newBlocks;
    }
}
