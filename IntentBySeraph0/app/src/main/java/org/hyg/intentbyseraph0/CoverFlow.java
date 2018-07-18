package org.hyg.intentbyseraph0;

import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Transformation;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by shiny on 2018-03-23.
 * CoverFlow : Camera 클래스를 이용해 바라보는 시점을 변경하여 사진 리스트를 볼때 입체감 있는 에니메이션 구현
 *             중앙에 이미지를 보여주면서 주변의 이미지는 입체감 있게 약간씩 회전시켜 보여주므로,
 *             중앙에 있는 이미지로부터의 거리값을 이용해 회전 각도를 계산한 후 이미지 변환
 *   * [참고] Gallery 클래스는 안드로이드 API 에서 지양하는 클래스이나 Camera 클래스를 이용해 CoverFlow 구현을 위해 사용
 *   - android.hardware.Camera 클래스 : 하드웨어 카메라 모듈 제어. 사진 찍을 때 사용.
 *   - android.graphics.Camera 클래스 : 보는 시각에 따라 달라지는 입체 효과
 */

public class CoverFlow extends Gallery {

    // 최대 회전값
    private int mMaxRotationAngle = 55;

    public CoverFlow(Context context) {
        super(context);

        init();
    }

    public CoverFlow(Context context, AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    public CoverFlow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    public CoverFlow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);

        init();
    }

    public int getMaxRotationAngle() { return mMaxRotationAngle; }
    public void setMaxRotationAngle(int angle) { mMaxRotationAngle = angle; }
    // 최대 확대값
    private int mMaxZoom = -60;
    public int getMaxZoom() { return mMaxZoom;}
    public void setMaxZoom(int zoom) { mMaxZoom = zoom; }

    private Camera mCamera = new Camera();
    private int mCenterPoint;




    /**
     *
     */
    private void init() {
        // getChildStaticTransformation() 에서 ViewGroup 에 들어있는 뷰들의 변환을 위해 재정의하기 위해 호출
        this.setStaticTransformationsEnabled(true);
    }

    /**
     * 이미지를 좌우 스크롤할 때, 각각의 이미지뷰를 입체감 있게 보여주도록 변환하는 작업
     *  - [선작업] setStaticTransformationsEnabled() 메서드를 호출해야 함.
     * @param child
     * @param t
     * @return
     */
    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {
        final int cpChild = getCenterHorizontal(child);
        final int width = child.getWidth();
        int angle = 0;

        t.clear();

        t.setTransformationType(Transformation.TYPE_MATRIX);

        //:: Log.d("ChildStaticTransform", cpChild + " : " + mCenterPoint);
        //** 중앙의 이미지가 아니면 중앙의 이미지로부터의 거리값을 이용해 회전각도 계산해 이미지 변환
        if(cpChild != mCenterPoint){
            angle = (int)(((float)(mCenterPoint - cpChild) / width) * mMaxRotationAngle);

            if(Math.abs(angle) > mMaxRotationAngle) {
                angle = (angle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
            }
        }

        transformImageBitmap((ImageView)child, t, angle);

        return true;
    }

    /**
     * 이미지 뷰의 중앙 좌표
     * @param view
     * @return
     */
    private int getCenterHorizontal(View view) {
        return (view.getWidth() + view.getPaddingLeft()) / 2 + view.getLeft();
    }

    /**
     * 뷰가 화면에 보이기 전에 화면 중앙 좌표 확인
     * @return
     */
    private int getCenterHorizontal() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft() ;
    }


    /**
     *  각각의 이미지뷰 변환
     *   : 스택에 Matrix 객체를 보관하고 차례대로 Matrix 객체를 적용시켜 이미지 변환 처리.
     *     - save() 메서드를 이용해 메트릭스 적용. Matrix 객체를 적용하기 위해 스택에 저장.
     *     - translate() 메서드로 전달되는 고정된 Z 파라미터를 이용해 대상 이미지를 확대/축소
     *     - 카메라 객체의 getMatrix() 메서드로 카메라 객체의 Matrix 객체 참조
     *     - restore() 메서드를 이용해 메트릭스 적용 해제. Matrix 객체 적용을 취소하기 위헤 스택에서 제거.
     * @param child : ViewGroup 의 각각의 이미지뷰
     * @param format
     * @param angle
     */
    private void transformImageBitmap(ImageView child, Transformation format, int angle){
        // 메트릭스 적용
        mCamera.save();

        final Matrix matrix = format.getMatrix();
        final int height = child.getLayoutParams().height;
        final int width = child.getLayoutParams().width;
        final int rotation = Math.abs(angle);
        // 높이값으로 입체감을 주는데 사용됨
        final float fixedZ = 100.f;

        // 이미지 확대 (최대 회전값보다 작을 경우 확대값 계산)
        if(angle < mMaxRotationAngle) {
            float zoom = (float)(mMaxZoom + (angle * 1.5));
            mCamera.translate(0.0f, 0.0f, zoom);
        } else {
            mCamera.translate(0.0f, 0.0f, fixedZ);
        }

        // 이미지 회전
        mCamera.rotateY(angle);

        // 카메라 객체의 Matrix 객체 참조 얻기
        mCamera.getMatrix(matrix);

        // 이미지 이동 (Matrix 객체로 이미지 사이의 거리 조정)
        matrix.preTranslate(-(width / 2), -(height / 2));
        matrix.postTranslate(width / 2, height / 2);

        // 메트릭스 적용 해제
        mCamera.restore();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterPoint = getCenterHorizontal();
        super.onSizeChanged(w, h, oldw, oldh);
    }


}



