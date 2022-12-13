package com.mikolka9144.WoCserver.model.EventCodecs;

public class LoginInfo {
    private String username;
    private short skinId;
    private String clientVer;

    public String getMarketName() {
        return marketName;
    }

    public void setMarketName(String marketName) {
        this.marketName = marketName;
    }

    private String marketName;
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public short getSkinId() {
        return skinId;
    }

    public void setSkinId(short skinId) {
        this.skinId = skinId;
    }

    public String getClientVer() {
        return clientVer;
    }

    public void setClientVer(String clientVer) {
        this.clientVer = clientVer;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAndroidVer() {
        return androidVer;
    }

    public void setAndroidVer(String androidVer) {
        this.androidVer = androidVer;
    }

    public String getAndroidAPI() {
        return androidAPI;
    }

    public void setAndroidAPI(String androidAPI) {
        this.androidAPI = androidAPI;
    }

    public LoginInfo(String username, short skinId, String clientVer, String deviceId, String deviceName, String androidVer, String androidAPI,String marketName) {
        this.username = username;
        this.skinId = skinId;
        this.clientVer = clientVer;
        this.deviceId = deviceId;
        this.deviceName = deviceName;
        this.androidVer = androidVer;
        this.androidAPI = androidAPI;
        this.marketName = marketName;
    }

    private String deviceId;
    private String deviceName;
    private String androidVer;
    private String androidAPI;
}
