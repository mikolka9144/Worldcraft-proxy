package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private int playerId;
    private String playerName;
    private boolean isPurchaseValidated;
}
