package com.mikolka9144.packet.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoomCreateReq {
    private String roomName;
    private String roomPassword;
    private boolean isReadOnly;
}
