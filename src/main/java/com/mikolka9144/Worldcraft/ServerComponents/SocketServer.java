package com.mikolka9144.Worldcraft.ServerComponents;

import com.mikolka9144.Models.Packet;
import com.mikolka9144.Models.PacketInterceptor;
import com.mikolka9144.Models.WorldcraftSocket;
import com.mikolka9144.Worldcraft.WorldCraftPacketIO;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.List;
import java.util.function.Function;

public class SocketServer implements Closeable {
    public static final int WORLD_OF_CRAFT_PORT = 443;
    public static final int WORLDCRAFT_PORT = 12530;
    private final ServerSocket serverSocket;
    private Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors;

    public SocketServer(int port, Function<WorldCraftPacketIO, List<PacketInterceptor>> interceptors) throws IOException {
        serverSocket = new ServerSocket(port);
        this.interceptors = interceptors;
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

    @Override
    public void close() throws IOException {
        serverSocket.close();
    }
}
