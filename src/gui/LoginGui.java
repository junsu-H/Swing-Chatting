package gui;

import dao.MemberDao;
import util.AESUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class LoginGui extends JFrame {
    private JPanel contentPane = null;
    private JTextField nicknameTextField = null;
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
        pwTextField = new JPasswordField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(150, 100, 175, 35);
        contentPane.add(pwTextField);
    }

    public void createLabel() {
        /* nickname JLabel */
        JLabel nicknameLabel = new JLabel("NICKNAME");
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

    public String getNickname(){
        return nicknameTextField.getText();
    }

    public void clickLoginBtn() {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String nickname = nicknameTextField.getText();
                    String password = String.valueOf(pwTextField.getPassword());

                    MemberDao dao = MemberDao.getMemberDao();
                    String encryptedPassword = dao.findByPassword(nickname);
                    String decryptedPassword = AESUtil.decrypt(encryptedPassword);
                    int result = 0;
                    if (password.equals(decryptedPassword)) {
                        result = dao.findByNicknameAndPassword(nickname, encryptedPassword);
                    }
                    if (result == 1) {
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                        new ChatClientGui(nickname);
                    } else {
                        JOptionPane.showMessageDialog(null, "가입된 정보와 다릅니다.");
                    }

                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
                } catch (NoSuchPaddingException noSuchPaddingException) {
                    System.out.println("test1");
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    System.out.println("test2");
                    illegalBlockSizeException.printStackTrace();
                } catch (UnsupportedEncodingException unsupportedEncodingException) {
                    System.out.println("test3");
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    System.out.println("test4");
                } catch (BadPaddingException badPaddingException) {
                    System.out.println("test5");
                } catch (InvalidKeyException invalidKeyException) {
                    System.out.println("test6");
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
