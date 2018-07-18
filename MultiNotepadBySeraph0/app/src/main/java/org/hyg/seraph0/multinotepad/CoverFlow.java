package org.hyg.seraph0.multinotepad;


import android.content.Context;
import android.graphics.Camera;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Transformation;
import android.widget.Gallery;
import android.widget.ImageView;

/**
 * Created by shiny on 2018-04-05.
 */

public class CoverFlow extends Gallery {

    private static int mMaxRotationAngle = 55;
    public int getMaxRotationAngle() { return mMaxRotationAngle; }
    public void setMaxRotationAngle(int value)  { mMaxRotationAngle = value; }

    private static int mMaxZoom= -60;
    public int getMaxZoom() { return mMaxZoom; }
    public void setMaxZoom(int value) { mMaxZoom = value; }

    private final Camera mCamera = new Camera();

    private int mCenterX;

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

    private void init() {
        this.setStaticTransformationsEnabled(true);
    }

    private int getCenterCoverFlow() {
        return (getWidth() - getPaddingLeft() - getPaddingRight()) / 2 + getPaddingLeft();
    }

    private static int getCenterOfView(View view) {
        return view.getLeft() + view.getWidth() / 2;
    }

    @Override
    protected boolean getChildStaticTransformation(View child, Transformation t) {

        final int childX = getCenterOfView(child);
        final int childWidth = child.getWidth();
        int rotationAngle = 0;

        t.clear();
        t.setTransformationType(Transformation.TYPE_MATRIX);

        if(childX != mCenterX) {
            rotationAngle = (int)(((float)(mCenterX - childX) / childWidth) * mMaxRotationAngle);
            if(Math.abs(rotationAngle) > mMaxRotationAngle) {
                rotationAngle = (rotationAngle < 0) ? -mMaxRotationAngle : mMaxRotationAngle;
            }
        }

        transformImage((ImageView)child, t, rotationAngle);

        return true;
    }

    private void transformImage(ImageView child, Transformation t, int rotationAngle) {
        mCamera.save();
        final Matrix matrix = t.getMatrix();
        final int height = child.getLayoutParams().height;
        final int width = child.getLayoutParams().width;
        final int rotation = Math.abs(rotationAngle);

        mCamera.translate(0.0f, 0.0f, 100.0f);

        if(rotation < mMaxRotationAngle) {
            float zoom = (float)(mMaxZoom + (rotation * 1.5f));
            mCamera.translate(0.0f, 0, zoom);
        }

        mCamera.rotateY(rotationAngle);
        mCamera.getMatrix(matrix);

        matrix.preTranslate(-(width / 2), -(height / 2));
        matrix.postTranslate(width / 2, height / 2);

        mCamera.restore();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mCenterX = getCenterCoverFlow();
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
