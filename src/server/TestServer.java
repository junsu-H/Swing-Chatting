package server;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class TestServer {
    private int PORT = 9632;
    private String IP = InetAddress.getLocalHost().getHostAddress();

    private ServerSocket serverSocket;

    private Socket clientSocket;


//    private boolean condition = true;
//
//
    private ArrayList<Socket> socketList = new ArrayList<>();
    private ArrayList<String> clientList = new ArrayList<>();
    private int numberOfClient = 0;

    public TestServer() throws UnknownHostException {
        try {

            /* create serverSocket */
            serverSocket = new ServerSocket(PORT);


//            serverSocket.bind(new InetSocketAddress(IP, PORT));
//            System.out.println("Bind Success");
//            System.out.println(IP + ": " + PORT);

            while (true) {
                /* accept */
                clientSocket = serverSocket.accept();

                if (socketList.add(clientSocket)) {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    if (numberOfClient >= 1) {
                        System.out.println("compare User" + clientList.get(0));
                        System.out.println("Now user " + numberOfClient);
                        String compareName = bufferedReader.readLine();
                        System.out.println("Compare Name : " + compareName);
                        for (int i = 0; i < clientList.size(); i++) {
                            if (compareName == clientList.get(i)) {
                                try {
                                    System.out.println("Exec~~~");
//                                    Whisper w = (Whisper) Naming.lookup("rmi://localhost:1099/whisper");
//                                    w.setCheckValue(true);
                                } catch (Exception nbe) {
                                    System.out.println("error : " + nbe);
                                }
                            }
                        }
                    }

                    clientList.add(bufferedReader.readLine());
                    System.out.println("Client enter!!!!!!!!!!!!" + clientList.get(numberOfClient));
                    numberOfClient++;


                    new Thread() {
                        @Override
                        public void run() {
                            BufferedReader bufferedReader = null;
                            try {
                                bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                                while (true) {

                                    String clientMessage = bufferedReader.readLine();

                                    if (clientMessage.trim().length() > 0) {
                                        TestServer.this.spreadMessage(clientMessage);
                                    }

                                    if (clientMessage.contains("out ")) {
                                        System.out.println("clear bufferLeader");
                                        break;
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println(e + "=>server run");
                            } finally {
                                try {
                                    bufferedReader.close();
                                } catch (IOException e) {
                                    System.out.println(e + "=> ");
                                }
                            }
                        }

                    }.start(); // exec Thread

                }
            }

        } catch (Exception e) {
            System.out.println(e);
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
                System.out.println("Socket error");
            }
        }
    }

    public static void main(String[] args) throws UnknownHostException {
        boolean connect = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        PrintStream out = System.out;
        new TestServer();
    }
}

/*			w = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
			r = new BufferedReader(new InputStreamReader(c.getInputStream()));

			String m = null;

			while ((m = r.readLine()) != null) {
				out.println(m);
				m = in.readLine();
				w.write(m, 0, m.length());
				w.newLine();
				w.flush();
			}
			w.close();
			r.close();
			c.close();
		} catch (IOException io) {
			try {
				w.close();
				r.close();
				c.close();

			}  catch (IOException i) {
			} */

//    public static void main(String[] args) throws IOException {
//
//        try {
//
//
//            ServerSocket serverSocket = new ServerSocket(PORT);
//
//
//
//            /* binding */
//            serverSocket.bind(new InetSocketAddress(IP, PORT));
//            System.out.println("Bind Success");
//            System.out.println(IP + ": " + PORT);
//
//            /* client accept */
//            Socket clientSocket = serverSocket.accept();
//
//            /* connected */
//            InetSocketAddress remoteSocketIp = (InetSocketAddress) clientSocket.getRemoteSocketAddress();
//            int remoteHostPort = remoteSocketIp.getPort();
//            System.out.println("Connected!");
//            System.out.println(remoteSocketIp + ": " + remoteHostPort);
//
//            OutputStream outputStream = clientSocket.getOutputStream();
//
//            String sendDataString = "Hello Server";
//            outputStream.write(sendDataString.getBytes());
//
//            serverSocket.close();
//            clientSocket.close();
//        } catch (SocketException e) {
//            e.printStackTrace();
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}