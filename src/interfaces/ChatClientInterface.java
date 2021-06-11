package interfaces;

import org.json.simple.parser.ParseException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public interface ChatClientInterface {
    /* HOST, IP */
    String HOST = "localhost";
    int PORT = 9625;
    Font font = new Font("바탕", Font.PLAIN, 15);

    JPanel pane = new JPanel();

    JTextPane chatTextPane = new JTextPane();

    SimpleAttributeSet left = new SimpleAttributeSet();
    SimpleAttributeSet right = new SimpleAttributeSet();
    StyledDocument doc = chatTextPane.getStyledDocument();
    JScrollPane chatScroll = new JScrollPane(chatTextPane);
    JTextField inputTextField = new JTextField(20);
    JList userList = new JList();
    JList roomList = new JList();

    JButton sendBtn = new JButton("SEND");
    JButton noteBtn = new JButton("쪽지 보내기");
    JButton voiceBtn = new JButton("음성 통화");
    JButton createRoomBtn = new JButton("방 만들기");
    JButton joinRoomBtn = new JButton("채팅방 참여");

    void receiveMessage(String pMessage) throws ParseException, NoSuchAlgorithmException,
            InvalidAlgorithmParameterException, NoSuchPaddingException,
            IllegalBlockSizeException, BadPaddingException,
            InvalidKeyException, UnsupportedEncodingException,
            NoSuchFieldException, IllegalAccessException,
            BadLocationException;
}