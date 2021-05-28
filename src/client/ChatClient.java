//package client.client;
//
//import gui.ChatClientGui;
//
//import java.io.*;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.net.UnknownHostException;
//
//import static gui.ChatClientGui.chatTextArea;
//
//public class ChatClient {
//    static Socket socket = null;
//
//    private static int PORT = 9632;
//    private static String HOST;
//    public ChatClient() throws IOException {
//            HOST = InetAddress.getLocalHost().getHostAddress();
//
//            socket = new Socket(HOST, PORT);
//        }
//    }
//
//    public static class ClientThread implements Runnable {
//        @Override
//        public void run() {
//            BufferedReader bufferedReader = null;
//
//            try {
//                bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                while (true) {
//                    // Message from client
//                    String clientMessage = bufferedReader.readLine();
//
//                    if (clientMessage != null && clientMessage.trim().length() > 0) {
//                        chatTextArea.append(clientMessage + "\n");
//                        chatTextArea.setCaretPosition(chatTextArea.getText().length());
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println(e + " => client run");
//                e.printStackTrace();
//            } finally {
//                try {
//                    bufferedReader.close();
//                    System.out.println("Read Fin");
//                    chatTextArea.append("\n**Read Fin**\n");
//                } catch (IOException e) {
//                    System.out.println(e);
//                }
//            }
//        }
//
//    }
//    public static void sendMessage(String message) {
//        try {
//            // 클라이언트에게 보내기 위한 준비
//            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//
//            bufferedWriter.write(message);
//            bufferedWriter.newLine();
//            bufferedWriter.flush();
//
//        } catch (Exception sendError) {
//            System.out.println("메시지 전송 에러 : " + sendError);
//        }
//
//    }
//
//
//
//
//}
