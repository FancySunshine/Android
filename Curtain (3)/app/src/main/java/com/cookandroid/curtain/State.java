package com.cookandroid.curtain;

import android.app.Application;

public class State extends Application {
    private int step;

    public int getStep(){
        return step;
    }

    public void setStep(int i){
        this.step = i;
    }

}
