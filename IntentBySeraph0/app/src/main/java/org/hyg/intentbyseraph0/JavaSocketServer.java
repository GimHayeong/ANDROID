package org.hyg.intentbyseraph0;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by shiny on 2018-03-27.
 * 이클립스에서 JavaSocketServer 자바 프로젝트 생성
 */

public class JavaSocketServer {

    /**
     * 소켓 연결을 위한 ServerSocket 객체 생성
     * 클라이언트 연결 대기
     * 클라이언트 연결시 소켓객체를 참조하여 접속 클라이언트 정보 참조
     *  - ObjectInputStream : 서버측에서 클라이언트(getInputStream)에서 보내온 데이터 읽기 목적 스트림 객체 생성
     *  - ObjectOutputStream : 서버측에서 클라이언트(getOutputStream)로 보낼 데이터 쓰기 목적 스트림 객체 생성
     * @param args
     */
    public static void main(String[] args){
        try {

            int port = 5001;

            //:: 서버소켓을 만들고 클라이언트 접속을 대기
            System.out.println("Starting Java Socket Server ...");
            ServerSocket server = new ServerSocket(port);
            System.out.println("Listening at port " + port + " ... ");

            while (true) {
                //:: 접속한 클라이언트 정보를 처리하기 위한 소켓
                Socket socket = server.accept();

                InetAddress clientHost = socket.getInetAddress();
                int clientPort = socket.getPort();
                System.out.println("A client connected.\r\nhost : " + clientHost + ", port : " + clientPort);

                //:: Server <== Client
                ObjectInputStream inStream = new ObjectInputStream(socket.getInputStream());
                Object obj = inStream.readUTF();
                System.out.println("Input : " + obj);

                //:: Server ==> Client
                ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
                outStream.writeUTF(obj + " - from Server.");

                outStream.flush();

                socket.close();

            }

        } catch (IOException ex) {
            // ServerSocket 을 위한 Exception
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
