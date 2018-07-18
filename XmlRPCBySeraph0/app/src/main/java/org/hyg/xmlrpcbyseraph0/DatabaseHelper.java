package org.hyg.xmlrpcbyseraph0;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by shiny on 2018-03-29.
 * 데이터베이스 생성 및 열기 헬퍼 클래스
 *  - 헬퍼 객체가 만든 후, 읽기/쓰기 메서드를 호출해야 데이터베이스 파일 생성 가능
 *    (getReadableDatabase() 나 getWritableDatabase())
 *  - 데이터베이스 생성 및 변경 시 콜백 메서드 호출됨
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private final String TAG = "DBHelper";
    private String DB_NAME;
    private int DB_VER;
    private String TBL_NAME;
    public void setTableName(String tblName){ TBL_NAME = tblName; }
    private boolean mIsOpenedDB = false;
    private void setIsOpenedDataBase(boolean isOpened){ mIsOpenedDB = isOpened; }


    /**
     * 헬퍼 객체 생성자
     * @param context
     * @param dbName : Database name
     * @param factory : 조회시 리턴하는 커서를 만들 CursorFactory 객체 (nullable)
     * @param dbVersion : 데이터베이스의 스키마나 데이터를 바꾸려면 기존의 버전 정보와 다르게 Database version 정보 지정
     */
    public DatabaseHelper(Context context, String dbName, SQLiteDatabase.CursorFactory factory, int dbVersion) {
        super(context, dbName, factory, dbVersion);
        DB_NAME = dbName;
        DB_VER = dbVersion;
    }

    /**
     * 데이터베이스 파일 최초 생성시 호출되는 콜백 메서드
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "DROP TABLE IF EXISTS " + TBL_NAME;
        try {
            db.execSQL(query);
        } catch (Exception ex) { Log.e(TAG, "Exception DROP: " + ex); }

        query = "CREATE TABLE " + TBL_NAME + "("
                + " _id integer PRIMARY KEY autoincrement"
                + ", name text"
                + ", age Integer"
                + ", phone text"
                + ");";
        try{
            db.execSQL(query);
        } catch (Exception ex) { Log.e(TAG, "Exception CREATE: " + ex); }
    }

    /**
     * 데이터베이스 오픈시 호출되는 콜백 메서드
     * @param db
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        Log.d(TAG, "Opened database");
    }

    /**
     * 데이터베이스 변경시(버전 차이) 호출되는 콜백 메서드
     * @param db
     * @param oldVersion : 기존 버전 정보
     * @param newVersion : 현재 버전 정보
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
