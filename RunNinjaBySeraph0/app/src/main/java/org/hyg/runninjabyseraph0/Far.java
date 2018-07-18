package org.hyg.runninjabyseraph0;

import android.content.Context;

/**
 * Created by shiny on 2018-03-05.
 */

public class Far extends Field {

    public Far(Context context, int width, int height){
        //super(context, width, height, 50, R.drawable.far0, (int)(height * 0.6f), 0);
        super(context, width, height, 50, R.drawable.far0, (int)height / 2, height * 0.14f);
    }
}
