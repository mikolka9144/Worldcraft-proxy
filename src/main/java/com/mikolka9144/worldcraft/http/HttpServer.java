package com.mikolka9144.worldcraft.http;

import com.mikolka9144.worldcraft.common.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.model.WorldUploadRequest;
import com.mikolka9144.worldcraft.common.config.ServerConfig;
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
        World world = null;

        for (HttpDownloadInterceptor interceptor : configuration.getHttpDownloadInterceptors()) {
            world = interceptor.getWorld(roomId,world);
        }
        return ResponseEntity.ok()
                .header("Content-Type","application/x-gzip")
                .body(world.toTarGzBin());
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
