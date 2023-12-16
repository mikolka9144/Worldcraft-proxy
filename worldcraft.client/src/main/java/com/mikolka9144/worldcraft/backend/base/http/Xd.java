package com.mikolka9144.worldcraft.backend.base.http;

import reactor.netty.http.client.HttpClient;

public class Xd {
    private Xd(){}
    public static HttpClient getClient() {
        return HttpClient.create();
    }
}
