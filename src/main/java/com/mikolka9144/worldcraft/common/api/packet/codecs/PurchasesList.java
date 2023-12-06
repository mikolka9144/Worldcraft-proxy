package com.mikolka9144.worldcraft.common.api.packet.codecs;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class PurchasesList {
    @JsonProperty("PurchaseId")
    String purchaseId;
    @JsonProperty("Coins")
    String coins;
    @JsonProperty("MarketOperationVersion")
    String marketOperationVersion;
    @JsonProperty("Resurrections")
    String resurrections;
    @SneakyThrows
    public static PurchasesList decodefromJson(String jsonData){
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = jsonData
                .replace("\n","")
                .replace(",}","}")
                .replace("{","{\"")
                .replace(": ","\":\"")
                .replace(",","\",\"")
                .replace("}","\"}");
        return objectMapper.readValue(payload, PurchasesList.class);
    }
    @SneakyThrows
    public String encodeToJson(){
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(this)
                .replace("{","{\n")
                .replace("}","\n}")
                .replace(",",",\n")
                .replace("\"","")
                .replace(":",": ")
                ;
    }
}
