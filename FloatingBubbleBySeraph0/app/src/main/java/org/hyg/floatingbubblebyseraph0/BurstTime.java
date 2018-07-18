package org.hyg.floatingbubblebyseraph0;

/**
 * Created by shiny on 2018-02-21.
 */

public class BurstTime {
    static private long m_currentTime = System.nanoTime();
    static public float deltaTime;

    static public void updateTime(){
        deltaTime = (System.nanoTime() - m_currentTime) / 1000000000f;
        m_currentTime = System.nanoTime();
    }
}
