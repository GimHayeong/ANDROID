package org.hyg.runninjabyseraph0;

import android.content.Context;

/**
 * Created by shiny on 2018-03-05.
 */

public class Near extends Field {

    public Near(Context context, int width, int height){
        //super(context, width, height, 220, R.drawable.near0, height / 2, height / 2);
        super(context, width, height, 220, R.drawable.near0, (int)(height * 0.4f), height * 0.6f);
    }
}
