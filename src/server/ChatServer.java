package server;

//import dao.MemberDao;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class ChatServer {
    private static final int PORT = 9625;

    private ServerSocket serverSocket = null;
    private Socket clientSocket = null;

    private ArrayList<Socket> socketList = new ArrayList<>();
    private ArrayList<String> clientList = new ArrayList<>();

    public ChatServer() throws IOException {
        final String HOST = InetAddress.getLocalHost().getHostAddress();

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println(HOST + ": " + PORT + " Ready");

            while (true) {
                /* accept */
                clientSocket = serverSocket.accept();

                if (socketList.add(clientSocket)) {
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
                /*
                 * new BufferedReader(new InputStreamReader(clientSocket.getInputStream())) => Read data from the Client
                 *
                 */

                /* 버퍼에 저장 */
                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                while (true) {
                    /* Read client Message */
                    String clientMessage = bufferedReader.readLine();
                    if (clientMessage.trim().length() > 0) {
                        ChatServer.this.spreadMessage(clientMessage);
                    }
                    if (clientMessage.contains("/quit")) {
                        serverSocket.close();
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
        new ChatServer();
    }
}