package me.mrredness.infection;

import static java.lang.Thread.sleep;

public class SleepUtils {
    public static void wait(int milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void one() {
        try {
            sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void oneHalf() {
        try {
            sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void oneQuarter() {
        try {
            sleep(250);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void three() {
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void five() {
        try {
            sleep(5000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
    public static void ten() {
        try {
            sleep(10000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }
}
