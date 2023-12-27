package com.mikolka9144.worldcraft.backend.packets.codecs;

import com.google.gson.Gson;
import lombok.*;


@SuppressWarnings("PublicField")
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PurchasesList {
    public String PurchaseId;
    public String Coins;
    public String MarketOperationVersion;
    public String Resurrections;

    @SneakyThrows
    public static PurchasesList decodefromJson(String jsonData) {
        Gson gson = new Gson();
        String payload = jsonData
                .replace("\n", "")
                .replace(",}", "}")
                .replace("{", "{\"")
                .replace(": ", "\":\"")
                .replace(",", "\",\"")
                .replace("}", "\"}");
        return gson.fromJson(payload, PurchasesList.class);
    }

    @SneakyThrows
    public String encodeToJson() {

        return new Gson().toJson(this)
                .replace("{", "{\n")
                .replace("}", "\n}")
                .replace(",", ",\n")
                .replace("\"", "")
                .replace(":", ": ")
                ;
    }
}


