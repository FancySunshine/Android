package com.cookandroid.curtain;

import java.util.HashMap;
import java.util.Map;


public class BusEvent {

    int curtain = -1, bright = -1;
    String led, auto_led, auto_step;
    String lux;
    boolean flag = false;


    public BusEvent(int curtain, String led, int bright, String auto_step, String auto_led) {

        //수동제어
        this.curtain = curtain;
        this.led = led;
        this.bright = bright;

        //자동제어
        this.auto_step = auto_step;
        this.auto_led = auto_led;

        this.flag = true;

    }

    public BusEvent(String lux){
        this.lux = lux;
        this.flag = false;
    }

}