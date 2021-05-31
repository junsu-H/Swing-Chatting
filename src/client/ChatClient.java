package client;

import models.Member;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public class ChatClient extends JFrame {
    public static JTextField inputTextField;
    static JButton sendBtn;
    public static JTextArea chatTextArea;

    static Socket socket;

    private static int PORT = 9625;
    private static String HOST;

    static {
        try {
            HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    static {
        try {
            socket = new Socket("localhost", PORT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getNicknameFromMember(Member member) {
        String nickname = member.getNickname();
        return nickname;
    }

    public ChatClient(Member member) {
        /* First Frame */
        super("Client Chatting");
        Font font = new Font("바탕", Font.PLAIN, 15);
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

        /* 우측 상단 유저 목록 */
        this.add(userListTextPane(font));

        /* 우측 하단 보내기 버튼 */
        sendBtn = new JButton("Send");
        sendBtn.setFont(font);
        sendBtn.setBounds(1000, 800, 100, 35);
        this.add(sendBtn);

        setVisible(true);


//        new Thread(new ClientThread()).start();

        new Thread() {
            @Override
            public void run() {
                /* 읽어오기 */
                BufferedReader bufferedReader = null;

                try {
                    /* 소켓 데이터 읽어오기 */
                    bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    while (true) {
                        // Message from client
                        String clientMessage = bufferedReader.readLine();

                        if (clientMessage != null && clientMessage.trim().length() > 0) {
                            chatTextArea.append(clientMessage + "\n");
                            chatTextArea.setCaretPosition(chatTextArea.getText().length());
                        }
                    }
                } catch (Exception e) {
                    System.out.println(e + " => client run");
                    e.printStackTrace();
                } finally {
                    try {
                        bufferedReader.close();
                        System.out.println("Read Fin");
                        chatTextArea.append("\n**Read Fin**\n");
                    } catch (IOException e) {
                        System.out.println(e);
                    }
                }
            }
        }.start();
        sendMessage(getNicknameFromMember(member) + "님이 입장하셨습니다.");

        inputTextField.addKeyListener(new KeyAdapter() {

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == 10) {
                    if (inputTextField.getText().trim().length() > 0) {
                        sendMessage(getNicknameFromMember(member) + " : " + inputTextField.getText().trim());
                        inputTextField.setText(null);
                    }
                }
            }

        });

        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendMessage(getNicknameFromMember(member) + " : " + inputTextField.getText().trim());
                    inputTextField.setText(null);
                }
            }
        });

//        clickSendBtn();
//        enterInputTextField();

    }

    /* 우측 상단 유저 목록 */
    public JTextPane userListTextPane(Font font) {
        JTextPane userListTextPane = new JTextPane();
        userListTextPane.setBounds(930, 10, 300, 760);
        userListTextPane.setBackground(Color.GREEN);
        userListTextPane.setFont(font);
        userListTextPane.setMargin(new Insets(20, 20, 20, 20));
        userListTextPane.setEditable(false);
        userListTextPane.setVisible(true);
        return userListTextPane;
    }

    public static void clickSendBtn() {
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
//                    sendMessage(nameFunc(member) + " : " + inputTextField.getText().trim());
                    inputTextField.setText(null);
                }
            }
        });
    }


    public static void enterInputTextField() {
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendMessage(inputTextField.getText().trim());
                    inputTextField.setText(null);
                }
            }
        });
    }

    public static void sendMessage(String message) {
        try {
            // 클라이언트에게 보내기 위한 준비
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

        } catch (Exception sendError) {
            System.out.println("메시지 전송 에러 : " + sendError);
        }

    }

}

