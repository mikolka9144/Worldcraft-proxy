package com.mikolka9144.worldcraft.socket.model;

public class VersionFlags {
    public boolean shoudFixLoginMarket;
    public boolean shoudIncludeReadOlnyRoomStatus;
    public boolean simplifyChat;
    public boolean simplifyAlerts;

    public VersionFlags(PacketProtocol proto) {
        switch (proto) {
            case WORLDCRAFT_2_7_4, WORLDCRAFT_2_8_7 -> setFlags(true, false, true, true);
            case WORLD_OF_CRAFT -> setFlags(true, true, false, false);
            default -> setFlags(false, true, false, false);
        }
    }

    private void setFlags(boolean shoudFixLoginMarket, boolean shoudIncludeReadOlnyRoomStatus, boolean simplifyChat, boolean simplifyAlerts) {
        this.shoudFixLoginMarket = shoudFixLoginMarket;
        this.shoudIncludeReadOlnyRoomStatus = shoudIncludeReadOlnyRoomStatus;
        this.simplifyChat = simplifyChat;
        this.simplifyAlerts = simplifyAlerts;
    }
}
