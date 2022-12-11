package com.mikolka9144;

import com.mikolka9144.WoCserver.Impl.Http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.Impl.packets.PacketOffitialInterceptor;
import com.mikolka9144.WoCserver.Models.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.ServerComponents.WorldcraftServer;
import com.mikolka9144.WoCserver.ServerComponents.http.HttpServer;
import com.mikolka9144.WoCserver.ServerComponents.socket.SocketServer;
import com.mikolka9144.WoCserver.legacy.PacketConverter;
import com.mikolka9144.WoCserver.legacy.WoC287WorldFixer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        String hostname = System.getenv("SERVER_IP"); //64.237.54.60
        int port = Integer.parseInt(System.getenv("SERVER_PORT")); //443
        WorldcraftServer server = new WorldcraftServer();
        server.createHttpServer(
                    HttpServer.WORLDCRAFRT_HTTP_PORT,
                    List.of(
                            new HttpOffictalInterceptor(),
                            new WoC287WorldFixer()
                    ),
                    List.of(
                            new HttpOffictalInterceptor()
                    )
            );
        server.createSocketServer(
                    SocketServer.WORLDCRAFT_PORT,
                    (io,x) -> {
                        var ret = new ArrayList<PacketInterceptor>();
                        ret.add(0,new PacketConverter.Early(io));
                        ret.add(new PacketConverter.Late(io));
                        x.setInterceptors(ret);
                        return ret;
                    }, x -> new PacketOffitialInterceptor(x,hostname,port)
            );
            server.start();
    }
}