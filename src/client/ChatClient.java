package client;

import org.json.simple.parser.ParseException;
import util.AES;
import util.Translate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatClient extends JFrame {
    /* HOST, IP */
    private static final String HOST = "localhost";
    private static final int PORT = 9625;

    /* socket */
    private static Socket socket;
    private String nickname;

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    /* 현재 내 방 */
    private String myRoom = "general";

    private static final Font font = new Font("바탕", Font.PLAIN, 15);

    private JPanel pane;

    //    private JTextArea chatTextPane = new JTextArea(10, 10);
    private JTextPane chatTextPane = new JTextPane();

    SimpleAttributeSet left = new SimpleAttributeSet();

    SimpleAttributeSet right = new SimpleAttributeSet();
    StyledDocument doc = chatTextPane.getStyledDocument();


    private JScrollPane chatScroll = new JScrollPane(chatTextPane);
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
    private static boolean selfCheck = false;


    public static void main(String[] args) throws IOException {
        new ChatClient();
    }

    public ChatClient() {
        inputDialogNickname();
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

        chatScroll.setBounds(10, 10, 600, 450);
        chatTextPane.setEnabled(false);
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
        quit();
    }

    public void inputDialogNickname() {
        do {
            nickname = JOptionPane.showInputDialog("2글자 이상 사용할 NICKNAME을 적어주세요.", "guest").trim();
        } while (nickname == null || nickname.length() < 2);
        setTitle(nickname + "님의 채팅창");
    }

    public void createSocket() {
        try {
            socket = new Socket(HOST, PORT);
        } catch (Exception e) {
            System.out.println("new Socket Error");
        } finally {
            connect();
        }
    }

    public void connect() {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* nickname server로 전송 */
        sendMessage(nickname);

        /* userVector 추가 */
        userVector.add(nickname);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        /* server가 보낸 메세지 받기 */
                        String message = null;
                        try {
                            message = bufferedReader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        System.out.println("3. receiveMessage: " + message);
                        receiveMessage(message);
                    }
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (BadLocationException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /* 서버가 보내는 메세지 받기 */
    public void receiveMessage(String pMessage) throws ParseException, NoSuchAlgorithmException, InvalidAlgorithmParameterException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException, UnsupportedEncodingException, NoSuchFieldException, IllegalAccessException, BadLocationException {
        StyleConstants.setAlignment(left, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(left, Color.RED);

        StyleConstants.setAlignment(right, StyleConstants.ALIGN_RIGHT);
        StyleConstants.setForeground(right, Color.BLUE);

        tokenizer = new StringTokenizer(pMessage, "?");

        String protocol = tokenizer.nextToken();
        String secondParam = tokenizer.nextToken();

        /* 내가 보낸 메세지는 우측에 가도록 */
        if (protocol.equals("chattingSelf")) {
            selfCheck = true;
        }

        if (protocol.equals("chatting")) {
            String message = tokenizer.nextToken();
            String decryptMessage = AES.decrypt(AES.ofb, AES.messageIv, message);

            if (selfCheck) {
                doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                doc.insertString(doc.getLength(), secondParam + ": " + decryptMessage + "\n", right);
//                doc.insertString(doc.getLength(), "(Translate) " + secondParam + ": " + Translate.translate(decryptMessage) + "\n", right);

                selfCheck = false;

            } else {
                doc.setParagraphAttributes(doc.getLength(), 1, left, false);
                doc.insertString(doc.getLength(), secondParam + ": " + decryptMessage + "\n", left);
                doc.insertString(doc.getLength(), "(Translate) " + secondParam + ": " + Translate.translate(decryptMessage) + "\n", left);

            }
            chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum());
        }

        if (protocol.equals("newUser")) {
            userVector.add(secondParam.trim());
            doc.insertString(doc.getLength(), "[System] " + secondParam + "님이 입장하셨습니다.\n", left);
            doc.setParagraphAttributes(doc.getLength(), 1, left, false);

//            chatTextPane.append("[System] " + secondParam + "님이 입장하셨습니다.\n");
        } else if (protocol.equals("oldUser")) {
            userVector.add(secondParam.trim());

//            chatTextPane.append("[System] " + secondParam + "님이 입장하셨습니다.\n");
        } else if (protocol.equals("userListUpdate")) {
            userList.setListData(userVector);
        } else if (protocol.equals("note")) {
            tokenizer = new StringTokenizer(secondParam, "@");
            String user = tokenizer.nextToken();
            String note = tokenizer.nextToken();
            System.out.println(user + "사용자로부터 온 쪽지 " + note);
            JOptionPane.showMessageDialog(null, note, user + "님으로부터 온 쪽지", JOptionPane.CLOSED_OPTION);

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
        } else if (protocol.equals("quit")) {
            userVector.remove(secondParam);
            userList.setListData(userVector);
            doc.insertString(doc.getLength(), "[System] " + secondParam + "님이 나갔습니다.\n", left);

//            chatTextPane.append("[System] " + secondParam + "님이 나갔습니다.\n");
        }
    }

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
            String encryptMessage = AES.encrypt(AES.ofb, AES.messageIv, inputTextField.getText().trim()).trim();
            sendMessage("chatting?" + myRoom + "?" + encryptMessage);
            inputTextField.setText(null);
//            System.out.println("(Test) 1. Client의 encryptSendMessag()e: " + encryptMessage);

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


    public void quit() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                super.windowClosing(e);
                sendMessage("quit?" + nickname);
            }

        });
    }


}