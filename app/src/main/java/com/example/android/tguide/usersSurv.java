package com.example.android.tguide;

/**
 * Created by Christian on 6/4/2018.
 */

public class usersSurv {

    private String SURV_ALARM_TIME;
    private String SURV_UNIQE_ID;
    private String id;

    public usersSurv(){}

    public usersSurv(String SURV_ALARM_TIME,
                    String SURV_UNIQE_ID,
                    String id){

        this.SURV_ALARM_TIME = SURV_ALARM_TIME;
        this.SURV_UNIQE_ID = SURV_UNIQE_ID;
        this.id = id;

    }

    public String getSURV_ALARM_TIME() {return SURV_ALARM_TIME;}
    public String getSURV_UNIQE_ID() {return SURV_UNIQE_ID;}
    public String getid() {return id;}
}
