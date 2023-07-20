package com.mikolka9144.worldcraft.http;

import com.mikolka9144.worldcraft.common.level.World;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.socket.model.ServerConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public ResponseEntity<byte[]> uploadWorld(RequestEntity<byte[]> request){
        byte[] data = request.getBody();

        String contentType = request.getHeaders().getFirst("Content-type");
        for (HttpUploadInterceptor interceptor : configuration.getHttpUploadInterceptors()) {
            data = interceptor.uploadWorld(data,contentType);
        }
        return ResponseEntity.ok(data);
    }
}
