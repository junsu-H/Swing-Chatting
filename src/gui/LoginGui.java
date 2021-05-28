package gui;

import dao.MemberDao;
import util.AESUtil;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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


    public void clickLoginBtn() {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    String nickname = nicknameTextField.getText();
                    String password = String.valueOf(pwTextField.getPassword());

                    MemberDao dao = MemberDao.getInstance();
                    String encryptedPassword = dao.findByPassword(nickname);
                    String decryptedPassword = AESUtil.decrypt(encryptedPassword);
                    int result = 0;
                    if (password.equals(decryptedPassword)) {
                        result = dao.findByUsernameAndPassword(nickname, encryptedPassword);
                    }
                    if (result == 1) {
                        JOptionPane.showMessageDialog(null, "로그인 성공");
                    } else {
                        JOptionPane.showMessageDialog(null, "로그인 실패");
                    }

                } catch (NoSuchPaddingException noSuchPaddingException) {
                    noSuchPaddingException.printStackTrace();
                } catch (NoSuchAlgorithmException noSuchAlgorithmException) {
                    noSuchAlgorithmException.printStackTrace();
                } catch (InvalidKeyException invalidKeyException) {
                    invalidKeyException.printStackTrace();
                } catch (IllegalBlockSizeException illegalBlockSizeException) {
                    illegalBlockSizeException.printStackTrace();
                } catch (BadPaddingException badPaddingException) {
                    badPaddingException.printStackTrace();
                } catch (InvalidAlgorithmParameterException invalidAlgorithmParameterException) {
                    invalidAlgorithmParameterException.printStackTrace();
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
