package com.mikolka9144.WoCserver.config;

import com.mikolka9144.WoCserver.modules.http.HttpOffictalInterceptor;
import com.mikolka9144.WoCserver.modules.socket.PacketOffitialInterceptor;
import com.mikolka9144.WoCserver.model.Packet.PacketInterceptor;
import com.mikolka9144.WoCserver.logic.WorldcraftServer;
import com.mikolka9144.WoCserver.logic.http.HttpServer;
import com.mikolka9144.WoCserver.logic.socket.SocketServer;
import com.mikolka9144.WoCserver.modules.socket.PacketConverter;
import com.mikolka9144.WoCserver.modules.http.WoC287WorldFixer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GigachadWoC {
    public static void main(String hostname, int port, int hostingPort) throws IOException {
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