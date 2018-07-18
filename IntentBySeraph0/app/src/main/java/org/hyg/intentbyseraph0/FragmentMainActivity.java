package org.hyg.intentbyseraph0;

import android.app.ActionBar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class FragmentMainActivity extends AppCompatActivity implements ListFragment.ImageSelectionCallback {

    //MainFragment m_frmMain;
    //MenuFragment m_frmMenu;

    ListFragment m_frmList;
    ViewerFragment m_frmView;
    // 이미지 리소스 아이디 배열
    int[] m_nImages = {R.drawable.img_1, R.drawable.img_2, R.drawable.img_3 };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);

        //initMenuFragment();

        initImageViewFragment();

        initOptionMenu();
    }

    private void initOptionMenu() {
        ActionBar actBar = getActionBar();
        if(actBar != null) {
            //actBar.show();
            //actBar.hide();
            actBar.setLogo(R.drawable.img_1);
            actBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_HOME | ActionBar.DISPLAY_USE_LOGO);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        setSearchLayoutXml(menu);

        return true;
    }

    private void setSearchLayoutXml(Menu menu) {
        View view = menu.findItem(R.id.mnuSearch).getActionView();
        if(view != null){
            EditText edtSearchWord = (EditText)view.findViewById(R.id.edtSearchWord);
            if(edtSearchWord != null){
                TextView.OnEditorActionListener onSearchListener = new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                        return false;
                    }
                };
                edtSearchWord.setOnEditorActionListener(onSearchListener);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int mnuId = item.getItemId();
        switch (mnuId){
            case R.id.mnuRefresh:
                Toast.makeText(this, "새로고침...", Toast.LENGTH_LONG).show();
                break;

            case R.id.mnuSearch:
                Toast.makeText(this, " 검색...", Toast.LENGTH_LONG).show();
                break;

            case R.id.mnuSettings:
                Toast.makeText(this, "설정...", Toast.LENGTH_LONG).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initImageViewFragment() {
        FragmentManager frmMgr = getSupportFragmentManager();
        m_frmList = (ListFragment)frmMgr.findFragmentById(R.id.frmList);
        m_frmView = (ViewerFragment)frmMgr.findFragmentById(R.id.frmView);
    }



    private void initMenuFragment() {
        // Activity 에 프레그먼트ID 등록됨
        //m_frmMain = (MainFragment)getSupportFragmentManager().findFragmentById(R.id.frmMain);
        // Activity 에 프레그먼트ID 등록되지 않음.
        //m_frmMenu = new MenuFragment();
    }


    /**
     * 이전버전 호환: getSupportFragmentManager()
     * @param idx
     */
    public void onFragmentChanged(int idx){
        switch(idx){
            case 0:
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, m_frmMenu).commit();
                break;

            case 1:
                //getSupportFragmentManager().beginTransaction().replace(R.id.container, m_frmMain).commit();
                break;
        }
    }

    /**
     * ListFragment.ImageSelectionCallback 인터페이스 구현
     * @param position
     */
    @Override
    public void onImageSelected(int position) {
        m_frmView.setImage(m_nImages[position]);
    }
}
