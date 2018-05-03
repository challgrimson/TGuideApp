package com.example.android.tguide;

/**
 * Created by Christian on 5/2/2018.
 */

public class User {

    private boolean clBox1;
    private boolean clBox2;
    private boolean clBox3;
    private boolean clBox4;

    public User(){

    }
    public User(boolean clBox1, boolean clBox2, boolean clBox3, boolean clBox4) {
        this.clBox1 = clBox1;
        this.clBox2 = clBox2;
        this.clBox3 = clBox3;
        this.clBox4 = clBox4;
    }

    public boolean getClBox1() {return clBox1;}
    public boolean getClBox2() {return clBox2;}
    public boolean getClBox3() {return clBox3;}
    public boolean getClBox4() {return clBox4;}

    public void setClBox1(boolean clBox1) {this.clBox1 = clBox1;}
    public void setClBox2(boolean clBox2) {this.clBox2 = clBox2;}
    public void setClBox3(boolean clBox3) {this.clBox3 = clBox3;}
    public void setClBox4(boolean clBox4) {this.clBox4 = clBox4;}


}
