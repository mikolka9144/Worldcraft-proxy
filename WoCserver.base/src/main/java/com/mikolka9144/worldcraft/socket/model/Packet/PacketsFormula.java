package com.mikolka9144.worldcraft.socket.model.Packet;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
public class PacketsFormula {
    public static final PacketsFormula FORWARD = null;
    private List<Packet> serverPackets = new ArrayList<>();
    private List<Packet> clientPackets = new ArrayList<>();
}
