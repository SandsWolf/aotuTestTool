package com.autoyol.entity;

public class CtripUser {
    private String IdNo;

    public String getIdNo() {
        return IdNo;
    }

    public void setIdNo(String idNo) {
        IdNo = idNo;
    }

    public String getIdType() {
        return IdType;
    }

    public void setIdType(String idType) {
        IdType = idType;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    private String IdType;
    private String Mobile;
    private String Name;

}
