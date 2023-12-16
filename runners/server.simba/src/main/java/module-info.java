module server.simba {
    requires worldcraft.spring;
    requires static lombok;
    requires org.slf4j;

    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires PacketParser;
    requires LevelParser;
    requires utills;
    requires worldcraft.client;
    exports com.mikolka9144.worldcraft.simba;
    exports com.mikolka9144.worldcraft.simba.backend.Monika;
    opens com.mikolka9144.worldcraft;
}