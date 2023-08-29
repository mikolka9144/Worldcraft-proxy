package com.mikolka9144.worldcraft.socket.model.EventCodecs;

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
    String PurchaseId;
    @JsonProperty("Coins")
    String Coins;
    @JsonProperty("MarketOperationVersion")
    String MarketOperationVersion;
    @JsonProperty("Resurrections")
    String Resurrections;
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
