package org.hyg.intentbyseraph0;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by shiny on 2018-03-09.
 * 객체 데이터를 전달할 때 바이트 배열로 변환하거나 Serializable 인터페이스를 구현하는 객체를 만들어 직렬화해 전달가능.
 * 안드로이드는 Serializable 보다 크기가 작은 Parcelable 인터페이스를 구현하는 객체를 만들어 직렬화해 전달 추천.
 * Parcelable 인터페이스 구현 객체는 객체를 직접 Bundle 에 추가할 수 있다.
 */

public class MenuData implements Parcelable {

    private int m_number;
    public int getNumber() { return m_number; }
    private String m_message;
    public String getMessage() { return m_message; }

    public MenuData(int num, String msg){
        m_number = num;
        m_message = msg;
    }

    /**
     * [Parcelable 인터페이스 구현]
     * Parcel 객체에서 읽기
     * @param objParcel
     */
    public MenuData(Parcel objParcel) {
        m_number = objParcel.readInt();
        m_message = objParcel.readString();
    }

    /**
     * [Parcelable 인터페이스 구현]
     * 상수 정의
     */
    public static final Creator<MenuData> CREATOR = new Creator<MenuData>() {
        /**
         * 생성자를 호출해 Parcel 객체에서 읽기
         * @param in
         * @return
         */
        @Override
        public MenuData createFromParcel(Parcel in) {
            return new MenuData(in);
        }

        @Override
        public MenuData[] newArray(int size) {
            return new MenuData[size];
        }
    };

    /**
     * [Parcelable 인터페이스 구현]
     * @return
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * [Parcelable 인터페이스 구현]
     * 데이터를 Parcel 객체로 변환
     * @param objParcel : 데이터를 쓸 Parcel 객체
     * @param flags
     */
    @Override
    public void writeToParcel(Parcel objParcel, int flags) {
        objParcel.writeInt(m_number);
        objParcel.writeString(m_message);
    }
}
