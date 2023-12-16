module worldcraft.spring {
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
    exports com.mikolka9144.worldcraft.backend.spring.socket;
    exports com.mikolka9144.worldcraft.backend.spring.config;
    exports com.mikolka9144.worldcraft.backend.spring.http.interceptors;
    exports com.mikolka9144.worldcraft.backend.spring.http;
    exports com.mikolka9144.worldcraft.backend.spring.http.model;
    exports com.mikolka9144.worldcraft.backend.spring.official;
    exports com.mikolka9144.worldcraft.backend.spring.unify;
    exports com.mikolka9144.worldcraft.backend.spring.unify.convert;
    opens com.mikolka9144.worldcraft.backend.spring.config;
}