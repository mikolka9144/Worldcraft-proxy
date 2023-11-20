package com.mikolka9144.worldcraft.programs.simba;

import com.mikolka9144.worldcraft.socket.model.EventCodecs.BlockData;
import com.mikolka9144.worldcraft.socket.model.EventCodecs.MovementPacket;
import lombok.Data;

import java.util.List;

@Data

public class StepPacket {
    private MovementPacket newPosition = null;
    private List<BlockData> newBlocks = null;
    private String textOutput = null;

    public StepPacket(String textOutput) {
        this.textOutput = textOutput;
    }

    public StepPacket(MovementPacket newPosition) {
        this.newPosition = newPosition;
    }

    public StepPacket(MovementPacket newPosition, List<BlockData> newBlocks) {
        this.newPosition = newPosition;
        this.newBlocks = newBlocks;
    }
}
