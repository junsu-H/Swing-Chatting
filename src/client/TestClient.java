package client;//package Client;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class TestClient {
	// 프레임 선언
	public static JFrame jFrame = new JFrame("Chat");

	// Swing에서의 텍스트 영역.. 스크롤바가 존재하지 않는다.
	JTextArea textArea = new JTextArea("", 0, 0); // 스크롤바 없음

	// Swing에서 스크롤바를 넣기 위해 아래와 같이 사용한다.
	JScrollPane jScrollPane = new JScrollPane(textArea); // 스클롤바 만들기

	// 내용을 입력할 입력창
	JTextField inputText = new JTextField();

	// 소켓
	static Socket socket;
	static String name;
	static boolean checkCondition = false;
	public static String nameFunc() {
		jFrame.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				super.windowClosing(e);
				sendMessage(name + "님이 퇴장하셨습니다.　　　");
			}

		});

		try {
			socket = new Socket("127.0.0.1", 9632);
		} catch (Exception e) {
			System.out.println("클라이언트 소켓 생성 에러");
		}

		// 이름설정
		do {
			name = JOptionPane.showInputDialog(jFrame, "이름은?");
			sendMessage(name);
			try {
//				Whisper w = (Whisper) Naming.lookup("rmi://localhost:1099/whisper");
//				checkCondition = w.getCheckValue();
			}catch(Exception nbe) {
				System.out.println("error ----->"+nbe);
			}
		}while (name == null || name.length() < 2 || checkCondition );

		sendMessage(name + "님이 입장하셨습니다.");
		jFrame.setTitle(name + "님의 채팅창");
		return name;
	}

	public TestClient() {

		textArea.setEnabled(false);
		textArea.setFocusable(false);

		// 텍스트 영역 추가
		jFrame.add(jScrollPane, "Center");
		jFrame.add(inputText, "South");

		// 프레임 크기 및 보이기 설정
		jFrame.setSize(600, 600);
		jFrame.setVisible(true);

		// swing에만 있는 X버튼 클릭시 종료
		jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		nameFunc();


		new Thread() {
			@Override
			public void run() {
				BufferedReader bufferedReader = null;

				try {
					bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while (true) {
						// 클라이언트로부터 메시지 입력받음
						String clientMessage = bufferedReader.readLine();

						if (clientMessage != null && clientMessage.trim().length() > 0) {
							textArea.append(clientMessage + "\n");
							textArea.setCaretPosition(textArea.getText().length());
						}
					}
				} catch (Exception e) {
					System.out.println(e + "=>clinet run");
					e.printStackTrace();
				} finally {
					// 읽어오기 종료
					try {
						bufferedReader.close();
						textArea.append("\n**읽어오기 종료**\n");
					} catch (IOException e) {
						System.out.println(e);
					}
				}
			}

		}.start(); // 스레드 실행

		inputText.addKeyListener(new KeyAdapter() {

			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == 10) {
					if (inputText.getText().trim().length() > 0) {
						sendMessage(name + " : " + inputText.getText().trim());
						inputText.setText(null);
					}
				}
			}

		});

	}

	public static void sendMessage(String message) {
		try {
			// 클라이언트에게 보내기 위한 준비
			BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

			//최초 메세지 전송 시 Server에 Client의 name 전송

			bufferedWriter.write(message);
			bufferedWriter.newLine();
			bufferedWriter.flush();

		} catch (Exception sendError) {
			System.out.println("메시지 전송 에러 : " + sendError);
		}

	}

	public static void main(String[] args) throws IOException {
		new TestClient();
	}
}