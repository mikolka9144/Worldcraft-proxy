package com.mikolka9144.worldcraft.http;

import com.mikolka9144.worldcraft.common.config.ServerConfig;
import com.mikolka9144.worldcraft.http.interceptors.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.interceptors.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldDownloadRequest;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController()
@RequestMapping("/worldcraft-web")
@AllArgsConstructor
public class HttpServer {

    private ServerConfig configuration;
    @GetMapping("rooms/{roomId}/game.tar.gz")
    public ResponseEntity<byte[]> getWorld(@PathVariable int roomId){
        WorldDownloadRequest request = new WorldDownloadRequest(roomId,null);
        for (HttpDownloadInterceptor interceptor : configuration.getHttpDownloadInterceptors()) {
            interceptor.getWorld(request);
        }
        return ResponseEntity.ok()
                .header("Content-Type","application/x-gzip")
                .body(request.getWorld().toTarGzBin());
    }

    @PostMapping("upload")
    public ResponseEntity<byte[]> uploadWorld(@RequestPart("file") MultipartFile worldBin, @RequestPart("uploadToken") String token) throws IOException {

        WorldUploadRequest world = new WorldUploadRequest(token,worldBin.getBytes());
        for (HttpUploadInterceptor interceptor : configuration.getHttpUploadInterceptors()) {
            interceptor.uploadWorld(world);
        }
        return ResponseEntity.ok("OK".getBytes());
    }
}
