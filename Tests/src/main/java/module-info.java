module Tests {
    requires spring.context;
    requires spring.beans;
    requires utills;
    requires worldcraft.server;
    requires PacketParser;
    requires worldcraft.client;
    requires LevelParser;
    requires spring.boot;
    requires spring.core;
    requires spring.boot.autoconfigure;
    opens com.mikolka9144.tests;
}