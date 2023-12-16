package com.mikolka9144.worldcraft.backend.base.http;

import io.netty.handler.codec.http.HttpHeaderNames;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;

import static com.mikolka9144.worldcraft.backend.base.http.Xd.getClient;

@Slf4j
public class HttpClient {
    private String serverURL = "http://%s:%d/";

    public HttpClient(String target, int port){
        serverURL = String.format(serverURL,target,port);
    }
    public void uploadWorld(String token,byte[] worldBin) {

        String uploadURL = "worldcraft-web/upload";
        getClient().headers(e -> e.add(HttpHeaderNames.ACCEPT_ENCODING,"gzip"))

        .post().uri(serverURL + uploadURL)
        .sendForm((a, b ) ->{
            b.file("file","game.tar.gz", new ByteArrayInputStream(worldBin),"application/x-gzip");
            b.textFile("uploadToken",new ByteArrayInputStream(token.getBytes()));
             b.multipart(true);
        })

        .response().doOnError(e -> log.error("World upload request failed",e)).block();

    }



    public byte[] getWorld(int worldId) {
        String downloadURL = "worldcraft-web/rooms/%d/game.tar.gz";
        return getClient().get()
                .uri(serverURL+String.format(downloadURL,worldId))
                .responseContent()
                .aggregate()
                .asByteArray()
                .block();
    }
}
