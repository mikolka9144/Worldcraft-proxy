package com.mikolka9144.packet.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JoinRoomRequest {
    private String roomName;
    private String password;
    private boolean isReadOlny;
}