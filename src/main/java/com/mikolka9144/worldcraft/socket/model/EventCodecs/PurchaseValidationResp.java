package com.mikolka9144.worldcraft.socket.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseValidationResp {
    public enum Status{
        NULL,
        Sucsess,
        Fail,
        Retry
    }
    private Status status;
    private String receipt;
}
