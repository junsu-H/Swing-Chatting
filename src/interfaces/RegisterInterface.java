package interfaces;

import javax.swing.*;

public interface RegisterInterface {
    public JPanel contentPane = new JPanel();
    public JTextField emailTextField = new JTextField();
    public JPasswordField pwTextField = new JPasswordField();
    public JPasswordField pwCheckTextField = new JPasswordField();
    public JButton registerCompleteBtn = new JButton("Register");
    public JButton checkEmailBtn = new JButton("CHECK");

    public void createTextField();
    public void createLabel();
    public void clickCheckEmailBtn();
    public void clickRegisterCompleteBtn();
}
