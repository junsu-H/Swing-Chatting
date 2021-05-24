package gui;

import dao.MemberDao;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginGui extends JFrame {
    private JPanel contentPane = null;
    private JTextField nicknameTextField = null;
    private JTextField pwTextField = null;
    private JButton loginBtn = null;
    private JButton registerBtn = null;

    public LoginGui() {
        super("LOGIN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        contentPane = new JPanel();

        /* location */
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createTextField();
        createLabel();
        createButton();

        setVisible(true);

        clickLoginBtn();
        clickRegisterBtn();

    }

    public void createTextField() {
        /* NICKNAME JTextField */
        nicknameTextField = new JTextField();
        nicknameTextField.setBounds(150, 50, 175, 35);
        contentPane.add(nicknameTextField);
        nicknameTextField.setColumns(10);

        /* PW JTextField */
        pwTextField = new JTextField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(150, 100, 175, 35);
        contentPane.add(pwTextField);
    }

    public void createLabel(){
        /* nickname JLabel */
        JLabel nicknameLabel = new JLabel("NICKNAME");
        nicknameLabel.setBounds(50, 45, 60, 35);
        contentPane.add(nicknameLabel);

        /* PW JLabel */
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(50, 90, 60, 35);
        contentPane.add(pwLabel);
    }

    public void createButton(){
        /* LOGIN JButton */
        loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(115, 155, 115, 35);
        contentPane.add(loginBtn);

        /* REGISTER JButton */
        registerBtn = new JButton("REGISTER");
        registerBtn.setBounds(230, 155, 115, 35);
        contentPane.add(registerBtn);
    }


    public void clickLoginBtn(){
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String nickname = nicknameTextField.getText();
                String password = pwTextField.getText();
                MemberDao dao = MemberDao.getInstance();
                int result = dao.findByUsernameAndPassword(nickname, password);
                if(result == 1) {
                    JOptionPane.showMessageDialog(null, "로그인 성공");
                    new ChatClientGui();
                }else {
                    JOptionPane.showMessageDialog(null, "로그인 실패");
                }

            }
        });
    }

    public void clickRegisterBtn(){
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterGui();
            }
        });
    }


    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new LoginGui();

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
