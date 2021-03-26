package com.cookandroid.curtain;

import android.app.Application;

public class State extends Application {
    private int step, bright = 0;
    private String led, auto_led, auto_step = "#000000";

    //수동 제어 "단계"
    public int getStep(){
        return step;
    }
    public void setStep(int step){ this.step = step; }

    //수동제어 "led 색상"
    public String getLed(){return led;}
    public void setLed(String led){ this.led = led;}

    //수동제어 "led 밝기"
    public int getBright(){return bright;}
    public void setBright(int bright){this.bright = bright;}

    //자동제어 "단계"
    public String getAuto_Step() { return auto_step; }
    public void setAuto_Step(String auto_step) { this.auto_step = auto_step; }

    //자동제어 "led 생상"
    public String getAuto_Led() { return auto_led;}
    public void setAuto_Led(String auto_led) { this.auto_led = auto_led;}



}
