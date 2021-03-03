package com.cookandroid.curtain;

public class BusEvent {

    int curtain, bright;
    String led;

    public BusEvent(int curtain, String led, int bright) {

        this.curtain = curtain;
        this.led = led;
        this.bright = bright;

    }

    public int isFlag() {

        return curtain;

    }
}