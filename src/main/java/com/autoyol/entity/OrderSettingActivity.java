package com.autoyol.entity;

public class OrderSettingActivity {
    private String activity_title;  //'活动名称，如：春节，国庆'
    private String begin_date;      //'活动开始时间(yyyyMMdd)'
    private String end_date;        //'活动结束时间(yyyyMMdd)'

    public String getActivity_title() {
        return activity_title;
    }

    public void setActivity_title(String activity_title) {
        this.activity_title = activity_title;
    }

    public String getBegin_date() {
        return begin_date;
    }

    public void setBegin_date(String begin_date) {
        this.begin_date = begin_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    @Override
    public String toString() {
        return "OrderSettingActivity{" +
                "activity_title='" + activity_title + '\'' +
                ", begin_date='" + begin_date + '\'' +
                ", end_date='" + end_date + '\'' +
                '}';
    }
}
