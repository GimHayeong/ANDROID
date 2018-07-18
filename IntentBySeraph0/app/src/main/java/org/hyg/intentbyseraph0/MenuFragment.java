package org.hyg.intentbyseraph0;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by shiny on 2018-03-14.
 *  이전버전 지원: android.support.v4.app.Fragment
 */

public class MenuFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vgRoot = (ViewGroup)inflater.inflate(R.layout.fragment_menu, container, false);

        Button btnMain = (Button)vgRoot.findViewById(R.id.btnMain);
        btnMain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                FragmentMainActivity activity = (FragmentMainActivity)getActivity();
                activity.onFragmentChanged(1);
            }
        });

        return vgRoot;
    }
}
