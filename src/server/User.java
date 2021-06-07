//package server;
//
//import java.io.*;
//import java.net.Socket;
//import java.util.StringTokenizer;
//
//class User implements Runnable {
//    //    private InputStream is;
//    private BufferedReader bufferedReader;
//
//    //    private OutputStream os;
//    private BufferedWriter bufferedWriter;
//
//    private Socket clientSocket;
//    private String nickname = "";
//
////    private DataOutputStream dos;
////    private DataInputStream dis;
//
//
//    public User(Socket socket) throws IOException {
//        this.clientSocket = socket;
//        userNetwork();
//    }
//
//    public void userNetwork() throws IOException {
//        try {
//            // dis
//            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
//
//            // dos
//            bufferedWriter = new BufferedWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
//
//            nickname = bufferedReader.readLine();
//            System.out.println("이것이 nickname이다: " + nickname);
//
//            broadCast("newUser/" + nickname);                 // 자신에게 기존 사용자를 알림
//
//            // 자신에게 기존 사용자를 받아오는 부분
//            for (int i = 0; i < userVector.size(); i++) {
//                User userInfo = (User) userVector.elementAt(i);
//                sendMessage("oldUser/" + userInfo.nickname);
//            }
//            sendMessage("roomListUpdate/null");
//            userVector.add(this);
//            broadCast("userListUpdate/null");
//
//            // 자신에게 기존 방 목록을 받아오는 부분
//            for (int i = 0; i < roomVector.size(); i++) {
//                RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
//                sendMessage("oldRoom/" + roomInfo.roomName);
//
//            }
//
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//
//    @Override
//    public void run() {
//        while (true) {
//            try {
//                String msg = bufferedReader.readLine(); // 메세지 수신
//                System.out.println(nickname + ": " + msg);
//                inMessage(msg);
//            } catch (IOException e) {
//                try {
//                    bufferedReader.close();
//                } catch (IOException ioException) {
//                    ioException.printStackTrace();
//                }
//                e.printStackTrace();
//            }
//        }
//    } // run 끝
//
//    // 클라이언트로부터 들어오는 메세지 처리
//    private void inMessage(String pMessage) {
//        tokenizer = new StringTokenizer(pMessage, "/");
//
//        String protocol = tokenizer.nextToken();
//        String message = tokenizer.nextToken();
//
//        System.out.println("프로토콜: " + protocol);
//        System.out.println("메세지: " + message);
//
//        if (protocol.equals("note")) {
//
//            tokenizer = new StringTokenizer(message, "@");
//
//            String user = tokenizer.nextToken();
//            String note = tokenizer.nextToken();
//
//            System.out.println("받는 사람: " + message);
//            System.out.println("보낼 내용: " + note);
//            // 벡터에서 해당 사용자를 찾아서 전송
//
//            for (int i = 0; i < userVector.size(); i++) {
//                User userInfo = (User) userVector.elementAt(i);
//                if (userInfo.nickname.equals(user)) {
//                    userInfo.sendMessage("note/" + nickname + "@" + note);
//                }
//            }
//        } // if 끝
//        else if (protocol.equals("createRoom")) {
//            // 1. 현재 같은 방이 존재하는지 확인
//            for (int i = 0; i < roomVector.size(); i++) {
//                RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
//
//                if (roomInfo.roomName.equals(message)) {
//                    sendMessage("createRoomFail/ok");
//                    roomCheck = false;
//                    break;
//                }
//            } // for 끝
//
//            if (roomCheck) {
//                RoomInfo roomInfo = new RoomInfo(message, this);
//                roomVector.add(roomInfo);
//                sendMessage("createRoom/" + message);
//                broadCast("newRoom/" + message);
//            }
//
//            roomCheck = true;
//
//        } else if (protocol.equals("chatting")) {
//            String msg = tokenizer.nextToken();
//            for (int i = 0; i < roomVector.size(); i++) {
//                RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
////                    sendMessage("chatting/" + nickname + "/" + msg);
//                roomInfo.broadCastRoom("chatting/" + nickname + "/" + msg);
//                System.out.println("클라이언트로 보내는 메세지: chatting/" + nickname + "/" + msg);
//            }
//        } else if (protocol.equals("joinRoom")) {
//            for (int i = 0; i < roomVector.size(); i++) {
//                RoomInfo roomInfo = (RoomInfo) roomVector.elementAt(i);
//                if (roomInfo.roomName.equals(message)) {
//                    roomInfo.broadCastRoom("chatting/system/[System] " + nickname + "님이 입장하셨습니다.");
//                    // 사용자 추가
//                    roomInfo.addUser(this);
//                    sendMessage("joinRoom/" + message);
//                }
//            }
//        }
//    }
//
//    // 전체 사용자에게 메세지 보내는 부분
//    public void broadCast(String message) {
//        // 기존 사용자에게 알림
//        for (int i = 0; i < userVector.size(); i++) {
//            User userInfo = (User) userVector.elementAt(i);
//            userInfo.sendMessage(message);
//        }
//    }
//
//    public void sendMessage(String message) {
//        try {
//            bufferedWriter.write(message);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
