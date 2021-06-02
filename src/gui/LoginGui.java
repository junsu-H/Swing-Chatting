package gui;

import client.ChatClient;
import dao.MemberDao;
import models.Member;
import util.AESUtil;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.security.InvalidAlgorithmParameterException;

public class LoginGui extends JFrame {
    private JPanel contentPane = null;
    private static JTextField emailTextField = null;
    private JPasswordField pwTextField = null;
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

        clickLoginBtn(new Member(emailTextField.getText(), pwTextField.getText()));
        clickRegisterBtn();

    }

    public void createTextField() {
        /* EMAIL JTextField */
        emailTextField = new JTextField();
        emailTextField.setBounds(150, 50, 175, 35);
        contentPane.add(emailTextField);
        emailTextField.setColumns(10);

        /* PW JTextField */
        pwTextField = new JPasswordField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(150, 100, 175, 35);
        contentPane.add(pwTextField);
    }

    public void createLabel() {
        /* EMAIL JLabel */
        JLabel nicknameLabel = new JLabel("EMAIL");
        nicknameLabel.setBounds(50, 45, 60, 35);
        contentPane.add(nicknameLabel);

        /* PW JLabel */
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(50, 90, 60, 35);
        contentPane.add(pwLabel);
    }

    public void createButton() {
        /* LOGIN JButton */
        loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(115, 155, 115, 35);
        contentPane.add(loginBtn);

        /* REGISTER JButton */
        registerBtn = new JButton("REGISTER");
        registerBtn.setBounds(230, 155, 115, 35);
        contentPane.add(registerBtn);
    }


    public void clickLoginBtn(Member member) {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    MemberDao dao = MemberDao.getInstance();
                    member.setEmail(emailTextField.getText());
                    member.setPassword(String.valueOf(pwTextField.getPassword()));

                    String encryptedPassword = dao.findByPassword(member);
                    String decryptedPassword = AESUtil.decrypt(encryptedPassword);

                    if (member.getPassword().equals(decryptedPassword)) {
                        JOptionPane.showMessageDialog(null, "로그인 되었습니다. 채팅을 시작합니다.");
                            new ChatClient();
                    } else if (dao.findByEmail(member).equals("1")){
                        JOptionPane.showMessageDialog(null, "아이디 혹은 비밀번호가 다릅니다.");
                    } else {
                        JOptionPane.showMessageDialog(null, "회원가입을 해주세요.");
                    }

                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    System.out.println("DB에 가입 정보가 없습니다.");
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        });
    }

    public void clickRegisterBtn() {
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new RegisterGui();
            }
        });
    }


    public static void main(String[] args) {
        new LoginGui();
    }
}
//        EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                try {
//                    new LoginGui();
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
