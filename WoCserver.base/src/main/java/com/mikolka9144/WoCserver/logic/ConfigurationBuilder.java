package com.mikolka9144.WoCserver.logic;

import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.logic.socket.WorldCraftPacketIO;
import com.mikolka9144.WoCserver.model.HttpInterceptor;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.ClientInterceptorFunc;
import com.mikolka9144.WoCserver.model.Packet.Interceptors.PacketInterceptor;
import com.mikolka9144.WoCserver.model.Packet.PacketServer;
import com.mikolka9144.WoCserver.model.ServerConfig;
import com.mikolka9144.WoCserver.modules.http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.modules.http.WoC287WorldFixer;
import com.mikolka9144.WoCserver.modules.socket.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

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
        throw new IllegalStateException("UNIMPLEMENTED PRESET REQUESTED!!!("+preset.name()+")");
    }
    public static ServerConfig configure(ConfigPreset preset, String targetServer, int targetServerHttpPort, int targetServerSocketPort, int hostingHttpPort, int hostingSocketPort){
        ClientInterceptorFunc socketPreset = null;
        List<HttpInterceptor> httpDownloadPreset = new ArrayList<>();
        httpDownloadPreset.add(new HttpOffictalInterceptor(targetServer,targetServerHttpPort));

        Function<WorldCraftPacketIO, PacketServer> serverCreator = (io) ->
                new PacketOffitialInterceptor(io,targetServer,targetServerSocketPort);

        switch (preset){
            case Default -> socketPreset  = (client, server) -> List.of(
                    new PurchaseFaker(client)
            );
            case Legacy -> {
                socketPreset = (io,server) -> {
                    var ret = new ArrayList<PacketInterceptor>();
                    ret.add(0,new PacketConverter.Early(io));
                    ret.add(new PacketConverter.Late(io));
                    return ret;
                };
                httpDownloadPreset.add(new WoC287WorldFixer());
            }
            case Cmd -> socketPreset = (client, server) -> List.of(
                    new ChatCommandsInterceptor(client,server),
                    new PacketLogger(client),
                    new DeviceSpoofer(client)
            );
        }

        return new ServerConfig(hostingSocketPort,hostingHttpPort,socketPreset,httpDownloadPreset,serverCreator);
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
