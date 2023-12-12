package com.mikolka9144.packet.packet.codecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ClientBuildManifest {
    private String market;
    private int clientVersion;
}
