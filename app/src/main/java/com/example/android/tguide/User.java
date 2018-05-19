package com.example.android.tguide;

/**
 * Created by Christian on 5/2/2018.
 */

public class User {

    private boolean clBox1;
    private boolean clBox2;
    private boolean clBox3;
    private boolean clBox4;


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
    public User(){

    }
    public User(boolean clBox1, boolean clBox2, boolean clBox3, boolean clBox4, String mPAPtext,String mTHYtext,String mCELtext,String mECGtext, String mECHtext,String mCTtext,String mPROtext,String mVIStext,String mHEAtext,String mBONtext) {
        this.clBox1 = clBox1;
        this.clBox2 = clBox2;
        this.clBox3 = clBox3;
        this.clBox4 = clBox4;

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


    public void setClBox1(boolean clBox1) {this.clBox1 = clBox1;}
    public void setClBox2(boolean clBox2) {this.clBox2 = clBox2;}
    public void setClBox3(boolean clBox3) {this.clBox3 = clBox3;}
    public void setClBox4(boolean clBox4) {this.clBox4 = clBox4;}


}
