package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.StringTokenizer;
import java.util.Vector;

public class StressServer {
    /* HOST, IP */
    private String HOST = InetAddress.getLocalHost().getHostAddress();
    private int PORT = 9625;

    /* Socket */
    private ServerSocket serverSocket;
    private Socket socket = null;

    private Vector<UserInfo> userVector = new Vector<>();
    private Vector roomVector = new Vector<>();

    private StringTokenizer tokenizer = null;

    private boolean roomCheck = true;

    public static void main(String[] args) throws IOException {
        new StressServer();
    }

    public StressServer() throws IOException {
        serverSocket = new ServerSocket(PORT);
        clientAccept();
    }

    // 클라이언트를 계속 받기 위한 쓰레드
    public void clientAccept() throws IOException {
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
//        private DataOutputStream dataOutputStream;
//        private DataInputStream dataInputStream;

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
//                    String clientMessage = dataInputStream.readUTF();
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

                    String clientMessage = bufferedReader.readLine();
                    System.out.println(clientMessage);
                    if (clientMessage.trim().length() > 0) {
                        System.out.println("server => " + nickname + ": " + clientMessage);
                        receiveMessage(clientMessage);
                    }
                }
            } catch (Exception e) {
                System.out.println("Still Server Run");
                e.printStackTrace();
            }
        }

        public void clientCommunication() {
            try {
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
                nickname = bufferedReader.readLine(); // 사용자 닉네임 받기

//                dataInputStream = new DataInputStream(StressServer.this.socket.getInputStream());
//                dataOutputStream = new DataOutputStream(StressServer.this.socket.getOutputStream());
//                nickname = dataInputStream.readUTF(); // 사용자 닉네임 받기

                System.out.println("이것이 nickname이다: " + nickname);

                broadCast("newUser?" + nickname);                 // 자신에게 기존 사용자를 알림

                // 자신에게 기존 사용자를 받아오는 부분
                for (int i = 0; i < userVector.size(); i++) {
                    UserInfo userInfo = (UserInfo) userVector.elementAt(i);
                    sendMessage("oldUser?" + userInfo.nickname);
                }
                userVector.add(this);
                broadCast("userListUpdate?null");

                sendMessage("roomListUpdate?null");

                // 자신에게 기존 방 목록을 받아오는 부분
                for (int i = 0; i < roomVector.size(); i++) {
                    RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
                    sendMessage("oldRoom?" + roomInfo.roomName);
                }
            } catch (IOException e) {
//                try {
////                    dataInputStream.close();
////                    dataOutputStream.close();
//                    bufferedWriter.close();
//                    bufferedReader.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
                e.printStackTrace();
            }
        }

        // 클라이언트로부터 들어오는 메세지 처리
        private void receiveMessage(String pMessage) {
            tokenizer = new StringTokenizer(pMessage, "?");

            String protocol = tokenizer.nextToken();
            String secondParam = tokenizer.nextToken();

            System.out.println("프로토콜: " + protocol);
            System.out.println("메세지: " + secondParam);

            if (protocol.equals("chatting")) {
                String msg = tokenizer.nextToken();
                broadCast("chatting?" + nickname + "?" + msg);
                System.out.println("2. Server의 broadCast encryptSendMessage: " + msg);

//                sendMessage("chatting/" + nickname + "/" + msg);
                for (int i = 0; i < roomVector.size(); i++) {
                    RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
                    sendMessage("chatting?" + nickname + "?" + msg);
                    roomInfo.broadCastRoom("chatting?" + nickname + "?" + msg);
                    System.out.println("클라이언트로 보내는 메세지: chatting?" + nickname + "?" + msg);
                }
            } else if (protocol.equals("quit")) {
                broadCast("quit?" + secondParam);
            }
            else if (protocol.equals("note")) {
                tokenizer = new StringTokenizer(secondParam, "@");

                String user = tokenizer.nextToken();
                String note = tokenizer.nextToken();

                System.out.println("받는 사람: " + secondParam);
                System.out.println("보낼 내용: " + note);
                // 벡터에서 해당 사용자를 찾아서 전송

                for (int i = 0; i < userVector.size(); i++) {
                    UserInfo userInfo = (UserInfo) userVector.elementAt(i);
                    if (userInfo.nickname.equals(user)) {
                        userInfo.sendMessage("note?" + nickname + "@" + note);
                    }
                }
            } // if 끝
            else if (protocol.equals("createRoom")) {
                // 1. 현재 같은 방이 존재하는지 확인
                for (int i = 0; i < roomVector.size(); i++) {
                    RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);

                    if (roomInfo.roomName.equals(secondParam)) {
                        sendMessage("createRoomFail?ok");
                        roomCheck = false;
                        break;
                    }
                } // for 끝

                if (roomCheck) {
                    RoomInfo roomInfo = new RoomInfo(secondParam, this);
                    roomVector.add(roomInfo);
                    sendMessage("createRoom?" + secondParam);
                    broadCast("newRoom?" + secondParam);
                }

                roomCheck = true;

            } else if (protocol.equals("joinRoom")) {
                for (int i = 0; i < roomVector.size(); i++) {
                    RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
                    if (roomInfo.roomName.equals(secondParam)) {
                        roomInfo.broadCastRoom("chatting?system?[System] " + nickname + "님이 입장하셨습니다.");
                        // 사용자 추가
                        roomInfo.addUser(this);
                        sendMessage("joinRoom?" + secondParam);
                    }
                }
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

//        /* 클라이언트로 데이터 보내기 */
//        public void sendMessage(String message) {
//            try {
//                dataOutputStream.writeUTF(message);
//                dataOutputStream.flush();
//            } catch (IOException ioException) {
//                ioException.printStackTrace();
//            }
//        }
//
//    }

    class RoomInfo {
        private String roomName;
        private Vector roomUserVector = new Vector();

        public RoomInfo(String roomName, UserInfo userInfo) {
            this.roomName = roomName;
            this.roomUserVector.add(userInfo);
        }

        public void broadCastRoom(String message) {
            for (int i = 0; i < roomUserVector.size(); i++) {
                UserInfo userInfo = (UserInfo) roomUserVector.elementAt(i);
                userInfo.sendMessage(message);
            }
        }

        private void addUser(UserInfo userInfo) {
            this.roomUserVector.add(userInfo);
        }

    }

    public String emoji(String message) {
        /* 이모티콘 유니코드 정보
         * https://apps.timwhitlock.info/emoji/tables/unicode
         */
        message = message.replace(":)", new String(Character.toChars(0x1F603)));
        message = message.replace(":D", new String(Character.toChars(0x1F604)));
        message = message.replace(">_<", new String(Character.toChars(0x1F606)));
        message = message.replace(":(", new String(Character.toChars(0x1F61E)));
        message = message.replace("(하트)", new String(Character.toChars(0x1F60D)));
        message = message.replace("(메롱)", new String(Character.toChars(0x1F61D)));
        return message;
    }
}