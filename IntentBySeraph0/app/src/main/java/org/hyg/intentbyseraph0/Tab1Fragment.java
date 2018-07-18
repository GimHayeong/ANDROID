package org.hyg.intentbyseraph0;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by shiny on 2018-03-14.
 */

public class Tab1Fragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        ViewGroup vgRoot = (ViewGroup)inflater.inflate(R.layout.fragment_tab1, container, false);

        return vgRoot;
    }
}
