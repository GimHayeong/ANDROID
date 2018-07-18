package org.hyg.intentbyseraph0;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.ViewSwitcher;

import java.util.ArrayList;

public class AnimationMainActivity extends AppCompatActivity {

    private Handler mHandler = new Handler();
    private ImageView mImgCurt;
    private Button mBtnStartAnim, mBtnStopAnim;
    private ArrayList<Drawable> mImgList = new ArrayList<Drawable>();

    private Thread mThread;
    private static ImageSwitcher mSwitcher;
    private boolean mIsRunning = true;
    private ImageView mImg;

    private ImageView mImgSwing, mImgSky, mImgWaterDrop;
    private Animation mAniShake, mAniFlow, mAniDrop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation_main);

        //initAutoAnim();
        ((FrameLayout)findViewById(R.id.frmAnim)).setVisibility(View.INVISIBLE);

        //initSwitcher();
        ((FrameLayout)findViewById(R.id.frmSwitcher)).setVisibility(View.INVISIBLE);

        //initZoomAnim();
        ((FrameLayout)findViewById(R.id.frmMotion)).setVisibility(View.INVISIBLE);

        initSwing();
    }

    private void initSwing() {
        mImgSwing = (ImageView)findViewById(R.id.imgAndroid);
        mImgSky = (ImageView)findViewById(R.id.imgSky);
        mImgWaterDrop = (ImageView)findViewById(R.id.imgWaterDrop);

        mAniShake = AnimationUtils.loadAnimation(this, R.anim.rotate_shake);
        mImgSwing.setAnimation(mAniShake);

        mAniFlow = AnimationUtils.loadAnimation(this, R.anim.trans_move_left);
        mImgSky.setAnimation(mAniFlow);

        mAniDrop = AnimationUtils.loadAnimation(this, R.anim.trans_drop);
        mImgWaterDrop.setAnimation(mAniDrop);

        Resources res = getResources();
        Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.sky);
        ViewGroup.LayoutParams params = mImgSky.getLayoutParams();
        params.width = bmp.getWidth();
        params.height = bmp.getHeight();

        mImgSky.setImageBitmap(bmp);
        mAniFlow.setAnimationListener(new AnimationAdapter());
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if(hasFocus){
            mAniShake.start();
            mAniDrop.start();
            mAniFlow.start();
        } else {
            mAniShake.reset();
            mAniDrop.reset();
            mAniFlow.reset();
        }
    }

    private void initZoomAnim() {
        mImg = (ImageView)findViewById(R.id.img);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            private Animation m_Anim;
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnScale1:
                    case R.id.btnScale2:
                        m_Anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.scale_zoom_in);
                        view.startAnimation(m_Anim);
                        break;

                    case R.id.btnMoveLT:
                        m_Anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.trans_move_left);
                        mImg.startAnimation(m_Anim);
                        break;

                    case R.id.btnRotateCW:
                        m_Anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_cw);
                        mImg.startAnimation(m_Anim);
                        break;

                    case R.id.btnFadeIn:
                        m_Anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.alpha_fade_in);
                        mImg.startAnimation(m_Anim);
                        break;

                    default:
                        break;
                }
            }
        };
        ((Button)findViewById(R.id.btnScale1)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnScale2)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnMoveLT)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnRotateCW)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnFadeIn)).setOnClickListener(OnButtonClick);
    }

    private void initSwitcher() {
        mSwitcher = (ImageSwitcher)findViewById(R.id.imgSwitcher);
        mBtnStartAnim = (Button)findViewById(R.id.btnStartAnim);
        mBtnStopAnim = (Button)findViewById(R.id.btnStopAnim);
        mBtnStartAnim.setVisibility(View.VISIBLE);
        mBtnStopAnim.setVisibility(View.VISIBLE);

        View.OnClickListener OnButtonClick = new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnStartAnim:
                        startAnimationForSwitcher();
                        break;

                    case R.id.btnStopAnim:
                        stopAnimationForSwitcher();
                        break;
                }
            }
        };
        mBtnStartAnim.setOnClickListener(OnButtonClick);
        mBtnStopAnim.setOnClickListener(OnButtonClick);


        mSwitcher.setFactory(new ViewSwitcher.ViewFactory(){
            final ImageSwitcher.LayoutParams m_Params = new ImageSwitcher.LayoutParams(ImageSwitcher.LayoutParams.MATCH_PARENT
                                                                                     , ImageSwitcher.LayoutParams.MATCH_PARENT);
            final ImageView m_ImgView = new ImageView(getApplicationContext());

            @Override
            public View makeView() {
                ImageView m_ImgView = new ImageView(getApplicationContext());
                m_ImgView.setBackgroundColor(0xFF000000);
                m_ImgView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                m_ImgView.setLayoutParams(m_Params);

                return m_ImgView;
            }
        });
    }

    private void startAnimationForSwitcher(){
        setStartButton(false);

        mSwitcher.setVisibility(View.VISIBLE);

        mIsRunning = true;
        mThread = new SwitcherThread();
        mThread.start();
    }

    private void stopAnimationForSwitcher(){
        setStartButton(true);

        mIsRunning = false;
        try{
            mThread.join();
        } catch (InterruptedException e) { /*...*/ }

        mSwitcher.setVisibility(View.INVISIBLE);
    }

    private void setStartButton(boolean isEnable){
        mBtnStartAnim.setEnabled(isEnable);
        mBtnStartAnim.setClickable(isEnable);
        mBtnStopAnim.setEnabled(!isEnable);
        mBtnStopAnim.setClickable(!isEnable);
    }

    private void startAutoAnimation() {
        AnimThread thread = new AnimThread();
        thread.start();
    }

    private void initAutoAnim() {
        //((FrameLayout)findViewById(R.id.frmAnim)).setForegroundGravity(1);
        mImgCurt = (ImageView)findViewById(R.id.imgCurt);

        Resources res = getResources();
        mImgList.add(res.getDrawable(R.drawable.emo_im_laughing));
        mImgList.add(res.getDrawable(R.drawable.emo_im_angry));
        mImgList.add(res.getDrawable(R.drawable.emo_im_happy));
        mImgList.add(res.getDrawable(R.drawable.emo_im_sad));
        mImgList.add(res.getDrawable(R.drawable.emo_im_surprised));

        startAutoAnimation();;
    }


    class AnimThread extends Thread {
        Drawable drawable;
        @Override
        public void run() {
            for(int i=0; i<100; i++){
                drawable = mImgList.get(i % 4);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mImgCurt.setImageDrawable(drawable);
                    }
                });

                try{
                    sleep(500);
                } catch (InterruptedException e) { /*...*/ }
            }
        }
    }


    class SwitcherThread extends Thread {
        private final int mDuration = 250;
        private final int[] mImgRes = { R.drawable.emo_im_laughing, R.drawable.emo_im_angry
                                        , R.drawable.emo_im_happy, R.drawable.emo_im_sad
                                        , R.drawable.emo_im_surprised };
        private int mCurtIdx = 0;

        @Override
        public void run() {
            while(mIsRunning){
                synchronized (this){
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            mSwitcher.setImageResource(mImgRes[mCurtIdx]);
                            Log.d("SwitcherThread: ", mIsRunning ? "RUN " + mCurtIdx : "STOP");
                        }
                    });

                    if(++mCurtIdx == mImgRes.length) {
                        mCurtIdx = 0;
                    }

                    try{
                        sleep(mDuration);
                    } catch (InterruptedException e) { /*...*/ }
                }
            }
        }
    }

    private class AnimationAdapter implements Animation.AnimationListener {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
