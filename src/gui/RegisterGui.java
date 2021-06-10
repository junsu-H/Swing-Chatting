package gui;

import interfaces.RegisterInterface;
import dao.UserDao;
import models.User;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGui extends JFrame implements RegisterInterface {
    private boolean checkEmail = false;

    public RegisterGui() {
        super("REGISTER");
        setSize(450, 500);
        setLocationRelativeTo(null);

        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createLabel();
        createTextField();

        /* Register Complete JButton */
        registerCompleteBtn.setBounds(180, 360, 140, 35);
        contentPane.add(registerCompleteBtn);

        /* CHECK EMAIL JButton */
        checkEmailBtn.setBounds(350, 140, 80, 35);
        contentPane.add(checkEmailBtn);

        setVisible(true);

        clickCheckEmailBtn();
        clickRegisterCompleteBtn();
    }

    public void createTextField() {
        /* EMAIL JTextField */
        emailTextField.setColumns(10);
        emailTextField.setBounds(160, 140, 185, 35);
        contentPane.add(emailTextField);

        /* PW JPasswordField */
        pwTextField.setColumns(10);
        pwTextField.setBounds(160, 220, 185, 35);
        contentPane.add(pwTextField);

        /* PW CHECK JPasswordField */
        pwCheckTextField.setColumns(10);
        pwCheckTextField.setBounds(160, 300, 185, 35);
        contentPane.add(pwCheckTextField);
    }

    public void createLabel() {
        /* REGISTER JLabel */
        JLabel registerLabel = new JLabel("REGISTER");
        registerLabel.setFont(new Font("바탕", Font.BOLD, 20));
        registerLabel.setBounds(160, 60, 200, 40);
        contentPane.add(registerLabel);

        /* EMAIL JLabel */
        JLabel emailLabel = new JLabel("EMAIL");
        emailLabel.setBounds(70, 145, 70, 20);
        contentPane.add(emailLabel);

        /* PW JLabel */
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(70, 225, 70, 20);
        contentPane.add(pwLabel);

        /* PW CHECK JLabel */
        JLabel pwCheckLabel = new JLabel("PW CHECK");
        pwCheckLabel.setBounds(70, 305, 70, 20);
        contentPane.add(pwCheckLabel);
    }

    public void clickCheckEmailBtn() {
        checkEmailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDao dao = UserDao.getInstance();
                User user = new User(emailTextField.getText(), pwTextField.getText());

                if (dao.checkEmail(user) == 0) {
                    JOptionPane.showMessageDialog(null, "EMAIL이 중복됩니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "사용 가능한 EMAIL입니다.");
                    checkEmail = true;
                }
            }
        });
    }

    public void clickRegisterCompleteBtn() {
        registerCompleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                UserDao dao = UserDao.getInstance();
                User user = new User(emailTextField.getText(), pwTextField.getText());

                if (emailTextField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "EMAIL을 입력해 주세요.");
                } else if (pwTextField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "PW를 입력해 주세요.");
                } else if (pwCheckTextField.getText().trim().length() == 0) {
                    JOptionPane.showMessageDialog(null, "PW CHECK을 입력해 주세요.");
                } else if (!(pwTextField.getText().trim().equals(pwCheckTextField.getText().trim()))) {
                    JOptionPane.showMessageDialog(null, "PW와 PW CHECK가 다릅니다.");
                } else {
                    if (checkEmail) {
                        if (dao.save(user) == 1) {
                            JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "CHECK를 눌러 EMAIL 중복 확인을 하십시오.");
                    }
                }
            }
        });
    }
}