package com.autoyol.entity;

public class CtripStore {

    private String PickupCityCode;
    private String ReturnCityCode;
    private String PickupStoreCode;

    public String getPickupCityCode() {
        return PickupCityCode;
    }

    public void setPickupCityCode(String pickupCityCode) {
        PickupCityCode = pickupCityCode;
    }

    public String getReturnCityCode() {
        return ReturnCityCode;
    }

    public void setReturnCityCode(String returnCityCode) {
        ReturnCityCode = returnCityCode;
    }

    public String getPickupStoreCode() {
        return PickupStoreCode;
    }

    public void setPickupStoreCode(String pickupStoreCode) {
        PickupStoreCode = pickupStoreCode;
    }

    public String getReturnStoreCode() {
        return ReturnStoreCode;
    }

    public void setReturnStoreCode(String returnStoreCode) {
        ReturnStoreCode = returnStoreCode;
    }

    private String ReturnStoreCode;

}
