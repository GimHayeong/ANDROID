package org.hyg.breakout;

/**
 * Created by shiny on 2018-03-05.
 */

public class Settings {
    // 배경음악, 사운드, 진동상태
    static private boolean m_isMusic = true;
    static public boolean getIsMusic() { return m_isMusic; }
    static public void setIsMusic(boolean val) { m_isMusic = val; }
    static private boolean m_isSound = true;
    static public boolean getIsSound() { return m_isSound; }
    static public void setIsSound(boolean val) { m_isSound = val; }
    static private boolean m_isVib = true;
    static public boolean getIsVib() { return m_isVib; }
    static public void setIsVib(boolean val) { m_isVib = val; }
}
