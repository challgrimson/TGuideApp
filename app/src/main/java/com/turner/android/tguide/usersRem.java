package com.turner.android.tguide;

/**
 * Created by Christian on 5/21/2018.
 */

public class usersRem {

    private String titleText;
    private String description;
    private String dateText;
    private String timeText;
    private String repeat;
    private String repeatNum;
    private String repeatType;
    private String sound;
    private String uniqueID;

    public usersRem(){}
   //reff.child("usersRem").child(userr.getUid()).child(id).child("SURV_ALARM_TIME").setValue(time);
      //  reff.child("usersRem").child(userr.getUid()).child(id).child("SURV_UNIQE_ID").setValue(uniqueid);
      //  reff.child("usersRem").child(userr.getUid()).child(id).child("id").setValue(id);
    public usersRem(String titleText,
             String description,
             String dateText,
             String timeText,
             String repeat,
             String repeatNum,
             String repeatType,
             String sound,
             String uniqueID){

        this.titleText = titleText;
        this.description = description;
        this.dateText = dateText;
        this.timeText = timeText;
        this.repeat = repeat;
        this.repeatNum = repeatNum;
        this.repeatType = repeatType;
        this.sound = sound;
        this.uniqueID = uniqueID;

    }

    public String gettitleText() {return titleText;}
    public String getdescription() {return description;}
    public String getdateText() {return dateText;}
    public String gettimeText() {return timeText;}
    public String getrepeat() {return repeat;}
    public String getrepeatNum()  {return repeatNum; }
    public String getrepeatType() {return repeatType;}
    public String getsound() {return sound;}
    public String getuniqueID() {return uniqueID;}

}
