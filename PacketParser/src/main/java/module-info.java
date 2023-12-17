module PacketParser {
    requires static lombok;
    requires org.slf4j;
    requires utills;
    requires com.google.gson;
    exports com.mikolka9144.packet.packet;
    exports com.mikolka9144.packet.packet.errorcodes;
    exports com.mikolka9144.packet.packet.codecs;
    exports com.mikolka9144.packet.packet.encodings;

}