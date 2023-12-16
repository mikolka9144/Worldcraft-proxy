module worldcraft.client {
    requires utills;
    requires PacketParser;
    requires static lombok;
    requires org.slf4j;
    requires reactor.netty.http;
    requires reactor.netty.core;
    requires io.netty.codec.http;
    requires reactor.core;
    exports com.mikolka9144.worldcraft.backend.base.socket;
    exports com.mikolka9144.worldcraft.backend.base.api;
    exports com.mikolka9144.worldcraft.backend.base.interceptor;
    exports com.mikolka9144.worldcraft.backend.base.socket.server;
    exports com.mikolka9144.worldcraft.backend.base.http;
}