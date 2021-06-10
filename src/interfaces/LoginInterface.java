package interfaces;

import models.User;

import javax.swing.*;

public interface LoginInterface {
    public JPanel contentPane =  new JPanel();
    public JTextField emailTextField = new JTextField();
    public JPasswordField pwTextField = new JPasswordField();
    public JLabel nicknameLabel = new JLabel("EMAIL");
    public JLabel pwLabel = new JLabel("PW");
    public JButton loginBtn = new JButton("LOGIN");
    public JButton registerBtn = new JButton("REGISTER");

    public void createTextField();
    public void createLabel();
    public void createButton();
    public void clickLoginBtn(User user);
    public void enterPwTextField(User user);
    public void loginBtn(User user);
    public void clickRegisterBtn();

}
