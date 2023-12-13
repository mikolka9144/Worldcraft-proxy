module serverBackend {
    requires spring.web;
    requires static lombok;
    requires spring.core;
    requires spring.beans;
    requires spring.context;

    requires org.slf4j;
    requires PacketParser;
    requires LevelParser;
    exports com.mikolka9144.worldcraft.socket.api;
    exports com.mikolka9144.worldcraft.socket;
    exports com.mikolka9144.worldcraft.config;
    exports com.mikolka9144.worldcraft.http.interceptors;
    exports com.mikolka9144.worldcraft.http;
    exports com.mikolka9144.worldcraft.http.model;
    exports com.mikolka9144.worldcraft.socket.server;
    exports com.mikolka9144.worldcraft.official;
    exports com.mikolka9144.worldcraft.unify;
    exports com.mikolka9144.worldcraft.unify.convert;
    opens com.mikolka9144.worldcraft.config;
    // tests need this apparently...
    exports  com.mikolka9144.worldcraft.socket.interceptor;
}