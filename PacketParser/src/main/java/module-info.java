module PacketParser {
    requires static lombok;
    requires org.slf4j;
    requires utills;
    requires com.google.gson;
    exports com.mikolka9144.worldcraft.backend.packets;
    exports com.mikolka9144.worldcraft.backend.packets.errorcodes;
    exports com.mikolka9144.worldcraft.backend.packets.codecs;
    exports com.mikolka9144.worldcraft.backend.packets.encodings;

}