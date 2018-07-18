package org.hyg.seraph0.media.capture.intent.Utils;

import android.content.Context;

/**
 * Created by shiny on 2018-04-04.
 */

public class File {
    public File createFile(Context context, String filename){
        File file;// = new File(context.getFilesDir(), filename);
        file = new File();

        return file;
    }
}
