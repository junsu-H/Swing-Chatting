package server;

import util.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatServer {
    /* HOST, IP */
    private String HOST = InetAddress.getLocalHost().getHostAddress();
    public int PORT = 9625;

    /* Socket */
    private ServerSocket serverSocket;
    private Socket socket = null;

    private Vector<UserInfo> userVector = new Vector<>();
    private Vector roomVector = new Vector<>();

    private StringTokenizer tokenizer = null;

    private boolean roomCheck = true;

    public static void main(String[] args) throws IOException {
        new ChatServer();
    }

    public ChatServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        clientAccept();
        new VoiceServer();
    }

    // 클라이언트를 계속 받기 위한 쓰레드
    public void clientAccept() {
        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        System.out.println("connecting...");
                        socket = serverSocket.accept();
                        System.out.println("사용자 접속!");

                        // 메세지 받는 쓰레드
                        new Thread(new UserInfo(socket)).start();
                    }
                } catch (IOException e) {
                    System.out.println("Exception: " + e);
                    System.out.println("But Still Run Server");
                }
            }
        }.start();
    }

    class UserInfo implements Runnable {
        private BufferedReader bufferedReader = null;

        private Socket socket;
        private String nickname = "guest";

        public UserInfo(Socket socket) throws IOException {
            this.socket = socket;
            clientCommunication();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    // 클라이언트 메세지 받기
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                    String clientMessage = bufferedReader.readLine();
                    System.out.println(clientMessage);
                    if (clientMessage.trim().length() > 0) {
                        System.out.println("server => " + nickname + ": " + clientMessage);
                        receiveMessage(clientMessage);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Still Server Run");
            }
        }

        public void clientCommunication() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                nickname = bufferedReader.readLine(); // 사용자 닉네임 받기

                System.out.println("이것이 nickname이다: " + nickname);

                /* nickname 정보 뿌리기 */
                broadCast("newUser?" + nickname);

                for (int i = 0; i < userVector.size(); i++) {
                    sendMessage("newUser?" + userVector.elementAt(i).nickname);
                }
                userVector.add(this);
                broadCast("userListUpdate?" + nickname);

            } catch (IOException e) {
                try {
                    bufferedReader.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                e.printStackTrace();
            }
        }

        /* 클라이언트로부터 들어오는 메세지 처리 */
        private void receiveMessage(String pMessage) throws IOException {
            tokenizer = new StringTokenizer(pMessage, "?");

            String protocol = tokenizer.nextToken();
            String secondParam = tokenizer.nextToken();

            System.out.println("프로토콜: " + protocol);
            System.out.println("메세지: " + secondParam);

            if (protocol.equals("chatting")) {
                String message = tokenizer.nextToken();
                sendMessage("chattingSelf?" + nickname + "?" + message);
                broadCast("chatting?" + nickname + "?" + message);

//                System.out.println("(Test) 2. Server의 broadCast encryptSendMessage: " + (message));

            } else if (protocol.equals("note")) {
                tokenizer = new StringTokenizer(secondParam, "?");

                String note = tokenizer.nextToken();

                System.out.println("받는 사람: " + secondParam);
                System.out.println("보낼 내용: " + note);

                /* 벡터에서 해당 사용자를 찾아서 전송 */
                for (int i = 0; i < userVector.size(); i++) {
                    UserInfo userInfo = userVector.elementAt(i);
                    if (userInfo.nickname.equals(secondParam)) {
                        userInfo.sendMessage("note?" + nickname + "?" + note);
                    }
                }
            } else if (protocol.equals("quit")) {
                broadCast("quit?" + secondParam);
            }
        }

        // 모든 클라이언트에게 닉네임 뿌리기
        public void broadCast(String nickname) {
            for (int i = 0; i < userVector.size(); i++)
                userVector.elementAt(i).sendMessage(nickname);
        }

        public void sendMessage(String message) {
            try {
                /* 데이터 보내기 */
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
                bufferedWriter.write(message);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                System.out.println("sendMessage Error");
            }

        }
    }

}