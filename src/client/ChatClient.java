package client;

import gui.VoiceGui;
import interfaces.ChatClientInterface;
import org.json.simple.parser.ParseException;
import util.AES;
import util.Translate;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyleConstants;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.StringTokenizer;
import java.util.Vector;

public class ChatClient extends JFrame implements ChatClientInterface {
    /* socket */
    private static Socket socket;
    private String nickname;

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    /* 디폴트 방 */
    private String defaultRoom = "general";

    private Vector userVector = new Vector();
    private StringTokenizer tokenizer;
    private static boolean selfCheck = false;

    public ChatClient() throws BadLocationException {
        inputDialogNickname();
        createSocket();
        setFont(font);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(800, 600);
        setResizable(false);

        pane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(pane);
        pane.setLayout(null);

        /* 좌측 상단 대화가 보이는 창 */
        /* 가로 600 세로 450 */
        chatScroll.setBounds(10, 10, 600, 450);
        chatTextPane.setEnabled(false);
        pane.add(chatScroll);

        /* 좌측 하단 대화 입력창 */
        inputTextField.setBounds(10, 475, 600, 75);
        inputTextField.setMargin(new Insets(20, 20, 20, 20));
        inputTextField.setFont(font);
        pane.add(inputTextField);

        /* 우측 상단 목록 */
        userListScrollPane.setBounds(625, 30, 135, 300);
        pane.add(userListScrollPane);

        /* 우측 중단 쪽지 보내기 */
        noteBtn.setFont(font);
        noteBtn.setBounds(625, 335, 135, 30);
        pane.add(noteBtn);

        /* Voice */
        voiceBtn.setFont(font);
        voiceBtn.setBounds(625, 400, 135, 50);
        pane.add(voiceBtn);

        /* 우측 하단 보내기 버튼 */
        sendBtn.setFont(font);
        sendBtn.setBounds(625, 485, 135, 50);
        pane.add(sendBtn);

        setVisible(true);

        userList.setListData(userVector);
        clickSendBtn();
        clickVoiceBtn();
        clickNoteBtn();
        enterInputTextField();
        quit();
    }

    public void inputDialogNickname() {
        do {
            nickname = JOptionPane.showInputDialog("2글자 이상 사용할 NICKNAME을 적어주세요.", "guest").trim();
        } while (nickname == null || nickname.length() < 2);
        setTitle(nickname + "님의 채팅창");
    }

    public void createSocket() throws BadLocationException {
        try {
            socket = new Socket(HOST, PORT);
        } catch (Exception e) {
            System.out.println("new Socket Error");
        } finally {
            communication();
        }
    }

    public void communication() throws BadLocationException {
        try {
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        /* nickname을 server로 전송 */
        sendMessage(nickname);

        /* 왼쪽 정렬 */
        doc.insertString(doc.getLength(), "[System] " + nickname + "님이 입장하셨습니다.\n", left);

        /* userVector에 들어온 user 추가 */
        userVector.add(nickname);

        new Thread() {
            @Override
            public void run() {
                try {
                    while (true) {
                        String message = null;
                        try {
                            /* server가 보낸 메시지 받기 */
                            message = bufferedReader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
//                        System.out.println("(Test) 3. receiveMessage: " + message);
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
                } catch (BadLocationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /* 서버가 보내는 메세지 받기 */
    @Override
    public void receiveMessage(String pMessage) throws ParseException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalAccessException,
            BadLocationException {

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
            System.out.println("Client가 받은 decryptMessage: " + decryptMessage);
            if (selfCheck) {
                doc.setParagraphAttributes(doc.getLength(), 1, right, false);
                doc.insertString(doc.getLength(), secondParam + ": " + decryptMessage + "\n", right);
                doc.insertString(doc.getLength(), "(Translate) " + secondParam + ": " + Translate.translate(decryptMessage) + "\n", right);
                selfCheck = false;
            } else {
                doc.setParagraphAttributes(doc.getLength(), 1, left, false);
                doc.insertString(doc.getLength(), secondParam + ": " + decryptMessage + "\n", left);
                doc.insertString(doc.getLength(), "(Translate) " + secondParam + ": " + Translate.translate(decryptMessage) + "\n", left);
            }
            chatScroll.getVerticalScrollBar().setValue(chatScroll.getVerticalScrollBar().getMaximum());
        } else if (protocol.equals("newUser")) {
            userVector.add(secondParam.trim());
            doc.setParagraphAttributes(doc.getLength(), 1, left, false);
            doc.insertString(doc.getLength(), "[System] " + secondParam + "님이 입장하셨습니다.\n", left);
        } else if (protocol.equals("oldUser")) {
            userVector.add(secondParam.trim());
        } else if (protocol.equals("userListUpdate")) {
            userList.setListData(userVector);
        } else if (protocol.equals("note")) {
            String note = tokenizer.nextToken();
            System.out.println(secondParam + note + "test");
            System.out.println(secondParam + "사용자로부터 온 쪽지 " + note);
            JOptionPane.showMessageDialog(null, note, secondParam + "님으로부터 온 쪽지", JOptionPane.CLOSED_OPTION);
        } else if (protocol.equals("quit")) {
            userVector.remove(secondParam);
            userList.setListData(userVector);
            doc.insertString(doc.getLength(), "[System] " + secondParam + "님이 나갔습니다.\n", left);
        }
    }

    @Override
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
    
    @Override
    public void sendEncryptMessage() {
        try {
            String encryptMessage = AES.encrypt(AES.ofb, AES.messageIv, inputTextField.getText().trim()).trim();
            sendMessage("chatting?" + defaultRoom + "?" + encryptMessage);

            System.out.println("Client의 plainText: " + inputTextField.getText().trim());
            inputTextField.setText(null);
            System.out.println("Client의 encryptSendMessage: " + encryptMessage);

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

    @Override
    public void clickSendBtn() {
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendEncryptMessage();
                }
            }
        });
    }

    @Override
    public void enterInputTextField() {
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (inputTextField.getText().trim().length() > 0) {
                    sendEncryptMessage();
                }
            }
        });
    }

    @Override
    public void clickNoteBtn() {
        noteBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = (String) userList.getSelectedValue();
                String note = JOptionPane.showInputDialog("보낼 메시지를 넣어주세요");

                if (note != null) {
                    sendMessage("note?" + user + "?" + note);
                }
            }
        });
    }

    @Override
    public void clickVoiceBtn() {
        voiceBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (voiceBtn.getText().equals("Voice Chatting")) {
                    EventQueue.invokeLater(new Runnable() {
                        public void run() {
                            try {
                                new VoiceGui();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                }
            }
        });
    }

    @Override
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