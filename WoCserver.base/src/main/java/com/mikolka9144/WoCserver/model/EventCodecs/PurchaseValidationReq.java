package com.mikolka9144.WoCserver.model.EventCodecs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseValidationReq {
    private String purchaseName;
    private String bundleId;
    private String receipt;
}
