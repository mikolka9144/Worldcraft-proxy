package com.mikolka9144.Models.EventCodecs;

public class PurchaseValidationReq {
    private String purchaseName;
    private String bundleId;
    private String receipt;

    public PurchaseValidationReq(String purchaseName, String bundleId, String receipt) {
        this.purchaseName = purchaseName;
        this.bundleId = bundleId;
        this.receipt = receipt;
    }

    public String getPurchaseName() {
        return purchaseName;
    }

    public void setPurchaseName(String purchaseName) {
        this.purchaseName = purchaseName;
    }

    public String getBundleId() {
        return bundleId;
    }

    public void setBundleId(String bundleId) {
        this.bundleId = bundleId;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }
}
