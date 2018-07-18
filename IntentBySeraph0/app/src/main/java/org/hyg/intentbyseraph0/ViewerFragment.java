package org.hyg.intentbyseraph0;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

/**
 * Created by shiny on 2018-03-14.
 */

public class ViewerFragment extends Fragment {
    ImageView m_imgView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup vgRoot = (ViewGroup)inflater.inflate(R.layout.frament_viewer, container, false);

        m_imgView = (ImageView)vgRoot.findViewById(R.id.imgView);

        return vgRoot;
    }


    public void setImage(int idx){
        m_imgView.setImageResource(idx);
    }
}
