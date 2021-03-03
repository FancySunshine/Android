package com.cookandroid.curtain;

import android.app.Application;

public class State extends Application {
    private int step, bright = 0;
    private String led = "#000000";

    public int getStep(){
        return step;
    }

    public void setStep(int i){
        this.step = i;
    }

    public String getLed(){return led;}

    public void setLed(String led){ this.led = led;}

    public int getBright(){return bright;}

    public void setBright(int bright){this.bright = bright;}

}
