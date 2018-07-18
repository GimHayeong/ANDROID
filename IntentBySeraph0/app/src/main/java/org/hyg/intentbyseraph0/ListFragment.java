package org.hyg.intentbyseraph0;

import android.content.Context;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Created by shiny on 2018-03-14.
 */

public class ListFragment extends Fragment {

    String[] m_strTitles = { "첫번째 이미지", "두번째 이미지", "세번째 이미지" };
    public ImageSelectionCallback CB_IMGSELECT;

    public static interface ImageSelectionCallback {
        public void onImageSelected(int position);
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if(context instanceof ImageSelectionCallback){
            CB_IMGSELECT = (ImageSelectionCallback) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup vgRoot = (ViewGroup)inflater.inflate(R.layout.fragment_list, container, false);
        ListView lstImage = (ListView)vgRoot.findViewById(R.id.lstImage);
        ArrayAdapter<String> adt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, m_strTitles);
        lstImage.setAdapter(adt);

        lstImage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(CB_IMGSELECT != null){
                    CB_IMGSELECT.onImageSelected(i);
                }
            }
        });

        return vgRoot;
    }
}
