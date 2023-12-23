package com.mikolka9144.worldcraft.backend.packets.codecs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlayerReport {
    private int playerId;
    private String abuseDescription;
}
