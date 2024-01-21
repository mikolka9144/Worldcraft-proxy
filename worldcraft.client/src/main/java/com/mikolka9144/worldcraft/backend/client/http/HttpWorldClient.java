package com.mikolka9144.worldcraft.backend.client.http;

import com.mikolka9144.worldcraft.utills.exception.ParsingException;
import kong.unirest.core.ContentType;
import kong.unirest.core.HttpResponse;
import kong.unirest.core.Unirest;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

/**
 * Basic Http client for downloading and uploading worlds to server
 */
@Slf4j
public class HttpWorldClient {

    private String serverURL = "http://%s:%d/";

    public HttpWorldClient(String target, int port) {
        serverURL = String.format(serverURL, target, port);
    }

    /**
     * Uploads a world to server (as part of room creation process)
     *
     * @param token    Token from server
     * @param worldBin World to upload. Refer to {@code World} class in {@code LevelParser} for more details
     */
    public void uploadWorld(String token, byte[] worldBin) {
        String uploadURL = "worldcraft-web/upload";
        Unirest.post(serverURL + uploadURL)
                .header("accept-encoding", "gzip")
                .field("file", new ByteArrayInputStream(worldBin), ContentType.create("application/x-gzip"), "game.tar.gz")
                .field("uploadToken", token).asEmpty().ifFailure(s -> log.error("World upload request failed"));
    }


    /**
     * Downloads a world binary from server.
     *
     * @param worldId Id of the world you want to download
     * @return Raw binary of world. Refer to {@code World} class in {@code LevelParser} to decode it.
     */
    public byte[] getWorld(int worldId) {
        String downloadURL = "worldcraft-web/rooms/%d/game.tar.gz";
        HttpResponse<byte[]> response = Unirest.get(serverURL + String.format(downloadURL, worldId)).asBytes();
        if (response.isSuccess()) return response.getBody();
        else {
            throw new ParsingException("World download request failed");
        }
    }
}
