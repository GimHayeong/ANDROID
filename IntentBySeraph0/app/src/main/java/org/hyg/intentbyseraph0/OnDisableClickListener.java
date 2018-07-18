package org.hyg.intentbyseraph0;

import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by shiny on 2018-03-24.
 * 버튼 비활성화상태에서 클릭이벤트 제한
 *  : 비활성화상태에서 활성화상태로 돌아오기 전 시간에 가까울 때 클릭하면 이벤트 받음.
 *    (네트워킹 처리시간 때문인 것 같음)
 *    (해결) => AsyncTask 상속받아 구현하면 해결됨
 */

public abstract class OnDisableClickListener implements View.OnClickListener {
    /**
     * 실제 onClick 이벤트에서 처리할 작업들
     * @param view
     */
    public abstract void onDisableClick(View view);

    /**
     * 클릭 이벤트 처리중인지 여부
     *  : 클릭 이벤트 처리중이거나 버튼이 비활성화상태에서 클릭이벤트 받으면 이벤트처링 없이 리턴.
     */
    private boolean mIsClicked = false;

    @Override
    public void onClick(View view) {
        Button btnView = (Button)view;

        final boolean isClicked = !mIsClicked;
        mIsClicked = isClicked;
        if(!isClicked || !btnView.isEnabled()) { return; }

        btnView.setEnabled(false);
        btnView.setClickable(false);

        onDisableClick(view);
    }
}
