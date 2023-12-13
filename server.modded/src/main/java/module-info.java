module Server {
    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires static lombok;
    requires org.slf4j;
    requires PacketParser;
    requires LevelParser;
    requires serverBackend;
    exports com.mikolka9144.worldcraft.modules.officialConnect;
    exports com.mikolka9144.worldcraft.modules.worldcraft.unify;
}