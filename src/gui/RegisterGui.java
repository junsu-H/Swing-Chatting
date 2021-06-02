package gui;

import dao.MemberDao;
import models.Member;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterGui extends JFrame {
    JPanel contentPane = null;
    JTextField emailTextField = null;
    JPasswordField pwTextField = null;
    JPasswordField pwCheckTextField = null;
    JButton registerCompleteBtn = null;
    JButton checkEmailBtn = null;
    private boolean checkEmail = false;

    public RegisterGui() {
        super("REGISTER");
        setSize(450, 500);
        setLocationRelativeTo(null);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        createLabel();
        createTextField();

        /* Register Complete JButton */
        registerCompleteBtn = new JButton("Register");
        registerCompleteBtn.setBounds(180, 360, 140, 35);
        contentPane.add(registerCompleteBtn);

        /* CHECK EMAIL JButton */
        checkEmailBtn = new JButton("CHECK");
        checkEmailBtn.setBounds(350, 140, 80, 35);
        contentPane.add(checkEmailBtn);

        setVisible(true);

        clickCheckEmailBtn();
        clickRegisterCompleteBtn();
    }
    public void createTextField(){
        /* EMAIL JTextField */
        emailTextField = new JTextField();
        emailTextField.setColumns(10);
        emailTextField.setBounds(160, 140, 185, 35);
        contentPane.add(emailTextField);

        /* PW JPasswordField */
        pwTextField = new JPasswordField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(160, 220, 185,35);
        contentPane.add(pwTextField);

        /* PW CHECK JPasswordField */
        pwCheckTextField = new JPasswordField();
        pwCheckTextField.setColumns(10);
        pwCheckTextField.setBounds(160, 300, 185,35);
        contentPane.add(pwCheckTextField);
    }

    public void createLabel(){
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

    public void clickCheckEmailBtn(){
        checkEmailBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Member member = new Member(emailTextField.getText(), pwTextField.getText());

                MemberDao dao = MemberDao.getInstance();
                if (dao.checkEmail(member) == 0){
                    JOptionPane.showMessageDialog(null, "EMAIL이 중복됩니다.");
                } else {
                    JOptionPane.showMessageDialog(null, "사용 가능한 EMAIL입니다.");
                    checkEmail = true;
                }
            }
        });
    }

    public void clickRegisterCompleteBtn(){
        registerCompleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Member member = new Member(emailTextField.getText(), pwTextField.getText());

                MemberDao dao = MemberDao.getInstance();

                if (emailTextField.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "EMAIL이 빈칸입니다.");
                }
                else if (pwTextField.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "PW가 빈칸입니다.");
                }
                else if (pwCheckTextField.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "PW CHECK가 빈칸입니다.");
                }
                else if (!(pwTextField.getText().trim().equals(pwCheckTextField.getText().trim()))){
                    JOptionPane.showMessageDialog(null, "PW와 PW CHECK가 다릅니다.");
                }
                else {
                    if (checkEmail){
                        if  (dao.save(member) == 1){
                            JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "CHECK를 눌러 중복 확인을 하십시오.");
                    }
                }
            }
        });
    }


    public static void main(String[] args) {
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
}
