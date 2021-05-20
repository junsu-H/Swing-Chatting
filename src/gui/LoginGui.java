package gui;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

public class LoginGui extends JFrame {
    public LoginGui() {
        super("LOGIN");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null);

        JPanel contentPane = new JPanel();

        /* location */
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        /* ID JLabel */
        JLabel idLabel = new JLabel("ID");
        idLabel.setBounds(50, 45, 45, 45);
        contentPane.add(idLabel);

        /* PW JLabel */
        JLabel pwLabel = new JLabel("PW");
        pwLabel.setBounds(50, 90, 45, 45);
        contentPane.add(pwLabel);

        /* ID JTextField */
        JTextField idTextField = new JTextField();
        idTextField.setBounds(150, 50, 175, 35);
        contentPane.add(idTextField);
        idTextField.setColumns(10);

        /* PW JTextField */
        JTextField pwTextField = new JTextField();
        pwTextField.setColumns(10);
        pwTextField.setBounds(150, 100, 175, 35);
        contentPane.add(pwTextField);

        /* LOGIN JButton */
        JButton loginBtn = new JButton("LOGIN");
        loginBtn.setBounds(115, 155, 115, 35);
        contentPane.add(loginBtn);

        /* REGISTER JButton */
        JButton registerBtn = new JButton("REGISTER");
        registerBtn.setBounds(230, 155, 115, 35);
        contentPane.add(registerBtn);

        setVisible(true);

        /* Action on registerBtn click */
        registerBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                /* create registerGui */
                RegisterGui registerGui = new RegisterGui();
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
