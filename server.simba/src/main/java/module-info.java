module server.simba {
    requires serverBackend;
    requires static lombok;
    requires org.slf4j;

    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires PacketParser;
    requires LevelParser;
    exports com.mikolka9144.worldcraft.simba;
    exports com.mikolka9144.worldcraft.simba.backend.Monika;
    opens com.mikolka9144.worldcraft;
}