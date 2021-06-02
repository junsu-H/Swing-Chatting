package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Vector;

public class ChatServer {
    private static final int PORT = 9625;

    private ServerSocket serverSocket = null;
    private Socket socket = null;

    /* 클라이언트 소켓리스트 */
    private Vector<Socket> socketList = new Vector<>();

    /* 클라이언트 닉네임 목록 */
    private ArrayList<String> clientList = new ArrayList<>();

    public ChatServer() throws IOException {
        final String HOST = InetAddress.getLocalHost().getHostAddress();

        try {
            System.out.println("connecting...");

            serverSocket = new ServerSocket(PORT);
            System.out.println(HOST + ": " + PORT + " Ready");

            /* 클라이언트한테 데이터를 받을 객체 */
            BufferedReader fromClientBufferedReader = null;

            /* 멀티 접속을 위한 while */
            while (true) {
                /* accept */
                socket = serverSocket.accept();

                /* 클라이언트가 보낸 거 받기 */
                fromClientBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                /* 클라이언트의 이름 받기 */
                String nickname = fromClientBufferedReader.readLine();

                /* client info */
                clientList.add(nickname);
                System.out.println("Connected Client: " + nickname);
                System.out.println("Host: " + socket.getInetAddress().getHostAddress());
                if (socketList.add(socket)) {
//                    for (int i = 0; i < clientList.size(); i++) {
//                        System.out.println(clientList);
//                        sendMessage("[System]: " + clientList.get(i) + "가 입장하셨습니다.\n");
//                    }
                    new Thread(new receiveThread()).start();

                }
            }

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            socket.close();
        }
    }

    class receiveThread implements Runnable {
        @Override
        public void run() {
            // 클라이언트가 보낸 메세지 읽을 객체
            BufferedReader bufferedReader = null;

            try {
                // 클라이언트가 보낸 메세지
                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                while (true) {
                    /* 클라이언트 메세지 읽기 */
                    String clientMessage = bufferedReader.readLine();
                    if (clientMessage.trim().length() > 0) {
                        /* 읽은 메세지 서버가 전달 */
                        ChatServer.this.sendMessage(clientMessage);
                    }
                }
            } catch (Exception e) {
                System.out.println("still server run");
            }
        }
    }



    public void sendMessage(String message) {
        for (int i = socketList.size() - 1; i > -1; i--) {
            try {
                if (socketList.get(i).isClosed()) {
                    socketList.remove(i);
                } else {
                    /* 데이터 보내기 */
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(socketList.get(i).getOutputStream()));
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                }
            } catch (Exception e) {
                System.out.println("sendMessage Error");
            }
        }
    }

    public synchronized void addClient(String nickname)
    {
        try {
            ChatServer.this.sendMessage(nickname+" 입장하셨습니다.");
            clientList.add(nickname);
            System.out.println("채팅 참여 인원 : "+ clientList.size());
        }catch(Exception e){
            System.out.println("addClient Error");
        }

    }

    public static void main(String[] args) throws IOException {
        new ChatServer();
    }
}