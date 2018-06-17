package com.example.android.tguide;

/**
 * Created by Christian on 5/2/2018.
 */

public class User {

    private boolean clBox1;
    private boolean clBox2;
    private boolean clBox3;
    private boolean clBox4;

    private boolean box1;
    private boolean box2;
    private boolean box3;

    private boolean firstTime;

    private String mPAPtext;
    private String mTHYtext;
    private String mCELtext;
    private String  mECGtext;
    private String mECHtext;
    private String  mCTtext;
    private String mPROtext;
    private String mVIStext;
    private String  mHEAtext;
    private String  mBONtext;

    private String  notepad;

    private long mPAPtime, mTHYtime, mCELtime, mECGtime, mECHtime, mCTtime, mPROtime, mVIStime, mHEAtime, mBONtime;
    public User(){

    }
    public User(boolean clBox1, boolean clBox2, boolean clBox3, boolean clBox4, String mPAPtext,String mTHYtext,String mCELtext,String mECGtext, String mECHtext,String mCTtext,String mPROtext,String mVIStext,String mHEAtext,String mBONtext,
                long mPAPtime,long  mTHYtime, long mCELtime, long mECGtime, long mECHtime,long  mCTtime,long  mPROtime,long  mVIStime,long  mHEAtime,long  mBONtimem, String notepad,boolean box1,
                boolean box2,
                boolean box3,
                boolean firstTime) {
        this.clBox1 = clBox1;
        this.clBox2 = clBox2;
        this.clBox3 = clBox3;
        this.clBox4 = clBox4;

        this.firstTime = firstTime;

        this.mPAPtext = mPAPtext;
        this.mTHYtext = mTHYtext;
        this.mCELtext = mCELtext;
        this.mECGtext = mECGtext;
        this.mECHtext = mECHtext;
        this.mCTtext = mCTtext;
        this.mPROtext = mPROtext;
        this.mVIStext = mVIStext;
        this.mHEAtext = mHEAtext;
        this.mBONtext = mBONtext;

        this.mPAPtime = mPAPtime;
        this.mTHYtime = mTHYtime;
        this.mCELtime = mCELtime;
        this.mECGtime = mECGtime;
        this.mECHtime = mECHtime;
        this.mCTtime = mCTtime;
        this.mPROtime = mPROtime;
        this.mVIStime = mVIStime;
        this.mHEAtime = mHEAtime;
        this.mBONtime = mBONtime;

        this.notepad = notepad;

        this.box1 = box1;
        this.box2 = box2;
        this.box3 = box3;

    }

    public boolean getClBox1() {return clBox1;}
    public boolean getClBox2() {return clBox2;}
    public boolean getClBox3() {return clBox3;}
    public boolean getClBox4() {return clBox4;}

    public String getmPAPtext() {return mPAPtext;}
    public String getmTHYtext() {return mTHYtext;}
    public String getmCELtext() {return mCELtext;}
    public String getmECGtext() {return mECGtext;}
    public String getmECHtext() {return mECHtext;}
    public String getmCTtext()  {return mCTtext; }
    public String getmPROtext() {return mPROtext;}
    public String getmVIStext() {return mVIStext;}
    public String getmHEAtext() {return mHEAtext;}
    public String getmBONtext() {return mBONtext;}

    public boolean getfirstTime() {return firstTime;}

    public String getnotepad() {return notepad;}

    public long getmPAPtime() {return mPAPtime;}
    public long getmTHYtime() {return mTHYtime;}
    public long getmCELtime() {return mCELtime;}
    public long getmECGtime() {return mECGtime;}
    public long getmECHtime() {return mECHtime;}
    public long getmCTtime()  {return mCTtime; }
    public long getmPROtime() {return mPROtime;}
    public long getmVIStime() {return mVIStime;}
    public long getmHEAtime() {return mHEAtime;}
    public long getmBONtime() {return mBONtime;}

    public boolean getbox1() {
        return box1;
    }

    public boolean getbox2() {
        return box2;
    }

    public boolean getbox3() {
        return box3;
    }


    public void setClBox1(boolean clBox1) {this.clBox1 = clBox1;}
    public void setClBox2(boolean clBox2) {this.clBox2 = clBox2;}
    public void setClBox3(boolean clBox3) {this.clBox3 = clBox3;}
    public void setClBox4(boolean clBox4) {this.clBox4 = clBox4;}


}
