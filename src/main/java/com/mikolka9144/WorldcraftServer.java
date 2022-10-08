package com.mikolka9144;

import com.mikolka9144.Impl.HttpOffictalInterceptor;
import com.mikolka9144.Models.Packet;
import com.mikolka9144.Models.PacketInterceptor;
import com.mikolka9144.Models.WorldcraftSocket;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;
import com.mikolka9144.Worldcraft.WorldcraftHttpProxy;
import com.sun.net.httpserver.HttpServer;

import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.function.Function;

public class WorldcraftServer implements Closeable {
    public static final int WORLD_OF_CRAFRT_HTTP_PORT = 80;
    public static final int WORLDCRAFRT_HTTP_PORT = 8080;
    private final ServerSocket serverSocket;
    private final HttpServer httpServer;;
    private Function<WorldCraftPacketIO,List<PacketInterceptor>> interceptors;

    WorldcraftServer(int httpProxyPort, Function<WorldCraftPacketIO,List<PacketInterceptor>> interceptors) throws IOException {
        this.interceptors = interceptors;
        serverSocket = new ServerSocket(443);
         WorldcraftHttpProxy proxy = new WorldcraftHttpProxy();
         proxy.getDownloadInterceptors().add(new HttpOffictalInterceptor());

         httpServer = HttpServer.create(new InetSocketAddress(httpProxyPort),50);
         httpServer.setExecutor(Executors.newFixedThreadPool(10));
         httpServer.createContext("/worldcraft-web/",proxy);
         httpServer.start();
    }
    @Override
    public void close() throws IOException {
        serverSocket.close();
        httpServer.stop(0);
    }
    public void start() throws IOException {
        while (true){
            WorldcraftSocket socket = new WorldcraftSocket(serverSocket.accept());
            List<PacketInterceptor> socketInter = interceptors.apply(socket.getChannel());
            new Thread(() -> worldcraftClientHandler(socket,socketInter)).start();
        }
    }
    private void worldcraftClientHandler(WorldcraftSocket socket,List<PacketInterceptor> socketInter){
        try {
            while (true) {
                Packet packet = socket.getChannel().recive();
                socketInter.forEach(s -> s.InterceptRawPacket(packet));
                switch (packet.getCommand()){

                }
            }
        }
        catch (IOException x) {
            System.out.println(x);
            try {
                for (PacketInterceptor packetInterceptor : socketInter) {
                    packetInterceptor.close();
                }
            }
            catch (IOException y){
                System.out.println(y);
            }
        }
    }
}
