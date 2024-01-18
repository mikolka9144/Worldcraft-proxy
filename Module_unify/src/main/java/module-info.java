module Module_unify {
    requires utills;
    requires static lombok;
    requires PacketParser;
    requires worldcraft.server;
    requires spring.context;
    requires LevelParser;
    requires org.slf4j;
    requires worldcraft.client;
    requires spring.beans;
    exports com.mikolka9144.worldcraft.interceptors.unify;
    exports com.mikolka9144.worldcraft.interceptors.unify.convert;
}