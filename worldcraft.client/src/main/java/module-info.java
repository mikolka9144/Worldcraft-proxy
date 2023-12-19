module worldcraft.client {
    requires utills;
    requires PacketParser;
    requires static lombok;
    requires org.slf4j;
    requires unirest.java.core;
    requires java.net.http;

    exports com.mikolka9144.worldcraft.backend.base.socket;
    exports com.mikolka9144.worldcraft.backend.base.api;
    exports com.mikolka9144.worldcraft.backend.base.http;
}