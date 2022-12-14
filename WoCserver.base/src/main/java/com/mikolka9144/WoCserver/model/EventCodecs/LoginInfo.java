package com.mikolka9144.WoCserver.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginInfo {
    private String username;
    private short skinId;
    private String clientVer;
    private String deviceId;
    private String deviceName;
    private String androidVer;
    private String androidAPI;
    private String marketName;
}
