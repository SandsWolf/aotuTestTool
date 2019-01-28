package com.autoyol.entity;

public class CommUseDriver {
    private String mem_no;              //租客memNo
    private String realName;            //附加驾驶人真是姓名
    private String mobile;              //附加驾驶人手机号
    private String id_card;             //附加驾驶人身份证
    private String dri_lic_allow_car;   //附加驾驶人驾照类型
    private String is_auth;             //'是否认证 0-未认证，1-认证通过，2认证失败',
    private String is_delete;           //'逻辑删除字段 0-正常，1-已逻辑删除',

    public String getMem_no() {
        return mem_no;
    }

    public void setMem_no(String mem_no) {
        this.mem_no = mem_no;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getId_card() {
        return id_card;
    }

    public void setId_card(String id_card) {
        this.id_card = id_card;
    }

    public String getDri_lic_allow_car() {
        return dri_lic_allow_car;
    }

    public void setDri_lic_allow_car(String dri_lic_allow_car) {
        this.dri_lic_allow_car = dri_lic_allow_car;
    }

    public String getIs_auth() {
        return is_auth;
    }

    public void setIs_auth(String is_auth) {
        this.is_auth = is_auth;
    }

    public String getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(String is_delete) {
        this.is_delete = is_delete;
    }

    @Override
    public String toString() {
        return "CommUseDriver{" +
                "mem_no='" + mem_no + '\'' +
                ", realName='" + realName + '\'' +
                ", mobile='" + mobile + '\'' +
                ", id_card='" + id_card + '\'' +
                ", dri_lic_allow_car='" + dri_lic_allow_car + '\'' +
                ", is_auth='" + is_auth + '\'' +
                ", is_delete='" + is_delete + '\'' +
                '}';
    }
}
