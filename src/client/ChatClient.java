package client;

import server.ChatServer;
import util.Translate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ChatClient extends JFrame {
    private static Font font = new Font("바탕", Font.PLAIN, 15);
    private static JTextField inputTextField;
    private static JButton sendBtn;
    private static JTextArea chatTextArea;
    private static JTextArea userListTextArea = null;
    private static JScrollPane userListScroll;

    private static String[] clientList;
    private static String nickname;

    private static Socket socket;
    private static int PORT = 9625;
    private static String HOST = "localhost";

    public ChatClient() throws IOException {
        setFont(font);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 960);
        setResizable(false);

        /* 좌측 상단 대화가 보이는 창 */
        chatTextArea = new JTextArea(10, 10);
        JScrollPane chatScroll = new JScrollPane(chatTextArea);
        chatScroll.setBounds(10, 10, 900, 760);
        chatTextArea.setEnabled(false);
        this.add(chatScroll);

        /* 좌측 하단 대화 입력창 */
        inputTextField = new JTextField(20);
        inputTextField.setBounds(10, 800, 900, 60);
        inputTextField.setMargin(new Insets(20, 20, 20, 20));
        inputTextField.setFont(font);
        this.add(inputTextField);

        /* 우측 하단 보내기 버튼 */
        sendBtn = new JButton("Send");
        sendBtn.setFont(font);
        sendBtn.setBounds(1000, 800, 100, 35);
        this.add(sendBtn);

        try {
            socket = new Socket("localhost", PORT);
        } catch (Exception e) {
            e.printStackTrace();
        }

        this.add(userListTextPane(getNickname()));

        new Thread(new ClientThread()).start();
        clickSendBtn();
        enterInputTextField();
        setVisible(true);
    }

    public String getNickname() throws IOException {
        quit();
        BufferedWriter bufferedWriter = new BufferedWriter((new OutputStreamWriter(socket.getOutputStream())));
        do {
            nickname = JOptionPane.showInputDialog("사용할 NICKNAME을 적어주세요.");
        } while (nickname == null || nickname.length() < 2);
        sendMessage(nickname);
        setTitle(nickname + "님의 채팅창");
        return nickname;
    }

    class ClientThread implements Runnable {
        @Override
        public void run() {
            BufferedReader serverBufferedReader = null;

            try {
                serverBufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//                clientList = serverBufferedReader.readLine().replace("[", "").replace("]", "").split(", ");
//                for (String c : clientList) {
//                    userListTextArea.append("@" + c + "\n");
//                }

                while (true) {
                    String fromServerMessage = serverBufferedReader.readLine();

                    if (fromServerMessage != null && fromServerMessage.trim().length() > 0) {
                        chatTextArea.append(fromServerMessage + "\n");
                        chatTextArea.append(Translate.translate(fromServerMessage) + "\n");
                        chatTextArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);

                    }
                }
            } catch (Exception e) {
                System.out.println(e + " => client run");
                e.printStackTrace();
            } finally {
                try {
                    serverBufferedReader.close();
                    System.out.println("Read Fin");
                    chatTextArea.append("\n**Read Fin**\n");
                } catch (IOException e) {
                    System.out.println(e);
                }
            }
        }
    }

    /* 우측 상단 유저 목록 */
    public JTextArea userListTextPane(String nickname) throws IOException {
        userListTextArea = new JTextArea();
        userListScroll = new JScrollPane(userListTextArea);
        userListScroll.setBounds(930, 10, 300, 760);
        userListScroll.setFont(font);
        userListTextArea.setBounds(930, 10, 300, 760);
        userListTextArea.setFont(font);
        userListTextArea.setMargin(new Insets(20, 20, 20, 20));
        userListTextArea.setEditable(false);
        userListTextArea.setVisible(true);
//        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        userListTextArea.append("@" + bufferedReader.readLine());
        return userListTextArea;
    }

    public static void clickSendBtn() {
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendMessage(nickname + ": " + inputTextField.getText().trim() + "\n");
                    inputTextField.setText(null);
                }
            }
        });
    }

    public static void enterInputTextField() {
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendMessage(nickname + ": " + inputTextField.getText().trim() + "\n");
                    inputTextField.setText(null);
                }
            }
        });
    }

    public void quit() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
//                userListTextArea.append(String.valueOf(ChatServer.clientList));
//                clientList.remove(nickname);
                sendMessage("[System]: " + nickname + "님이 퇴장하셨습니다.");
            }

        });
    }

    public static void sendMessage(String message) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (Exception sendError) {
            System.out.println("메시지 전송 에러 : " + sendError);
        }

    }

}