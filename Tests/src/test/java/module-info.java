module Tests {
    requires PacketParser;
    requires org.assertj.core;
    requires junit;
    requires org.slf4j;
    requires static lombok;
    requires worldcraft.server;
    requires worldcraft.client;
    requires utills;
    requires org.junit.jupiter.api;
    requires LevelParser;
    exports tests;
    opens tests;
}