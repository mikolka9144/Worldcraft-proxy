module PacketParser {
    requires static lombok;
    requires org.slf4j;
    requires com.fasterxml.jackson.databind;
    exports com.mikolka9144.packet;
    exports com.mikolka9144.packet.packet;
    exports com.mikolka9144.packet.packet.enums;
    exports com.mikolka9144.packet.packet.build;
    exports com.mikolka9144.packet.packet.errorcodes;
    exports com.mikolka9144.packet.packet.codecs;
    exports com.mikolka9144.packet.packet.encodings;

}