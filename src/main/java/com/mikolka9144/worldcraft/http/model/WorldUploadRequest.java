package com.mikolka9144.worldcraft.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class WorldUploadRequest {
    private String token;
    private byte[] world;
}
