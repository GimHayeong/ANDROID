package org.hyg.intentbyseraph0;

import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by shiny on 2018-03-24.
 * 버튼의 중복클릭 방지
 *  : 0.6초 이내의 짧은 시간간격으로 클릭하는 사용자의 입력 제한
 */

public abstract class OnSingleClickListener implements View.OnClickListener{
    /**
     * 실제 onClick 이벤트에서 처리할 작업들
     * @param view
     */
    public abstract void onSingleClick(View view);

    /**
     * 연속 클릭시 클릭으로 인정할 최소 시간 간격 : 0.6초 이내이면 클릭 인정 안됨
     */
    private static final long MIN_CLICK_INTERVAL = 600;
    /**
     * 마지막에 클릭한 시간
     */
    private long mLastClickedTime;


    @Override
    public void onClick(View view) {

        final long curtClickedTime = SystemClock.uptimeMillis();
        final boolean isDoubleClick = (curtClickedTime - mLastClickedTime <= MIN_CLICK_INTERVAL);
        mLastClickedTime = curtClickedTime;

        if(isDoubleClick) {  return; }

        onSingleClick(view);
    }
}

