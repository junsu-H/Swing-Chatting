package interfaces;

import javax.swing.*;

public interface RegisterInterface {
    JPanel contentPane = new JPanel();
    JTextField emailTextField = new JTextField();
    JPasswordField pwPasswordField = new JPasswordField();
    JPasswordField pwCheckPasswordField = new JPasswordField();
    JButton registerCompleteBtn = new JButton("Register");
    JButton checkEmailBtn = new JButton("CHECK");

    void createTextField();
    void createLabel();
    void clickCheckEmailBtn();
    void clickRegisterCompleteBtn();
}
