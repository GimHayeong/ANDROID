package org.hyg.seraph0.multinotepad.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import org.hyg.seraph0.multinotepad.BasicInfo;
import org.hyg.seraph0.multinotepad.MainActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by shiny on 2018-04-05.
 */

public class MemoDatabase {
    public static final String TAG = "MemoDatabase";
    /**
     * Table name
     */
    public final static String TABLE_MEMO = "MEMO";
    public final static String TABLE_PHOTO = "PHOTO";
    public final static String TABLE_VIDEO = "VIDEO";
    public final static String TABLE_VOICE = "VOICE";
    public final static String TABLE_HANDWRITING = "HANDWRITING";


    private Context mContext;


    public static int DATABASE_VERSION = 1;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Singleton
     */
    private static MemoDatabase database;
    public static MemoDatabase getInstance(Context context) {
        if(database == null) {
            database = new MemoDatabase(context);
        }

        return database;
    }
    /**
     * private constructor
     * @param context
     */
    private MemoDatabase(Context context){
        mContext = context;
    }

    /**
     * open database
     * @return isOpened
     */
    public boolean open() {
        mDbHelper = new DatabaseHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();

        return true;
    }

    /*
       * 데이터 조회
       *
       *  *** READ ***
       *
       *  String query = "SELECT ID_PHOTO, ID_VIDEO, ID_VOICE, ID_HANDWRITING, INPUT_DATE, CONTENT_TEXT "
       *                 " FROM " + MemoDatabase.TABLE_MEMO
       *          + " WHERE _id = " + mMemoId;
       *  if(MainActivity.mDatabase != null) {
       *     Cursor cursor = MainActivity.mDatabase.rawQuery(query.toString());
       *     if (cursor.moveToNext()) {
       *         int photoId = cursor.getInt(0);
       *         int videoId = cursor.getInt(1);
       *         int voiceId = cursor.getInt(2);
       *         int handwritingId = cursor.getInt(3);
       *         String memoDate = cursor.getString(4);
       *         String memoText = cursor.getString(5);
       *     }
       *     cursor.close();
       *  }
       *
       *
       *
       *
       *  StringBuilder query = new StringBuilder();
       *  query.append("SELECT _id FROM ");
       *  query.append(tMemoDatabase.TABLE_PHOTO);
       *  query.append(" WHERE URI = '");
       *  query.append(newUri);
       *  query.append("'");
       *  if(MainActivity.mDatabase != null) {
       *       Cursor cursor = MainActivity.mDatabase.rawQuery(query.toString());
       *       if (cursor.moveToNext()) {
       *                  newId = cursor.getInt(0);
       *       }
       *       cursor.close();
       *  }
       *  query.setLength(0);
       *  query.trimToSize();
       *
       *
       *
       *
       *  String query = "SELECT URI FROM " + table + " WHERE _id = " + id + "";
       *  Cursor cursor = mDatabase.rawQuery(query);
       *  if(cursor.moveToNext()) {
       *     uri = cursor.getString(0);
       *  }
       *  cursor.close();
       *
       *
     */
    public Cursor rawQuery(String query) {
        Cursor cursor = null;
        try {
            cursor = mDb.rawQuery(query, null);
        } catch (Exception ex) { Log.e(TAG, "Exception in execute query.", ex); }

        return cursor;
    }

    /*
       * 데이터 생성 / 수정 / 삭제
       *
       * *** CREATE ********************************************
       *  String query = "INSERT INTO " + MemoDatabase.TABLE_MEMO
       *              + " (ID_PHOTO, ID_VIDEO, ID_VOICE, ID_HANDWRITING, INPUT_DATE, CONTENT_TEXT) "
       *              + " VALUES ("
       *              + "  " + photoId
       *              + ", " + videoId
       *              + ", " + voiceId
       *              + ", " + handwritingId
       *              + ",    DATETIME('" + mMemoDate + "') "
       *              + ", '" + mMemoText + "'"
       *              + ")";
       *  if(MainActivity.mDatabase != null) {
       *      MainActivity.mDatabase.executeQuery(query);
       *  }
       *
       *
       *
       *
       * *** MODIFY ********************************************
       *  String query = "UPDATE " + MemoDatabase.TABLE_MEMO
       *          + " SET ID_PHOTO = " + photoId
       *          + ", ID_VIDEO = " + videoId
       *          + ", ID_VOICE = " + voiceId
       *          + ", ID_HANDWRITING = " + handwritingId
       *          + ", INPUT_DATE = DATETIME('" + mMemoDate + "') "
       *          + ", CONTENT_TEXT = '" + mMemoText + "'"
       *          + " WHERE _id = " + mMemoId;
       *  if(MainActivity.mDatabase != null) {
       *      MainActivity.mDatabase.executeQuery(query);
       *  }
       *
       *
       *
       *
       *  *** REMOVE ********************************************
       *  String query = "DELETE FROM " + MemoDatabase.TABLE_MEMO
       *          + " WHERE _id = " + mMemoId;
       *  if(MainActivity.mDatabase != null) {
       *      MainActivity.mDatabase.executeQuery(query);
       *  }
       *
       *
     */
    public boolean executeQuery(String query) {
        boolean isExecuted = false;
        try {
            mDb.execSQL(query);
            isExecuted = true;
        } catch (Exception ex) { Log.e(TAG, "Exception in execute query.", ex); }

        return isExecuted;
    }

    /**
     * 하나의 레코드 입력
     * @param tblName
     * @param params
     *     String tblName = "TBL_PHOTO";
     *     String uri = "Moon.png";
     *     ContentValues params = new ContentValues();
     *     params.put("Uri", uri);
     *     if(insertRecord(tblName, params)) { Log.d("DB", "success input."); }
     * @return 레코드 입력 성공 여부
     * @throws SQLiteException
     */
    public boolean insertRecord(String tblName, ContentValues params) throws SQLiteException {
        int cnt = (int) mDb.insert(tblName, null, params);
        return cnt > 0;
    }


    /**
     * 하나의 레코드 입력 후 신규 입력 레코드 아이디 반환
     * @param tableName
     * @param params
     *     String tblName = "TBL_PHOTO";
     *     String uri = "Moon.png";
     *     ContentValues params = new ContentValues();
     *     params.put("Uri", uri);
     *     int newId = insertRecord(tblName, params);
     * @return 신규 입력 레코드 아이디 (-1: failed input.)
     */
    public int insertMedia(String tableName, ContentValues params) {
        if(insertRecord(tableName, params)) {
            MediaRecord record = (MediaRecord)selectMediaByUri(tableName, params.getAsString(BasicInfo.COLUMN_URI));
            if(record != null) { return record.getId(); }
        }
        return -1;
    }





    /**
     * 해당 조건을 만족하는 레코드 조회
     * @param tblName
     * @param selectColumns
     * @param where
     * @param whereArgs
     *     String tblName = "TBL_PHOTO";
     *     int id = 1;
     *     String[] selectColumns = { "_id", "uri" };
     *     String where = "_id=?";
     *     String[] whereArgs = { String.valueOf(id) };
     *     MediaRecord photoRecord = selectRecord(tblName, selectColumns, where, whereArgs);
     * @return
     * @throws SQLiteException
     */
    public List<MediaRecord> selectMedia(String tblName, String[] selectColumns, String where, String[] whereArgs) throws SQLiteException {
        return selectMedia(tblName, selectColumns, where, whereArgs, null, null, null);
    }
    public List<MediaRecord> selectMedia(String tblName, String[] selectColumns, String where, String[] whereArgs, String groupBy, String having, String orderBy) throws SQLiteException {
        List<MediaRecord> lst = null;

        Cursor cursor = mDb.query(tblName
                , selectColumns
                , where
                , whereArgs
                , groupBy
                , having
                , orderBy);

        final int cnt = cursor.getCount();

        if(cnt > 0) lst = new ArrayList<MediaRecord>();
        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            lst.add(new MediaRecord(cursor.getInt(0)
                    , cursor.getString(1))
            );
        }
        cursor.close();

        return lst;
    }
    public MediaRecord selectMediaById(String tblName, int id) {
        return selectMediaById(tblName, new String[] { BasicInfo.COLUMN_ID, BasicInfo.COLUMN_URI }, id);
    }
    public MediaRecord selectMediaById(String tblName, String[] selectColumns, int id) {
        return selectMediaById(tblName, selectColumns, String.valueOf(id));
    }
    public MediaRecord selectMediaById(String tblName, String[] selectColumns, String id) {
        List<MediaRecord> lst = selectMedia(tblName, selectColumns, "_id=?", new String[] { id });
        return (lst != null && lst.size() > 0) ? lst.get(0) : null;
    }
    public MediaRecord selectMediaByUri(String tblName, String uri) {
        return selectMediaByUri(tblName, new String[] { BasicInfo.COLUMN_ID, BasicInfo.COLUMN_URI }, uri);
    }

    public MediaRecord selectMediaByUri(String tblName, String[] selectColumns, String uri) {
        List<MediaRecord> lst = selectMedia(tblName, selectColumns, "uri=?", new String[] { uri });
        return (lst != null && lst.size() > 0) ? lst.get(0) : null;
    }


    /**
     *
     * @param tblName
     * @param where
     * @param whereArgs
     * @return
     * @throws SQLiteException
     */
    public List<MemoRecord> selectMemo(String tblName, String where, String[] whereArgs) throws SQLiteException {
        String[] selectColumns = new String[] {  "_id", "INPUT_DATE", "CONTENT_TEXT", "ID_HANDWRITING", "ID_PHOTO", "ID_VIDEO", "ID_VOICE"};
        return selectMemo(tblName, selectColumns, where, whereArgs, null, null, "_id DESC");
    }
    private List<MemoRecord> selectMemo(String tblName
                                      , String[] selectColumns, String where, String[] whereArgs
                                      , String groupBy, String having, String orderBy) {
        List<MemoRecord> lst = null;

        Cursor cursor = mDb.query(tblName
                , selectColumns
                , where
                , whereArgs
                , groupBy
                , having
                , orderBy);

        final int cnt = cursor.getCount();

        if(cnt > 0) lst = new ArrayList<MemoRecord>();
        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            lst.add(new MemoRecord(cursor.getInt(0)
                    , cursor.getString(1)
                    , cursor.getString(2)
                    , cursor.getInt(3)
                    , cursor.getInt(4)
                    , cursor.getInt(5)
                    , cursor.getInt(6))
            );
        }
        cursor.close();

        return lst;
    }
    public List<MemoRecord> selectAllMemo() {
        return selectAllMemo(0, 0);
    }
    public List<MemoRecord> selectAllMemo(int limit, int offset) {
        String query = "SELECT A._id, A.INPUT_DATE, A.CONTENT_TEXT"
                + ", A.ID_HANDWRITING, E.URI AS HANDWRITING_URI "
                + ", A.ID_PHOTO, B.URI AS PHOTO_URI"
                + ", A.ID_VIDEO, C.URI AS VIDEO_URI"
                + ", A.ID_VOICE, D.URI AS VOICE_URI"
                + " FROM " + TABLE_MEMO + " AS A "
                + "     LEFT OUTER JOIN " + TABLE_PHOTO + " AS B ON B._ID = A.ID_PHOTO "
                + "     LEFT OUTER JOIN " + TABLE_VIDEO + " AS C ON C._ID = A.ID_VIDEO "
                + "     LEFT OUTER JOIN " + TABLE_VOICE + " AS D ON D._ID = A.ID_VOICE "
                + "     LEFT OUTER JOIN " + TABLE_HANDWRITING + " AS E ON E._ID = A.ID_HANDWRITING "
                + "";
        if (limit > 0) {
            query += " LIMIT " + limit;
            if(offset > 0) {
                query += " OFFSET " + offset;
            }
        }
        return selectAllMemo(query);
    }
    public List<MemoRecord> selectAllMemo(String query) {
        List<MemoRecord> lst = null;

        Cursor cursor = mDb.rawQuery(query, null);
        final int cnt = cursor.getCount();
        if(cnt > 0) lst = new ArrayList<MemoRecord>();
        for(int i=0; i<cnt; i++) {
           cursor.moveToNext();
            lst.add(new MemoRecord(cursor.getInt(0)
                    , cursor.getString(1)
                    , cursor.getString(2)
                    , cursor.getInt(3)
                    , cursor.getString(4)
                    , cursor.getInt(5)
                    , cursor.getString(6)
                    , cursor.getInt(7)
                    , cursor.getString(8)
                    , cursor.getInt(9)
                    , cursor.getString(10))
            );
        }
        cursor.close();

        return lst;
    }
    public MemoRecord selectMemoById(String tblName, Integer id) throws SQLiteException {
        return selectMemoById(tblName, String.valueOf(id));
    }
    public MemoRecord selectMemoById(String tblName, String id) throws SQLiteException {
        List<MemoRecord> lst = selectMemo(tblName, "_id=?", new String[] { id });
        return (lst != null && lst.size() > 0) ? lst.get(0) : null;
    }




    /**
     * 해당 조건을 만족하는 레코드 수정
     * @param tblName
     * @param updateColumns
     * @param where
     * @param whereArgs
     *     String tblName = "TBL_PHOTO";
     *     int id = 1;
     *     String uri = "Moon-2018.png";
     *     ContentValues params = new ContentValues();
     *     params.put("Uri", uri);
     *     String where = " _id = ?";
     *     String[] whereArgs = { id.toString() };
     *     if(updateRecord(tblName, params, where, whereArgs)) { Log.d("DB", "success update."); }
     * @return
     * @throws SQLiteException
     */
    public boolean updateRecord(String tblName, ContentValues updateColumns, String where, String[] whereArgs) throws SQLiteException {
        final int cnt = mDb.update(tblName
                , updateColumns
                , where
                , whereArgs);

        return cnt > 0;
    }


    /**
     * 해당 조건을 만족하는 레코드 삭제
     * @param tblName
     * @param where
     * @param whereArgs
     *     String tblName = "TBL_PHOTO";
     *     int id = 1;
     *     String where = " _id = ?";
     *     String[] whereArgs = { id.toString() };
     *     if(deleteRecord(tblName, where, whereArgs)) { Log.d("DB", "success delete."); }
     * @return
     * @throws SQLiteException
     */
    public boolean deleteRecord(String tblName, String where, String[] whereArgs) throws SQLiteException {
        final int cnt = mDb.delete(tblName
                , where
                , whereArgs);

        return cnt > 0;
    }
    public boolean deleteMediaById(String tblName, int id) {
        return deleteMediaById(tblName, String.valueOf(id));
    }
    public boolean deleteMediaById(String tblName, String id) {
        return deleteRecord(tblName, "_id=?", new String[] { id });
    }



    /**
     * close database
     */
    public void close() {
        mDb.close();
        database = null;
    }


    /*******************************************************************************************
     * Memo Database
     *
     ******************************************************************************************/
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, BasicInfo.DATABASE_NAME, null, DATABASE_VERSION);
        }

        /**
         *
         * @param db
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            createTable(db, TABLE_MEMO);
            createTable(db, TABLE_PHOTO);
            createTable(db, TABLE_VIDEO);
            createTable(db, TABLE_VOICE);
            createTable(db, TABLE_HANDWRITING);
        }

        /**
         * database 에 테이블 생성
         * @param db : 테이블을 생성할 데이터베이스
         * @param table : 생성할 테이블명
         */
        private void createTable(SQLiteDatabase db, String table) {
            StringBuilder sb;

            switch (table) {
                case TABLE_MEMO:
                    sb = new StringBuilder("DROP TABLE IF EXISTS " + table);
                    try{
                        db.execSQL(sb.toString());
                    } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

                    sb.setLength(0);
                    sb.trimToSize();

                    sb.append("CREATE TABLE " + table  + " (");
                    sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
                    sb.append(", INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
                    sb.append(", CONTENT_TEXT TEXT DEFAULT '' ");
                    sb.append(", ID_PHOTO INTEGER ");
                    sb.append(", ID_VIDEO INTEGER ");
                    sb.append(", ID_VOICE INTEGER ");
                    sb.append(", ID_HANDWRITING INTEGER ");
                    sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
                    sb.append(")");

                    try{
                        db.execSQL(sb.toString());
                    } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

                    sb.setLength(0);
                    sb.trimToSize();
                    println("created table is " + table + ".");
                    break;


                case TABLE_PHOTO:
                case TABLE_VIDEO:
                case TABLE_VOICE:
                case TABLE_HANDWRITING:
                    sb = new StringBuilder("DROP TABLE IF EXISTS " + table);
                    try{
                        db.execSQL(sb.toString());
                    } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

                    sb.setLength(0);
                    sb.trimToSize();

                    sb.append("CREATE TABLE " + table  + " (");
                    sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
                    sb.append(", URI TEXT ");
                    sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
                    sb.append(")");

                    try{
                        db.execSQL(sb.toString());
                    } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

                    sb.setLength(0);
                    sb.trimToSize();
                    println("created table is " + table + ".");

                    sb.append("CREATE INDEX " + table + "_IDX ON " + table + " (URI) ");

                    try{
                        db.execSQL(sb.toString());
                    } catch (Exception ex) { Log.e(TAG, "Exception in create index", ex); }

                    sb.setLength(0);
                    sb.trimToSize();
                    println("created index is " + table + "_IDX.");
                    break;
            }
        }

        /**
         * Memo Database 에 필요한 모든 테이블과 인덱스 생성
         * @param db
         */
        private void createTable(SQLiteDatabase db) {
            StringBuilder sb;

            /***************************************************************************
             * Memo table
             */
            sb = new StringBuilder("DROP TABLE IF EXISTS " + TABLE_MEMO);
            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE TABLE " + TABLE_MEMO  + " (");
            sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
            sb.append(", INPUT_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(", CONTENT_TEXT TEXT DEFAULT '' ");
            sb.append(", ID_PHOTO INTEGER ");
            sb.append(", ID_VIDEO INTEGER ");
            sb.append(", ID_VOICE INTEGER ");
            sb.append(", ID_HANDWRITING INTEGER ");
            sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(")");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            /***************************************************************************
             * Photo table
             */
            sb = new StringBuilder("DROP TABLE IF EXISTS " + TABLE_PHOTO);
            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE TABLE " + TABLE_PHOTO  + " (");
            sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
            sb.append(", URI TEXT ");
            sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(")");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE INDEX " + TABLE_PHOTO + "_IDX ON " + TABLE_PHOTO + " (URI) ");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create index", ex); }

            sb.setLength(0);
            sb.trimToSize();

            /***************************************************************************
             * video table
             */
            sb = new StringBuilder("DROP TABLE IF EXISTS " + TABLE_VIDEO);
            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE TABLE " + TABLE_VIDEO  + " (");
            sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
            sb.append(", URI TEXT ");
            sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(")");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE INDEX " + TABLE_VIDEO + "_IDX ON " + TABLE_VIDEO + " (URI) ");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create index", ex); }

            sb.setLength(0);
            sb.trimToSize();

            /***************************************************************************
             * voice table
             */
            sb = new StringBuilder("DROP TABLE IF EXISTS " + TABLE_VOICE);
            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE TABLE " + TABLE_VOICE  + " (");
            sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
            sb.append(", URI TEXT ");
            sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(")");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE INDEX " + TABLE_VOICE + "_IDX ON " + TABLE_VOICE + " (URI) ");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create index", ex); }

            sb.setLength(0);
            sb.trimToSize();

            /***************************************************************************
             * Handwriting table
             */
            sb = new StringBuilder("DROP TABLE IF EXISTS " + TABLE_HANDWRITING);
            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in Drop table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE TABLE " + TABLE_HANDWRITING  + " (");
            sb.append(" _id INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT ");
            sb.append(", URI TEXT ");
            sb.append(", CREATE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ");
            sb.append(")");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create table", ex); }

            sb.setLength(0);
            sb.trimToSize();

            sb.append("CREATE INDEX " + TABLE_HANDWRITING + "_IDX ON " + TABLE_HANDWRITING + " (URI) ");

            try{
                db.execSQL(sb.toString());
            } catch (Exception ex) { Log.e(TAG, "Exception in create index", ex); }

            sb.setLength(0);
            sb.trimToSize();

            Log.d(TAG, "create tables.");
        }


        /**
         * Memo Database 가 열릴 때
         * @param db
         */
        @Override
        public void onOpen(SQLiteDatabase db) {
            println("Opened database is " + BasicInfo.DATABASE_NAME + ".");
        }

        /**
         * Memo Database 의 버전이 변경될 때
         * @param db
         * @param oldVersion
         * @param newVersion
         */
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            println("Upgrading database from version " + oldVersion + " to " + newVersion + ".");
        }
    }

    private void println(String msg) {
        Log.d(TAG, msg);
    }


}
