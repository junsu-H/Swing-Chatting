package gui;

import client.VoiceClient;
import com.mysql.cj.xdevapi.Client;
import server.VoiceServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class VoiceGui extends JFrame {

    private final JButton clientStartBtn = new JButton("Voice Client Start");
    private final JButton clientStopBtn = new JButton("Voice Client Stop");
    public JPanel contentPane =  new JPanel();
    public VoiceGui(){
        super("Voice");
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        setSize(200, 200);
        setLocationRelativeTo(null);

        clientStartBtn.setBounds(10, 25, 170, 50);
        contentPane.add(clientStartBtn);

        clientStopBtn.setBounds(10, 75, 170, 50);
        contentPane.add(clientStopBtn);


        clickClientStartBtn();
        clickClientStopBtn();
        setVisible(true);
    }

    public void clickClientStartBtn() {
        clientStartBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            if (clientStartBtn.getText() == "Voice Client Start") {
                                new VoiceClient();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public void clickClientStopBtn() {
        clientStopBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                EventQueue.invokeLater(new Runnable() {
                    public void run() {
                        try {
                            VoiceClient.sendVoiceThread.stop();
                            VoiceClient.receiveVoiceThread.stop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    public static void main(String[] args) {
        new VoiceGui();
    }
}
