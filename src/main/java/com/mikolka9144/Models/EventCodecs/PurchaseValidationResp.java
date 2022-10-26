package com.mikolka9144.Models.EventCodecs;

public class PurchaseValidationResp {
    public enum Status{
        NULL,
        Sucsess,
        Fail,
        Retry
    }

    public PurchaseValidationResp(Status status, String receipt) {
        this.status = status;
        this.receipt = receipt;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReceipt() {
        return receipt;
    }

    public void setReceipt(String receipt) {
        this.receipt = receipt;
    }

    private Status status;
    private String receipt;
}
