package com.cookandroid.curtain;

import java.util.HashMap;
import java.util.Map;


public class BusEvent {

    int curtain, bright ;
    String led, auto_led, auto_step;


    public BusEvent(int curtain, String led, int bright, String auto_step, String auto_led) {



        //수동제어
        this.curtain = curtain;
        this.led = led;
        this.bright = bright;

        //자동제어
        this.auto_step = auto_step;
        this.auto_led = auto_led;

    }

    public int isFlag() {

        return curtain;

    }
}