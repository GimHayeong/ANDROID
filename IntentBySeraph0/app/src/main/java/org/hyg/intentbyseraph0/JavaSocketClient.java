package org.hyg.intentbyseraph0;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by shiny on 2018-03-27.
 * 이클립스에서 JavaSocketClient 자바 프로젝트 생성
 */

public class JavaSocketClient {
    /**
     *  - 소켓 연결을 위한 Socket 객체 생성
     *  - 데이터 쓰기 목적 스트림 객체 생성
     *  - 데이터 읽기 목적 스트림 객체 생성
     * @param args
     */
    public static void main(String[] args){
        try{
            int port = 5001;
            //:: 접속할 서버 (localhost = 클라이언트와 같은 컴퓨터에 있는 서버로 접속)
            String serverIp = "localhost";

            //:: 클라이언트 소켓
            Socket socket = new Socket(serverIp, port);

            //:: Client ==> Server
            ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
            outStream.writeUTF("Hello Android Town. - from Client.");
            outStream.flush();

            //:: Client <== Server
            ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
            System.out.println(inStream.readUTF());

            socket.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
}