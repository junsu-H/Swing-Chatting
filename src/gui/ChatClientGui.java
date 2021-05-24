package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChatClientGui extends JFrame {
    JTextField inputTextField;
    JButton sendBtn;
    JTextArea chatTextArea;

    public ChatClientGui() {
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

        clickSendBtn();
        enterInputTextField();
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

    public void clickSendBtn() {
        sendBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(inputTextField.getText());
                chatTextArea.append(inputTextField.getText() + "\n");
            }
        });
    }
    public void enterInputTextField() {
        inputTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JTextField t = (JTextField) e.getSource();
                chatTextArea.append(t.getText() + "\n");
                t.setText("");
            }
        });
    }

    public static void main(String[] args) {
        new ChatClientGui();
    }
}
