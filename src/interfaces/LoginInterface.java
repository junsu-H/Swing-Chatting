package interfaces;

import models.User;

import javax.swing.*;

public interface LoginInterface {
    JPanel contentPane =  new JPanel();
    JTextField emailTextField = new JTextField();
    JPasswordField pwPasswordField = new JPasswordField();
    JLabel nicknameLabel = new JLabel("EMAIL");
    JLabel pwLabel = new JLabel("PW");
    JButton loginBtn = new JButton("LOGIN");
    JButton registerBtn = new JButton("REGISTER");

    void createTextField();
    void createLabel();
    void createButton();
    void clickLoginBtn(User user);
    void enterPwPasswordField(User user);
    void loginBtn(User user);
    void clickRegisterBtn();
}
