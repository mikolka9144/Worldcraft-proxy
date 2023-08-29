package com.mikolka9144.worldcraft.http;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.MultipartBodyBuilder;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class HttpClient {
    private String serverURL = "http://%s:%d/";
    private String uploadURL = "worldcraft-web/upload";
    private String downloadURL = "worldcraft-web/rooms/%d/game.tar.gz";
    public HttpClient(String target,int port){
        serverURL = String.format(serverURL,target,port);
    }
    public void uploadWorld(String token,byte[] worldBin) {
        var test = new MultipartBodyBuilder();
        test.part("file",worldBin)
                .filename("game.tar.gz")
                .header("Content-Type","application/x-gzip");
        test.part("uploadToken",token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set("Accept-Encoding","gzip");
        HttpEntity<MultiValueMap<String, HttpEntity<?>>> request = new HttpEntity<>(test.build(),headers);

        RestTemplate template = new RestTemplate();
        template.postForEntity(serverURL+uploadURL,request, String.class);
    }
    public byte[] getWorld(int worldId) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<byte[]> world = template.getForEntity(
                serverURL+String.format(downloadURL,worldId), byte[].class);
        return world.getBody();
    }
}
