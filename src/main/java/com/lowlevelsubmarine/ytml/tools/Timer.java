package com.lowlevelsubmarine.ytml.tools;

public class Timer {

    private long startMs;
    private long stopMs;

    public Timer() {
        start();
    }

    public Timer start() {
        this.startMs = System.currentTimeMillis();
        return this;
    }

    public Timer stop() {
        this.stopMs = System.currentTimeMillis();
        return this;
    }

    public void printTime(String name) {
        long ms = this.stopMs - this.startMs;
        System.out.println("Timer: " + name + " [" + ms + "ms]");
    }

}
