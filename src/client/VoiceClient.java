package client;

import server.VoiceServer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import static server.VoiceServer.getAudioFormat;

public class VoiceClient {
    Socket socket;

    public static void main(String[] args) {
        new VoiceClient();
    }

    public VoiceClient() {
        try {
            socket = new Socket("localhost", 9725);
        } catch (IOException e) {
            e.printStackTrace();
        }
        new Thread(new receiveThread()).start();
        new Thread(new sendThread()).start();

    }
    class receiveThread implements Runnable {
        @Override
        public void run() {
            AudioFormat format;
            DataLine.Info info;
            SourceDataLine sourceDataLine;

            InputStream inputStream;
            try {
                inputStream = socket.getInputStream();

                int eof;
                byte[] buffer = new byte[65536];
                format = getAudioFormat();
                info = new DataLine.Info(SourceDataLine.class, format);
                sourceDataLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceDataLine.open(format);
                sourceDataLine.start();
                while ((eof = inputStream.read(buffer, 0, buffer.length)) != -1) {
                    if (eof > 0) {
                        System.out.println(eof + " ");
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

    class sendThread implements Runnable {

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

                System.out.println("buffer length: " + buffer.length);
                while (true) {
                    int count = line.read(buffer, 0, buffer.length);
                    if (count > 0) {
                        outputStream.write(buffer, 0, count);
                    }
                }
            } catch (IOException ex) {
                System.out.println("send i/o:" + ex);
                Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
            } catch (LineUnavailableException ex) {
                System.out.println("send line:" + ex);
                Logger.getLogger(VoiceClient.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
	