module worldcraft.client {
    requires utills;
    requires PacketParser;
    requires static lombok;
    requires org.slf4j;
    requires unirest.java.core;
    requires java.net.http;

    exports com.mikolka9144.worldcraft.backend.client.socket;
    exports com.mikolka9144.worldcraft.backend.client.api;
    exports com.mikolka9144.worldcraft.backend.client.http;
    exports com.mikolka9144.worldcraft.backend.client.socket.interceptor;
}