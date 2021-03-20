package com.cookandroid.curtain;

public class BusEvent {

    int curtain, bright, auto_step;
    String led, auto_led;

    public BusEvent(int curtain, String led, int bright) {

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