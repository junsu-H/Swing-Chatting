package server;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private String HOST = InetAddress.getLocalHost().getHostAddress();
    private int PORT = 9632;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    private ArrayList<Socket> socketList = new ArrayList<>();
    private ArrayList<String> clientList = new ArrayList<>();

    public ChatServer() throws IOException {
        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println(HOST + ": " + PORT + " Ready");
            while (true) {
                /* accept */
                clientSocket = serverSocket.accept();

                if (socketList.add(clientSocket)) {
                    /* select로 nickname 읽어와야 됨 */
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                    clientList.add(bufferedReader.readLine());
                    System.out.println("Client Enter!!!");

                    new Thread(new ServerThread()).start();
                }
            }

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    class ServerThread implements Runnable {
        @Override
        public void run() {
            BufferedReader bufferedReader = null;
            try {
                /* 클라이언트에서 보낸 메세지 읽기 */
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (true) {
                    String clientMessage = bufferedReader.readLine();
                    if (clientMessage.trim().length() > 0) {
                        ChatServer.this.spreadMessage(clientMessage);
                    }
                    if (clientMessage.contains("out ")) {
                        System.out.println("clear bufferLeader");
                        break;
                    }
                }
            } catch (Exception e) {
                System.out.println(e + " => server run");
            } finally {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    System.out.println(e + "=> ");
                }
            }
        }

    }

    public void spreadMessage(String message) {
        for (int i = socketList.size() - 1; i > -1; i--) {
            try {
                if (!socketList.get(i).isClosed()) {
                    BufferedWriter bufferedWriter = new BufferedWriter(
                            new OutputStreamWriter(socketList.get(i).getOutputStream()));
                    bufferedWriter.write(message);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } else {
                    socketList.remove(i);
                }
            } catch (Exception e) {
                System.out.println("Socket Error");
            }
        }
    }

    public static void main(String[] args) throws IOException {
        boolean connect = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;
        new ChatServer();
    }
}