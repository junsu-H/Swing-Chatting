package server;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class VoiceServer {
    private static final int PORT = 9725;
    ServerSocket serverSocket = null;
    Socket socket = null;

    public VoiceServer() {
        String HOST = "";
        try {
            HOST = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }

        try {
            serverSocket = new ServerSocket(PORT);
            System.out.println("VoiceServer Ready");
            while (true) {
                socket = serverSocket.accept();
                System.out.println("-------------------start VoiceServer-------------------");
                new Thread(new receiveVoiceThread()).start();
                new Thread(new sendVoiceThread()).start();
            }
        } catch (IOException e) {
            System.out.println("socket:" + e);
            Logger.getLogger(VoiceServer.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /*
     * float sampleRate: 샘플수,
     * int sampleSizeInBits: 비트당샘플사이즈,
     * int channels: 채널, 1 - 모노, 2 - 스테레오
     * boolean signed:부호여부,
     * boolean bigEndian:빅엔디안여부
     * ref: https://stackoverflow.com/tags/javasound/info
     */

    public static AudioFormat getAudioFormat() {
        float sampleRate = 44100.0F;
        int sampleSizeInBits = 16;
        int channels = 2;
        boolean signed = true;
        boolean bigEndian = true;
        return new AudioFormat(sampleRate, sampleSizeInBits, channels, signed, bigEndian);
    }

    class receiveVoiceThread implements Runnable {
        @Override
        public void run() {
            AudioFormat format = null;
            /*
            https://docs.oracle.com/javase/7/docs/api/javax/sound/sampled/DataLine.Info.html#:~:text=public%20static%20class%20DataLine.Info,sizes%20of%20its%20internal%20buffer
            */

            DataLine.Info info;
            SourceDataLine sourceDataLine;

            InputStream inputStream;
            try {
                inputStream = socket.getInputStream();

                int eof = 0;
                byte[] buffer = new byte[65536];
                format = getAudioFormat();
                info = new DataLine.Info(SourceDataLine.class, format);
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceDataLine.open(format);
                sourceDataLine.start();

                while (true) {
                    eof = inputStream.read(buffer, 0, buffer.length);
                    if (eof == -1){
                        break;
                    }
                    else if (eof > 0) {
//                        System.out.println(eof + " ");
                        sourceDataLine.write(buffer, 0, eof);
                        buffer = new byte[65536];
                    }
                }
                sourceDataLine.stop();
                sourceDataLine.close();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    class sendVoiceThread implements Runnable {
        @Override
        public void run() {

            OutputStream outputStream;
            try {
                outputStream = socket.getOutputStream();

                TargetDataLine line;
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);
                if (!AudioSystem.isLineSupported(info)) {
                    System.err.println("Your hardware is not supported");
                    System.exit(1);
                }
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();
                System.out.println("Start Capturing...");
                byte buffer[] = new byte[(int) format.getSampleRate() * format.getFrameSize()];
                while (true) {
                    int count = line.read(buffer, 0, buffer.length);
                    if (count > 0) {
                        outputStream.write(buffer, 0, count);
                    }
                }
            } catch (IOException ex) {
                System.out.println("send i/o:" + ex);
                System.out.println("But Still Run VoiceServer");
            } catch (LineUnavailableException ex) {
                System.out.println("send i/o:" + ex);
                System.out.println("But Still Run VoiceServer");
            }
        }
    }
}