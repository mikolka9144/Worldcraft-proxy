module Server {
    requires spring.boot;
    requires spring.core;
    requires spring.beans;
    requires spring.context;
    requires spring.boot.autoconfigure;
    requires static lombok;
    requires org.slf4j;
    requires spring.web;
    requires PacketParser;
    requires LevelParser;

    // tests need this apparently...
    exports  com.mikolka9144.worldcraft.socket.interceptor;
}