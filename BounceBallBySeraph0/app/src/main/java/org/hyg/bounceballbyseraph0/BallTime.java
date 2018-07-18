package org.hyg.bounceballbyseraph0;

/**
 * Created by shiny on 2018-02-22.
 */

public class BallTime {
    static public float SmDeltaTime;
    static private long SmCurrentTime = System.nanoTime();

    static public void updateTime(){
        SmDeltaTime = (System.nanoTime() - SmCurrentTime) / 1000000000f;
        SmCurrentTime = System.nanoTime();
    }
}
