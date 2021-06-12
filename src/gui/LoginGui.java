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

public class LoginGui extends JFrame implements LoginInterface {

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

    public LoginGui() {
        super("LOGIN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 250);
        setLocationRelativeTo(null);

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createTextField();
        createLabel();
        createButton();

        setVisible(true);

        clickLoginBtn(new User(emailTextField.getText(), pwPasswordField.getText()));
        enterPwPasswordField(new User(emailTextField.getText(), pwPasswordField.getText()));
        clickRegisterBtn();
    }

    public void createTextField() {
        emailTextField.setBounds(150, 50, 175, 35);
        contentPane.add(emailTextField);
        emailTextField.setColumns(10);

        pwPasswordField.setColumns(10);
        pwPasswordField.setBounds(150, 100, 175, 35);
        contentPane.add(pwPasswordField);
    }

    public void createLabel() {
        nicknameLabel.setBounds(50, 45, 60, 35);
        contentPane.add(nicknameLabel);

        pwLabel.setBounds(50, 90, 60, 35);
        contentPane.add(pwLabel);
    }

    public void createButton() {
        loginBtn.setBounds(115, 155, 115, 35);
        contentPane.add(loginBtn);

        registerBtn.setBounds(230, 155, 115, 35);
        contentPane.add(registerBtn);
    }

    public void loginBtn(User user) {
        try {
            if (emailTextField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "EMAIL을 입력해 주세요.");
            } else if (pwPasswordField.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "PW을 입력해 주세요.");
            } else {
                UserDao dao = UserDao.getInstance();
                user.setEmail(emailTextField.getText());
                user.setPassword(String.valueOf(pwPasswordField.getPassword()));

                String encryptedPassword = dao.findByPassword(user);
                String decryptedPassword = AES.decrypt(AES.cbc, AES.dbIv, encryptedPassword);

                if (user.getPassword().equals(decryptedPassword)) {
                    JOptionPane.showMessageDialog(null, "로그인 되었습니다. 채팅을 시작합니다.");
                    new ChatClient();
                } else if (dao.findByEmail(user).equals("1")) {
                    JOptionPane.showMessageDialog(null, "EMAIL 혹은 PW가 다릅니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "정보가 없습니다. 회원가입을 해주세요.");
                }
            }
        } catch (Exception exception) {
            System.out.println("clickLoginBtn Error" + exception);
        }
    }

    public void enterPwPasswordField(User user) {
        pwPasswordField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn(user);
            }
        });
    }

    public void clickLoginBtn(User user) {
        loginBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginBtn(user);
            }
        });
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
}