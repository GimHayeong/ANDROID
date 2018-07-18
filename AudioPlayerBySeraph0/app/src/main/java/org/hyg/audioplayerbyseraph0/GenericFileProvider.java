package org.hyg.audioplayerbyseraph0;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by shiny on 2018-04-03.
 * targetVersion 24이상에서 file:// 을 content:// 로 바꿔주기 위한 클래스
 * (기존의 FileProvider 와 특정 파일이나 폴더에 액세스 권한을 부여하는 FileProvider 의 충돌 회피 목적)
 *
 */

public class GenericFileProvider extends FileProvider{

    private final String PROVIDER_NAME = ".fileprovider";

    public Uri getPhotoUri(Context context, File file) {
        Uri uri;

        //23 버전 이하
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            uri = Uri.fromFile(file);
        } else {
            uri = FileProvider.getUriForFile(context
                                        , context.getApplicationContext().getPackageName() + PROVIDER_NAME
                                        , file);
        }
        return uri;
    }

    public Uri getUriForFile(String provider, File file, String type) {
        return Uri.parse(file.getPath());
    }


    /**
     * onCreate() 에서 호출해야 함
     * 개발 단계에서 필요
     */
    private void ableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectAll().penaltyLog().penaltyDeath().build());

        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll()
                .penaltyLog().penaltyDeath().build());
    }

    private void disableStrictMode() {
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().permitAll().build());
    }

}
