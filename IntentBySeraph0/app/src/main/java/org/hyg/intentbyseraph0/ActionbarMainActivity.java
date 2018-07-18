package org.hyg.intentbyseraph0;

import android.annotation.SuppressLint;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

public class ActionbarMainActivity extends AppCompatActivity {

    Toolbar tbar;
    Tab1Fragment frmTab1;
    Tab2Fragment frmTab2;
    Tab3Fragment frmTab3;
    String[] mTabText = { "통화기록", "스팸기록", "연락처" };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actionbar_main);

        initToolbar();
    }

    private void initToolbar() {
        tbar = (Toolbar)findViewById(R.id.tbarActTool);
        setSupportActionBar(tbar);

        ActionBar abar = getSupportActionBar();
        abar.setDisplayShowHomeEnabled(false);

        frmTab1 = new Tab1Fragment();
        frmTab2 = new Tab2Fragment();
        frmTab3 = new Tab3Fragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frmContainer, frmTab1)
                .commit();

        TabLayout tabs = (TabLayout)findViewById(R.id.tabActTabs);
        for(int i=0; i<mTabText.length; i++){
            tabs.addTab(tabs.newTab().setText(mTabText[i]));
        }

        /**[TabLayout.setOnTabSelectedListener 사용지양. => TabLayout.addOnTabSelectedListener 사용권장.]
        //useSetOnTabSelectedListener(tabs);**/

        useAddOnTabSelectedListener(tabs);
    }

    /**
     * [TabLayout.setOnTabSelectedListener 사용지양. => TabLayout.addOnTabSelectedListener 사용권장.]
     * TabLayout.setOnTabSelectedListener
     *   => tabs.addOnTabSelectedListener 와 ViewPager, ViewPagerAdaper 를 이용해 변경 가능
     *      [1] xml 의 <TabLayout /> 아래에 <~v4.view.ViewPager /> 추가
     *          (ViewPager의 아이디 : viewpager)
     *      [2] FragmentStatePagerAdaper 을 상속받는 ViewPagerAdapter 클래스 생성 및 구현
     *          2-1. 탭 갯수 저장 변수 선언
     *               (private int m_countOfTabs;)
     *          2-2. 생성자(FragmentManager fm, int countOfTabs) {
     *               super(fm);
     *               m_countOfTabs = countOfTabs;
     *          }
     *          2-3. getCount() 메서드 재정의
     *          2-4. getItem(int postion) 메서드 재정의
     *     [3] MainActivity 에서 ViewPager와 ViewPagerAdapter 연결
     *         3-1. ViewPager 객체 생성
     *         3-2. ViewPagerAdapter 객체 생성
     *         3-3. ViewPager와 ViewPagerAdapter 연결
     *         3-4. TabLayout에 addOnTabSelectedListener 를 설정
     *              (ViewPagerOnTabSelectedListener(ViewPager) 를 생성자로 전달)
     *         3-5. ViewPager에 addOnPageChangeListener 를 설정
     *              (TabLayoutOnPageChangeListener(TabLayout) 을 생성자로 전달)
     * @param tabs
     */
    private void useAddOnTabSelectedListener(TabLayout tabs) {
        final android.support.v4.view.ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        final ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), tabs.getTabCount());
        viewPager.setAdapter(viewPagerAdapter);

        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
    }







    /**
     * [TabLayout.setOnTabSelectedListener 사용지양. => TabLayout.addOnTabSelectedListener 사용권장.]
     * @param tabs
     */
    private void useSetOnTabSelectedListener(TabLayout tabs){
        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener(){

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();

                Fragment frmSelected = null;

                switch(position){
                    case 0:
                        frmSelected = frmTab1;
                        break;

                    case 1:
                        frmSelected = frmTab2;
                        break;

                    case 2:
                        frmSelected = frmTab3;
                        break;
                }

                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frmContainer, frmSelected)
                        .commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
