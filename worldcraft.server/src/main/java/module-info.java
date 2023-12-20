module worldcraft.server {
    requires spring.web;
    requires static lombok;
    requires spring.core;
    requires spring.beans;
    requires spring.context;

    requires org.slf4j;
    requires PacketParser;
    requires LevelParser;
    requires utills;
    requires worldcraft.client;
    exports com.mikolka9144.worldcraft.backend.server.socket;
    exports com.mikolka9144.worldcraft.backend.server.socket.interceptor;
    exports com.mikolka9144.worldcraft.backend.server.config;
    exports com.mikolka9144.worldcraft.backend.server.http.interceptors;
    exports com.mikolka9144.worldcraft.backend.server.http;
    exports com.mikolka9144.worldcraft.backend.server.http.model;
    exports com.mikolka9144.worldcraft.backend.server.official;
    exports com.mikolka9144.worldcraft.backend.server.unify;
    exports com.mikolka9144.worldcraft.backend.server.unify.convert;
    opens com.mikolka9144.worldcraft.backend.server.config;
}