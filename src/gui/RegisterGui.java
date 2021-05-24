package gui;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import dao.MemberDao;
import models.Member;

public class RegisterGui extends JFrame {
    JPanel contentPane = null;
    JTextField nicknameTextField = null;
    JTextField pwTextField = null;
    JTextField pwCheckTextField = null;
    JButton registerCompleteBtn = null;

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

        setVisible(true);

        clickRegisterCompleteBtn();
}
    public void createTextField(){
        /* NICKNAME JTextField */
        nicknameTextField = new JTextField();
        nicknameTextField.setColumns(10);
        nicknameTextField.setBounds(160, 140, 185, 35);
        contentPane.add(nicknameTextField);

        /* PW JTextField */
        pwTextField = new JTextField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(160, 220, 185,35);
        contentPane.add(pwTextField);

        /* PW CHECK JTextField */
        pwCheckTextField = new JTextField();
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

        /* nickname JLabel */
        JLabel nicknameLabel = new JLabel("ID");
        nicknameLabel.setBounds(70, 145, 70, 20);
        contentPane.add(nicknameLabel);

        /* PW JLabel */
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(70, 225, 70, 20);
        contentPane.add(pwLabel);

        /* PW CHECK JLabel */
        JLabel pwCheckLabel = new JLabel("PW CHECK");
        pwCheckLabel.setBounds(70, 305, 70, 20);
        contentPane.add(pwCheckLabel);
    }

    public void clickRegisterCompleteBtn(){
        registerCompleteBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Member member = new Member(nicknameTextField.getText(), pwTextField.getText());

                MemberDao dao = MemberDao.getInstance();
                int save = 0;
                int checkNickname = dao.checkNickname(member);

                if (nicknameTextField.getText().trim().length() == 0){
                    JOptionPane.showMessageDialog(null, "ID가 빈칸입니다.");
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
                    if (checkNickname == 1){
                        save = dao.save(member);
                        if  (save == 1){
                            JOptionPane.showMessageDialog(null, "회원가입이 완료되었습니다.");
                        }
                    }
                    else {
                        JOptionPane.showMessageDialog(null, "nickname이 중복됩니다.");
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
