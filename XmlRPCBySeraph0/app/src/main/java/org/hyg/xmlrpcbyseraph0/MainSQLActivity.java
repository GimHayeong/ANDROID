package org.hyg.xmlrpcbyseraph0;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *  - 문자열 : text, varchar
 *  - 정수(2, 4바이트) : smallint, integer
 *  - 부동소수(4, 8바이트) : real, float, double
 *  - 논리값 : boolean
 *  - 시간(날짜, 시간, 일시) : date, time, timestamp
 *  - 이진수 : blob, binary
 */
public class MainSQLActivity extends AppCompatActivity {

    private EditText mEdtDBName, mEdtTBLName, mEdtName, mEdtAge, mEdtPhone;
    private TextView mTxtStatus;
    private Button mBtnCreateDB, mBtnCreateTBL, mBtnInsert;

    private SQLiteDatabase mDb;
    private DatabaseHelper mDbHelper;

    private String mCreatedTableName = null;
    private boolean mIsCreatedDB = false, mIsCreatedTBL = false;
    private void setIsCreatedDataBase(boolean isCreated){
        mIsCreatedDB = isCreated;
        mBtnCreateTBL.setEnabled(isCreated);
    }
    private void setIsCreatedTable(boolean isCreated){
        mIsCreatedTBL = isCreated;
        mBtnInsert.setEnabled(isCreated);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_sql);

        initWidget();
    }

    private void initWidget() {
        mEdtDBName = (EditText)findViewById(R.id.edtDBName);
        mEdtTBLName = (EditText)findViewById(R.id.edtTBLName);
        mTxtStatus = (TextView)findViewById(R.id.txtStatus);
        mEdtName = (EditText)findViewById(R.id.edtName);
        mEdtAge = (EditText)findViewById(R.id.edtAge);
        mEdtPhone = (EditText)findViewById(R.id.edtPhone);

        View.OnClickListener OnButtonClick = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.btnCreateDB:
                        String dbName = mEdtDBName.getText().toString();
                        createDatabase(dbName);
                        break;

                    case R.id.btnCreateTBL:
                        String tblName = mEdtTBLName.getText().toString();
                        createTable(tblName);
                        break;

                    case R.id.btnInsert:
                        createRecord();
                        break;

                    default:
                        break;
                }
            }
        };
        ((Button)findViewById(R.id.btnCreateDB)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnCreateTBL)).setOnClickListener(OnButtonClick);
        ((Button)findViewById(R.id.btnInsert)).setOnClickListener(OnButtonClick);
    }

    /**
     * 기존 데이터베이스가 있으면 열고, 없으면 새로 데이터베이스 생성하여 객체반환
     * @param database
     * @throws SQLiteException
     */
    private void createDatabase(String database) throws SQLiteException{
        mDb = openOrCreateDatabase(database, MODE_WORLD_WRITEABLE, null);

        setIsCreatedDataBase(true);
    }

    /**
     * [SQLiteOpenHelper 클래스 상속 구현 클래스 사용]
     * @return
     */
    private boolean openDatabaseByHelper(String database){
        mDbHelper = new DatabaseHelper(this, database, null, 0);
        mDb = mDbHelper.getWritableDatabase();

        return true;
    }

    /**
     * 해당 데이터베이스에 테이블명의 테이블 생성
     * @param table
     * @throws SQLiteException
     */
    private void createTable(String table) throws SQLiteException {
        mDb.execSQL("CREATE TABLE " + table + " ("
                    + " _id integer PRIMARY KEY autoincrement"
                    + ", name text"
                    + ", age Integer"
                    + ", phone text"
                    + ");");

        setIsCreatedTable(true);
        mCreatedTableName = table;
    }

    private void createRecord() throws SQLiteException {
        String name = mEdtName.getText().toString();
        Integer age = Integer.parseInt(mEdtAge.getText().toString());
        String phone = mEdtPhone.getText().toString();

        mDb.execSQL("INSERT INTO " + mCreatedTableName
                    + " (name, age, phone) VALUES ("
                    + name
                    + ", " + age
                    + ", " + phone
                    + ");");
    }

    /**
     * SQLiteDatabase 의 insert() 메서드 호출
     * @param tblName
     * @return
     * @throws SQLiteException
     */
    private boolean createRecord(String tblName) throws SQLiteException {
        String name = mEdtName.getText().toString();
        Integer age = Integer.parseInt(mEdtAge.getText().toString());
        String phone = mEdtPhone.getText().toString();

        ContentValues params = new ContentValues();
        params.put("name", name);
        params.put("age", age);
        params.put("phone", phone);

        /**
         * insert() 메서드 호출
         */
        int cnt = (int) mDb.insert(tblName, null, params);

        return cnt > 0;
    }

    /**
     * 전체 레코드 갯수 조회
     * @return
     * @throws SQLiteException
     */
    private int getRecordCount() throws SQLiteException {
        Cursor cursor = mDb.rawQuery("SELECT COUNT(_id) AS total FROM " + mCreatedTableName, null);
        final int cnt = cursor.getCount();
        cursor.close();

        return cnt;
    }

    /**
     * 전체 레코드 반환
     * @return
     * @throws SQLiteException
     */
    private List<Employee> readRecord() throws SQLiteException {
        List<Employee> employees = new ArrayList<Employee>();
        Cursor cursor = mDb.rawQuery("SELECT _id, name, age, phone FROM " + mCreatedTableName
                                        + " ORDER BY name ASC"
                                     , null);
        final int cnt = cursor.getCount();

        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            employees.add(new Employee(cursor.getInt(0)
                                     , cursor.getString(1)
                                     , cursor.getInt(2)
                                     , cursor.getString(3))
                         );
        }
        cursor.close();

        return employees;
    }

    /**
     * 해당 아이디의 데이터 조회 반환
     * @param id
     * @return 조회 데이터를 Employee 형으로 변환하여 반환
     * @throws SQLiteException
     */
    private Employee readRecord(Integer id) throws SQLiteException {
        Employee employee = null;
        String[] args = { id.toString() };
        Cursor cursor = mDb.rawQuery("SELECT FROM " + mCreatedTableName + " WHERE _id = ?", args);

        while(cursor.moveToNext()){
            employee = new Employee(cursor.getInt(0)
                                  , cursor.getString(1)
                                  , cursor.getInt(2)
                                  , cursor.getString(3));
        }
        cursor.close();

        return employee;
    }

    /**
     * SQLiteDatabase 의 query() 메서드 호출 : SELECT 문
     * @param tblName
     * @param id
     * @return
     * @throws SQLiteException
     */
    private Employee readRecord(String tblName, Integer id) throws SQLiteException {
        Employee employee = null;
        String[] selectColumns = { "name", "age", "phone" };
        String where = " WHERE _id = ?";
        String[] whereArgs = { id.toString() };

        Cursor cursor = mDb.query(tblName
                                , selectColumns
                                , where
                                , whereArgs
                                , null
                                , null
                                , null);

        final int cnt = cursor.getCount();

        for(int i=0; i<cnt; i++){
            cursor.moveToNext();
            employee = new Employee(cursor.getInt(0)
                                  , cursor.getString(1)
                                  , cursor.getInt(2)
                                  , cursor.getString(3));
        }
        cursor.close();

        return employee;
    }

    /**
     * 해당 아이디의 데이터 수정
     * @param id
     * @return 수정 성공여부 반환
     * @throws SQLiteException
     */
    private boolean editRecord(Integer id) throws SQLiteException {
        String name = mEdtName.getText().toString();
        Integer age = Integer.parseInt(mEdtAge.getText().toString());
        String phone = mEdtPhone.getText().toString();

        String[] args = { name, age.toString(), phone, id.toString() };


        Cursor cursor = mDb.rawQuery("UPDATE " + mCreatedTableName + " SET "
                                        + " name = ?"
                                        + ", age = ?"
                                        + ", phone = ?"
                                        + " WHERE _id = ?"
                                    , args);

        final int cnt = cursor.getCount();
        cursor.close();

        return cnt > 0;
    }

    /**
     * SQLiteDatabase 의 update() 메서드 호출
     * @param tblName
     * @param id
     * @return
     * @throws SQLiteException
     */
    private boolean editRecord(String tblName, Integer id) throws SQLiteException {
        String name = mEdtName.getText().toString();
        Integer age = Integer.parseInt(mEdtAge.getText().toString());
        String phone = mEdtPhone.getText().toString();

        ContentValues updateColumns = new ContentValues();
        updateColumns.put("age", 22);
        String where = "_id = ?";
        String[] whereArgs = { id.toString() };

        final int cnt = mDb.update(tblName
                                 , updateColumns
                                 , where
                                 , whereArgs);

        return cnt > 0;
    }

    /**
     *
     * @param id
     * @return
     * @throws SQLiteException
     */
    private boolean deleteRecord(Integer id) throws SQLiteException {
        String[] args = { id.toString() };
        Cursor cursor = mDb.rawQuery("DELETE " + mCreatedTableName + " WHERE _id = ?"
                                   , args);
        final int cnt = cursor.getCount();
        cursor.close();

        return cnt > 0;
    }

    /**
     * SQLiteDatabase 의 delete() 메서드 호출
     * @param tblName
     * @param id
     * @return
     * @throws SQLiteException
     */
    private boolean deleteRecord(String tblName, Integer id) throws SQLiteException {
        String where = "_id = ?";
        String[] whereArgs = { id.toString() };

        final int cnt = mDb.delete(tblName
                                 , where
                                 , whereArgs);

        return cnt > 0;
    }

}
