package org.hyg.breakout;

/**
 * Created by shiny on 2018-02-22.
 */

public class DTime {
    static public float DeltaTime;
    static private long m_currentTime = System.nanoTime();

    static public void updateTime(){
        DeltaTime = (System.nanoTime() - m_currentTime) / 1000000000f;
        m_currentTime = System.nanoTime();
    }
}
