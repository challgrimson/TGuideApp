package com.example.android.tguide;

/**
 * Created by Christian on 6/5/2018.
 */

public class usersRdCard {
    private boolean box1;
    private boolean box2;
    private boolean box3;

    public usersRdCard() {
    }

    public usersRdCard(boolean box1,
                       boolean box2,
                       boolean box3) {

        this.box1 = box1;
        this.box2 = box2;
        this.box3 = box3;

    }

    public boolean getbox1() {
        return box1;
    }

    public boolean getbox2() {
        return box2;
    }

    public boolean getbox3() {
        return box3;
    }

}
