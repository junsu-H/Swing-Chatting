package client;

import org.json.simple.parser.ParseException;
import util.AESUtil;
import util.Translate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.Vector;

import static util.AESUtil.*;

public class StressClient extends JFrame {
    /* HOST, IP */
    private static final String HOST = "localhost";
    private static final int PORT = 9625;

    /* socket */
    private static Socket socket;
    private String nickname;

    /* Stream */
//    private DataInputStream dataInputStream;
//    private DataOutputStream dataOutputStream;


    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;


    /* 현재 내 방 */
    private String myRoom = "general";

    /* GUI */
    private static final Font font = new Font("바탕", Font.PLAIN, 15);

    private JPanel pane;
    private JTextArea chatTextArea;
    private JTextField inputTextField;
    private JList userList = new JList();
    private JList roomList = new JList();


    private JButton sendBtn = new JButton("전송");
    private JButton noteBtn = new JButton("쪽지 보내기");
    private JButton createRoomBtn = new JButton("방 만들기");
    private JButton joinRoomBtn = new JButton("채팅방 참여");

    private Vector userVector = new Vector();
    private Vector roomVector = new Vector();
    private StringTokenizer tokenizer;

    public static void main(String[] args) throws IOException {
        new StressClient();
    }

    public StressClient() {
        nickname = JOptionPane.showInputDialog("사용할 NICKNAME을 적어주세요.");
        setTitle(nickname + "님의 채팅창");
        createSocket();
        setFont(font);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);

        pane = new JPanel();
        pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pane);
        pane.setLayout(null);
        /* 좌측 상단 대화가 보이는 창 */
        /* 가로 600 세로 450 */
        chatTextArea = new JTextArea(10, 10);
        JScrollPane chatScroll = new JScrollPane(chatTextArea);
        chatScroll.setBounds(10, 10, 600, 450);
        chatTextArea.setEnabled(false);
        pane.add(chatScroll);

        /* 좌측 하단 대화 입력창 */
        inputTextField = new JTextField(20);
        inputTextField.setBounds(10, 475, 600, 75);
        inputTextField.setMargin(new Insets(20, 20, 20, 20));
        inputTextField.setFont(font);
        pane.add(inputTextField);

        JLabel clientLabel = new JLabel("Client List");
        clientLabel.setBounds(625, 10, 75, 15);
        pane.add(clientLabel);

        /* 우측 상단 목록 */
        userList.setBounds(625, 30, 125, 150);
        pane.add(userList);

        /* 우측 중단 쪽지 보내기 */
        noteBtn.setFont(font);
        noteBtn.setBounds(625, 185, 125, 30);
        pane.add(noteBtn);

        JLabel roomLabel = new JLabel("Room List");
        roomLabel.setBounds(625, 220, 75, 15);
        pane.add(roomLabel);

        roomList.setBounds(625, 240, 125, 150);
        roomVector.add(myRoom);
        roomList.setListData(roomVector);
        pane.add(roomList);

        createRoomBtn.setFont(font);
        createRoomBtn.setBounds(625, 395, 125, 30);
        pane.add(createRoomBtn);

        joinRoomBtn.setFont(font);
        joinRoomBtn.setBounds(625, 430, 125, 30);
        pane.add(joinRoomBtn);

        /* 우측 하단 보내기 버튼 */
        sendBtn.setFont(font);
        sendBtn.setBounds(625, 485, 75, 50);
        pane.add(sendBtn);

        setVisible(true);

        userList.setListData(userVector);
        clickSendBtn();
        enterInputTextField();
    }


    public void createSocket() {
        try {
            socket = new Socket(HOST, PORT);

        } catch (Exception e) {
            System.out.println("new Socket Error");
        } finally {
            try {
                connect();
            } catch (IOException e) {
                System.out.println("connect() error");
            }
        }
    }

    public void connect() throws IOException {
//        dataInputStream = new DataInputStream(socket.getInputStream());
//        dataOutputStream = new DataOutputStream(socket.getOutputStream());

        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));

        // 처음 접속 시에 ID 전송
        sendMessage(nickname);

        // user List 초기화
        userVector.add(nickname);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        // 메세지 수신
//                        String message = dataInputStream.readUTF();

                        String message = bufferedReader.readLine();
                        System.out.println("receiveMessage: " + message);
                        // userVector
                        receiveMessage(message);
                        System.out.println(message);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {

                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }

            }
        }.start();
    }

    // 서버로부터 들어오는 모든 메세지
    public void receiveMessage(String pMessage) throws ParseException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException {
        tokenizer = new StringTokenizer(pMessage, "/");
        String protocol = tokenizer.nextToken();
        String secondParam = tokenizer.nextToken();

//        System.out.println("프로토콜: " + protocol);
//        System.out.println("내용: " + secondParam);

        if (protocol.equals("newUser")) {
            userVector.add(secondParam.trim());

        } else if (protocol.equals("chatting")) {
            String message = tokenizer.nextToken();
//            chatTextArea.append(secondParam +  ": " + message);
            String decryptMessage = AESUtil.messageDecrypt("aes/ofb/nopadding", AESUtil.messageKey, AESUtil.messageIv, message);
//            System.out.println("decryptMessage: " + decryptMessage);
            chatTextArea.append(secondParam + ": " + decryptMessage + "\n");
            chatTextArea.append(secondParam + ": " + Translate.translate(decryptMessage) + "\n");
        } else if (protocol.equals("oldUser")) {
            userVector.add(secondParam.trim());
        } else if (protocol.equals("note")) {
            tokenizer = new StringTokenizer(secondParam, "@");
            String user = tokenizer.nextToken();
            String note = tokenizer.nextToken();
            System.out.println(user + "사용자로부터 온 쪽지 " + note);
            JOptionPane.showMessageDialog(null, note, user + "님으로부터 온 쪽지", JOptionPane.CLOSED_OPTION);
        } else if (protocol.equals("userListUpdate")) {
            userList.setListData(userVector);
        } else if (protocol.equals("createRoom")) { // 새로운 방 만들었을 때
            myRoom = secondParam;
        } else if (protocol.equals("createRoomFail")) { // 방 만들기 실패
            JOptionPane.showMessageDialog(null, "방 만들기 실패", "알림", JOptionPane.CLOSED_OPTION);
        } else if (protocol.equals("newRoom")) {
            roomVector.add(secondParam);
            roomList.setListData(roomVector);

        } else if (protocol.equals("oldRoom")) {
            roomVector.add(secondParam);
        } else if (protocol.equals("roomListUpdate")) {
            roomList.setListData(roomVector);
        } else if (protocol.equals("joinRoom")) {
            myRoom = secondParam;
            JOptionPane.showMessageDialog(null, myRoom + " 채팅방에 입장했습니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        }
    }


//    public void sendMessage(String message) {
//        try {
//            // 채팅에서 유저가 보내는 메세지
//            dataOutputStream.writeUTF(message);
//            dataOutputStream.flush();
//
//        } catch (Exception sendError) {
//            System.out.println("메시지 전송 에러 : " + sendError);
//        }
//    }


    public void sendMessage(String message) {
        try {
            /* 데이터 보내기 */
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("sendMessage Error");
        }

    }

    private void encryptSendMessage() {
        try {
            String encryptMessage = AESUtil.messageEncrypt("aes/ofb/nopadding", AESUtil.messageKey, AESUtil.messageIv, inputTextField.getText().trim());
            sendMessage("chatting/" + myRoom + "/" + encryptMessage);
            System.out.println("encryptSendMessage: " + encryptMessage);

//            sendMessage("chatting/" + myRoom + "/" + inputTextField.getText().trim());
//            System.out.println("chatting/" + myRoom + "/" + inputTextField.getText().trim());
            inputTextField.setText(null);
        } catch (NoSuchPaddingException noSuchPaddingException) {
            noSuchPaddingException.printStackTrace();
        } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
            noSuchAlgorithmException.printStackTrace();
        } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
            invalidAlgorithmParameterException.printStackTrace();
        } catch (InvalidKeyException invalidKeyException) {
            invalidKeyException.printStackTrace();
        } catch (BadPaddingException badPaddingException) {
            badPaddingException.printStackTrace();
        } catch (IllegalBlockSizeException illegalBlockSizeException) {
            illegalBlockSizeException.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    /* 1. Client -> Server Message */
    public void clickSendBtn() {
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    encryptSendMessage();
                }
            }
        });
    }

    /* 1. Client -> Server Message */
    public void enterInputTextField() {
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    encryptSendMessage();
                }
            }
        });
    }

    public void clickJoinRoomBtn() {

    }


//    public void quit() {
//        addWindowListener(new WindowAdapter() {
//            @Override
//            public void windowClosing(WindowEvent e) {
//                super.windowClosing(e);
////                userListTextArea.append(String.valueOf(ChatServer.clientList));
////                clientList.remove(nickname);
//                sendMessage("[System]: " + nickname + "님이 퇴장하셨습니다.");
//            }
//
//        });
//    }
//

}