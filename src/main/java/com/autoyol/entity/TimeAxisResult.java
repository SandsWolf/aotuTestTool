package com.autoyol.entity;

public class TimeAxisResult {
    private String name;                 //表名
    private String type;                 //该字段用于前端判断展示样式
    private Object timeAxis;             //时间轴对比后结果
    private String timeAxisFlag;         //整个时间轴是否可租标志
    private String msg;                  //表的不可租时间轴msg
    private String resultMsg;           //比较后结果msg

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Object getTimeAxis() {
        return timeAxis;
    }

    public void setTimeAxis(Object timeAxis) {
        this.timeAxis = timeAxis;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeAxisFlag() {
        return timeAxisFlag;
    }

    public void setTimeAxisFlag(String timeAxisFlag) {
        this.timeAxisFlag = timeAxisFlag;
    }


}
