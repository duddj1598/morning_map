import org.json.simple.*;
import org.json.simple.parser.*;
import java.awt.event.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import javax.swing.*;

public class loginFrame extends JFrame {
	String id;

	loginFrame() {
		super("로그인");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(400, 300);
		setVisible(true);
		setLocationRelativeTo(null);

		JPanel sPanel = new JPanel();
		setContentPane(sPanel);
		sPanel.setLayout(null);

		// 아이디
		JLabel lbl = new JLabel("아이디");
		lbl.setSize(100, 30);
		lbl.setLocation(50, 40);
		sPanel.add(lbl);

		// 아이디 텍스트 상자
		JTextField tf1 = new JTextField(10);
		tf1.setSize(200, 30);
		tf1.setLocation(110, 40);
		sPanel.add(tf1);

		// 비밀번호
		JLabel lbl2 = new JLabel("비밀번호");
		lbl2.setSize(100, 30);
		lbl2.setLocation(50, 90);
		sPanel.add(lbl2);

		// 비밀번호 텍스트 상자
		JTextField tf2 = new JTextField(10);
		tf2.setSize(200, 30);
		tf2.setLocation(110, 90);
		sPanel.add(tf2);

		// 로그인 버튼
		JButton btn = new JButton("로그인");
		btn.setSize(90, 30);
		btn.setLocation(90, 170);
		sPanel.add(btn);

		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 로그인 버튼 클릭 시 user.json에서 아이디와 비밀번호 확인
				JSONParser parser = new JSONParser();
				try {
					InputStream inputStream = getClass().getClassLoader().getResourceAsStream("user.json");
					if (inputStream == null) {
						throw new FileNotFoundException("Resource not found: user.json");
					}
					Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
					JSONObject jsonObject = (JSONObject) parser.parse(reader);
					id = tf1.getText();
					JSONObject user = (JSONObject) jsonObject.get(tf1.getText());
					if (user != null && user.get("password").equals(tf2.getText())) {
						// 로그인 성공 시 mainFrame으로 전환
						setVisible(false);
						new mainFrame(id).setVisible(true);
					} else {
						JOptionPane.showMessageDialog(null, "아이디 또는 비밀번호가 틀렸습니다.");
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		});

		// 회원가입 버튼
		JButton btn2 = new JButton("회원가입");
		btn2.setSize(90, 30);
		btn2.setLocation(200, 170);
		sPanel.add(btn2);

		btn2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				new signupFrame(loginFrame.this).setVisible(true);
			}
		});

		setVisible(true);
	}

	public static void main(String[] args) {
		new loginFrame();
	}
}
