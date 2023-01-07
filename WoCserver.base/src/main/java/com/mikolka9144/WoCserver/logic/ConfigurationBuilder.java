package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.HttpDownloadInterceptor;
import com.mikolka9144.WoCserver.model.HttpUploadInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.model.ServerConfig;
import com.mikolka9144.WoCserver.modules.http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.modules.http.WoC287WorldFixer;
import com.mikolka9144.WoCserver.modules.socket.*;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
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
        ClientInterceptorFunc socketPreset;

        List<HttpDownloadInterceptor> httpDownloadPreset = new ArrayList<>();
        httpDownloadPreset.add(new HttpOffictalInterceptor.Downloader(targetServer,targetServerHttpPort));

        List<HttpUploadInterceptor> httpUploadPreset = new ArrayList<>();

        // this function creates server interceptor (mostly to inject packets to server)
        Function<WorldCraftPacketIO, PacketServer> serverFunction = (io) ->
                new PacketOffitialInterceptor(io,targetServer,targetServerSocketPort);

        switch (preset){
            case Default -> socketPreset  = (client, server) -> List.of(
                    new PurchaseFaker(client)
            );
            case Legacy -> {
                socketPreset = (io,server) -> List.of(
                  new PacketConverter.Early(io),
                  new PacketConverter.Late(io)
                );
                httpDownloadPreset.add(new WoC287WorldFixer());
            }
            case Cmd -> socketPreset = (client, server) -> List.of(
                    new ChatCommandsInterceptor(client,server),
                    new PacketLogger(client),
                    new DeviceSpoofer(client)
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
