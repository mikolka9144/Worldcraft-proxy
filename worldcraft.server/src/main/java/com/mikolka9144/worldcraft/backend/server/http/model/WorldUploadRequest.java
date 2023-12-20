package com.mikolka9144.worldcraft.backend.server.http.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class WorldUploadRequest {
    private String token;
    private byte[] world;
    private List<String> flags;
}
