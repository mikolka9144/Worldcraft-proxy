package com.mikolka9144.worldcraft;

import com.mikolka9144.worldcraft.http.HttpServer;
import com.mikolka9144.worldcraft.http.model.HttpDownloadInterceptor;
import com.mikolka9144.worldcraft.http.model.HttpUploadInterceptor;
import com.mikolka9144.worldcraft.http.modules.WoC287WorldFixer;
import com.mikolka9144.worldcraft.http.servers.HttpOffictalInterceptor;
import com.mikolka9144.worldcraft.socket.SocketServer;
import com.mikolka9144.worldcraft.socket.logic.WorldCraftPacketIO;
import com.mikolka9144.worldcraft.socket.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.worldcraft.socket.model.Packet.PacketServer;
import com.mikolka9144.worldcraft.socket.model.ServerConfig;
import com.mikolka9144.worldcraft.socket.modules.*;
import com.mikolka9144.worldcraft.socket.servers.PacketOffitialInterceptor;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

@Slf4j
public class ConfigurationBuilder {
    public static ServerConfig configure(ServerPreset preset){
        switch (preset){
            case Official ->{
                return configure(ConfigPreset.Default,
                    "64.237.54.60",
                    80,
                    SocketServer.WORLD_OF_CRAFT_PORT,
                    80,
                    SocketServer.WORLD_OF_CRAFT_PORT);
            }
            case Legacial ->{
                return configure(ConfigPreset.Legacy,
                        "64.237.54.60",
                        HttpServer.WORLD_OF_CRAFRT_HTTP_PORT,
                        SocketServer.WORLD_OF_CRAFT_PORT,
                        HttpServer.WORLDCRAFT_HTTP_PORT,
                        SocketServer.WORLDCRAFT_PORT);
            }
        }
        log.error("UNIMPLEMENTED PRESET REQUESTED!!!("+preset.name()+")");
        throw new RuntimeException();
    }
    public static ServerConfig configure(ConfigPreset preset, String targetServer, int targetServerHttpPort, int targetServerSocketPort, int hostingHttpPort, int hostingSocketPort){
        Supplier<List<PacketInterceptor>> socketPreset;

        List<HttpDownloadInterceptor> httpDownloadPreset = new ArrayList<>();
        httpDownloadPreset.add(new HttpOffictalInterceptor.Downloader(targetServer,targetServerHttpPort));

        List<HttpUploadInterceptor> httpUploadPreset = new ArrayList<>();

        // this function creates server interceptor (mostly to inject packets to server)
        Function<WorldCraftPacketIO, PacketServer> serverFunction = (io) ->
                new PacketOffitialInterceptor(io,targetServer,targetServerSocketPort);

        switch (preset){
            case Default -> socketPreset  = () -> List.of(
                    new PurchaseFaker()
            );
            case Legacy -> {
                socketPreset = () -> List.of(
                  new PacketConverter.Early(),
                  new GameVersionSpoofer(),
                 new PacketLogger(),
                 new PacketConverter.Late()
                );
                httpDownloadPreset.add(new WoC287WorldFixer());
            }
            case Cmd -> socketPreset = () -> List.of(
                    new ChatCommandsInterceptor(),
                    new PacketLogger(),
                    new DeviceSpoofer()
            );
            default -> {
                log.error("Preset "+preset.name()+" is missing in configurations WTF!!!");
                throw new RuntimeException();
            }
        }
        //Uploader must be added last
        httpUploadPreset.add(new HttpOffictalInterceptor.Uploader(targetServer,targetServerSocketPort));

        return new ServerConfig(hostingSocketPort,hostingHttpPort,socketPreset,httpDownloadPreset,httpUploadPreset,serverFunction);
    }
    public enum ConfigPreset {
        Default,
        Legacy,
        Cmd
    }
    public enum ServerPreset{
        Official,
        Legacial
    }
}
