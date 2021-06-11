package gui;

import client.ChatClient;
import interfaces.LoginInterface;
import dao.UserDao;
import models.User;
import util.AES;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoginGui extends JFrame implements LoginInterface {
    private final Logger logger = Logger.getLogger(LoginGui.class.getName());

    public LoginGui() {
        super("LOGIN");
        logger.setLevel(Level.ALL);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        /* location */
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createTextField();
        createLabel();
        createButton();

        setVisible(true);

        clickLoginBtn(new User(emailTextField.getText(), pwTextField.getText()));
        enterPwTextField(new User(emailTextField.getText(), pwTextField.getText()));
        clickRegisterBtn();
    }

    public void createTextField() {
        /* EMAIL JTextField */
        emailTextField.setBounds(150, 50, 175, 35);
        contentPane.add(emailTextField);
        emailTextField.setColumns(10);

        /* PW JTextField */
        pwTextField.setColumns(10);
        pwTextField.setBounds(150, 100, 175, 35);
        contentPane.add(pwTextField);
    }

    public void createLabel() {
        /* EMAIL JLabel */
        nicknameLabel.setBounds(50, 45, 60, 35);
        contentPane.add(nicknameLabel);

        /* PW JLabel */
        pwLabel.setBounds(50, 90, 60, 35);
        contentPane.add(pwLabel);
    }

    public void createButton() {
        /* LOGIN JButton */
        loginBtn.setBounds(115, 155, 115, 35);
        contentPane.add(loginBtn);

        /* REGISTER JButton */
        registerBtn.setBounds(230, 155, 115, 35);
        contentPane.add(registerBtn);
    }

    public void clickLoginBtn(User user) {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn(user);
            }
        });
    }

    public void enterPwTextField(User user) {
        pwTextField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn(user);
            }
        });
    }

    public void loginBtn(User user) {
        try {
            if (emailTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "EMAIL을 입력해 주세요.");
            } else if (pwTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "PW을 입력해 주세요.");
            } else {
                UserDao dao = UserDao.getInstance();
                user.setEmail(emailTextField.getText());
                user.setPassword(String.valueOf(pwTextField.getPassword()));

                String encryptedPassword = dao.findByPassword(user);
                String decryptedPassword = AES.decrypt(AES.cbc, AES.dbIv, encryptedPassword);

                if (user.getPassword().equals(decryptedPassword)) {
                    JOptionPane.showMessageDialog(null, "로그인 되었습니다. 채팅을 시작합니다.");
                    new ChatClient();
//                    new StressClient();
                } else if (dao.findByEmail(user).equals("1")) {
                    JOptionPane.showMessageDialog(null, "EMAIL 혹은 PW가 다릅니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "정보가 없습니다. 회원가입을 해주세요.");
                }
            }
        } catch (Exception exception) {
            logger.log(Level.INFO, "레벨 INFO 로그");
            System.out.println("clickLoginBtn Error" + exception);
//                    logger.log(Level.WARNING, "레벨 WARNING 로그");
//                    logger.log(Level.SEVERE, "레벨 SEVERE 로그");
//                    logger.log(Level.FINE, "레벨 FINE 로그");
        }
    }

    public void clickRegisterBtn() {
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            new RegisterGui();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
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