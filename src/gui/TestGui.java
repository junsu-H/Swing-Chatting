package gui;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class TestGui extends JFrame {
    TestGui() {
        this.setTitle("텍스트영역 만들기 예제");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        this.add(new MyCenterPanel(), BorderLayout.CENTER);
        this.setLocationRelativeTo(null);
        this.setSize(300, 300);
        this.setVisible(true);
    }

    class MyCenterPanel extends JPanel {
        JTextField inputTextField;
        JButton sendBtn;
        JTextArea chatTextArea;

        MyCenterPanel() {
            inputTextField = new JTextField(20);
            sendBtn = new JButton("추가");

            sendBtn.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    chatTextArea.append(inputTextField.getText() + "\n");
                }
            });
            chatTextArea = new JTextArea(7, 20);
            add(inputTextField);
            add(sendBtn);
            add(new JScrollPane(chatTextArea));
        }
    }

    public static void main(String[] args) {
        new TestGui();
    }
}

