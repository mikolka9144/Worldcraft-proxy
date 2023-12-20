module server.hacking {
    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires static lombok;
    requires org.slf4j;
    requires PacketParser;
    requires LevelParser;
    requires worldcraft.server;
    requires utills;
    requires worldcraft.client;
    opens com.mikolka9144.worldcraft;
    exports com.mikolka9144.worldcraft.hacked.modules.hackoring;
    exports com.mikolka9144.worldcraft.hacked.modules.hackoring.fakers;
    exports com.mikolka9144.worldcraft.hacked.modules.debug;
    exports com.mikolka9144.worldcraft.hacked.modules;
    exports com.mikolka9144.worldcraft.hacked.modules.debug.http;

}