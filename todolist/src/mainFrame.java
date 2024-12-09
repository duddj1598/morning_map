import org.json.simple.parser.*;
import org.json.simple.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

class Week{
	public static String get_week(String date){
		//date가 무슨 요일인지 반환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		String day = localDate.getDayOfWeek().toString();
		return day;
	}
	public static String get_day(String date){
		//오늘이 며칠인지 반환
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate localDate = LocalDate.parse(date, formatter);
		String day = localDate.getDayOfMonth() + "일";
		return day;
	}
	public static String get_now(){
		//오늘이 며칠인지 반환
		LocalDate localDate = LocalDate.now();
		String day = String.valueOf(localDate.getDayOfMonth());
		return day;
	}
}

public class mainFrame extends JFrame {
	private String[] todolist; // 클래스 필드로 선언
	private JCheckBox[] checkBox; // 체크박스를 동적으로 관리하기 위한 필드
	private String id;

	mainFrame(String id) throws IOException, ParseException {
		super("모닝맵");
		this.id = id;
		todolist tdl = new todolist(id); // 'todolist' 객체
		todolist = tdl.getTodolist(id);   // 초기화

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(530, 800);
		setLocationRelativeTo(null);

		JPanel nPanel = new JPanel();
		nPanel.setLayout(null);
		nPanel.setPreferredSize(new Dimension(500, 740)); // 전체 길이

		// JScrollPane으로 감싸기
		JScrollPane scrollPane = new JScrollPane(nPanel);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		scrollPane.getVerticalScrollBar().setUnitIncrement(16); // 스크롤 속도 조정
		setContentPane(scrollPane);

		// 앱 이름
		JLabel name = new JLabel("모닝맵");
		name.setFont(new Font("Gothic", Font.BOLD, 30));
		name.setSize(200, 50);
		name.setLocation(210, 20);
		nPanel.add(name);

		//날씨 정보
		JLabel weather = new JLabel("날씨 정보");
		weather.setFont(new Font("Gothic",Font.BOLD, 20));
		weather.setSize(100, 30);
		weather.setLocation(260, 100);
		api weatherapi = new api();
		String [] weatherinfo = weatherapi.get_weather();
		String temperature = weatherapi.get_temperature();
		String [] N_temperature = weatherapi.getN_temperature();
		String[] X_temperature = weatherapi.getX_temperature();
		String [] weatherimg = weatherapi.get_weatherimg();
		JLabel [] weatherinfoLabel = new JLabel[7];
		ImageIcon [] weatherimgIcon = new ImageIcon[7];
		JLabel [] weatherimgLabel = new JLabel[7];
		for (int i=0; i<7; i++){
			weatherimgLabel[i] = new JLabel();
			weatherimgIcon[i] = new ImageIcon(weatherimg[i]);
		}
		weatherinfoLabel[0] = new JLabel("현재 날씨 : " + weatherinfo[0]);
		Label temperatureLabel = new Label("현재 온도 : " + temperature);
		//날씨 이미지 추가
		weatherimgLabel[0].setIcon(new ImageIcon(weatherimg[0]));
		weatherinfoLabel[0].setSize(200, 30);
		weatherinfoLabel[0].setLocation(200, 150);
		temperatureLabel.setSize(200, 30);
		temperatureLabel.setLocation(200, 180);
		weatherimgLabel[0].setSize(100, 100);
		weatherimgLabel[0].setLocation(240, 210);
		nPanel.add(weatherinfoLabel[0]);
		nPanel.add(temperatureLabel);
		nPanel.add(weatherimgLabel[0]);

		//날씨 패널
		JPanel weatherPanel = new JPanel();
		weatherPanel.setLayout(new GridLayout(1, 7, 5, 5));
		weatherPanel.setSize(500, 150);
		weatherPanel.setLocation(5, 300);

		for (int i = 1; i < 7; i++) {
			JPanel dayPanel = new JPanel();
			dayPanel.setBackground(Color.LIGHT_GRAY);

			//i일 후 29일(금) 형식으로 출력
			String date = LocalDate.now().plusDays(i).toString();
			String week = Week.get_week(date);
			String day = Week.get_day(date);
			switch (week){
				case "MONDAY":
					week = "월";
					break;
				case "TUESDAY":
					week = "화";
					break;
				case "WEDNESDAY":
					week = "수";
					break;
				case "THURSDAY":
					week = "목";
					break;
				case "FRIDAY":
					week = "금";
					break;
				case "SATURDAY":
					week = "토";
					break;
				case "SUNDAY":
					week = "일";
					break;
			}
			JLabel dayLabel = new JLabel(day + "(" + week + ")");
			JLabel iconLabel = new JLabel(weatherimgIcon[i]);
			JLabel maxTempLabel = new JLabel("최고: " + X_temperature[i] + "°C");
			JLabel minTempLabel = new JLabel("최저: " + N_temperature[i] + "°C");

			dayPanel.add(dayLabel, BorderLayout.NORTH);
			dayPanel.add(iconLabel, BorderLayout.CENTER);
			dayPanel.add(maxTempLabel, BorderLayout.SOUTH);
			dayPanel.add(minTempLabel, BorderLayout.AFTER_LAST_LINE);

			weatherPanel.add(dayPanel);
		}

		nPanel.add(weatherPanel);

		//뉴스 패널
		// 뉴스 패널
		JPanel newsPanel = new JPanel();
		newsPanel.setLayout(new GridLayout(1, 3, 10, 10)); // 3개의 뉴스 항목을 가로로 배치
		newsPanel.setSize(500, 200); // 패널 크기 설정
		newsPanel.setLocation(5, 470); // 위치 설정
		newsPanel.setBackground(new Color(173, 216, 230)); // 배경색 설정

		// 뉴스 제목과 URL 가져오기
		String[] news_titles = new String[2];
		String[] news_urls = new String[2];
		String[] news_images = new String[2];

		news_api newsApi = new news_api();
		news_titles = newsApi.get_news_title();
		news_urls = newsApi.get_news_url();
		news_images = newsApi.get_news_img();

		// 뉴스 항목 추가
		for (int i = 0; i < news_titles.length; i++) {
			JPanel newsItem = new JPanel();
			newsItem.setLayout(new BorderLayout());
			newsItem.setBorder(BorderFactory.createLineBorder(Color.GRAY, 1)); // 테두리 설정

			// 이미지 라벨
			JLabel imageLabel = new JLabel();
			imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
			ImageIcon icon = new ImageIcon(new java.net.URL(news_images[i]));
			Image scaledImage = icon.getImage().getScaledInstance(150, 150, Image.SCALE_SMOOTH);
			imageLabel.setIcon(new ImageIcon(scaledImage));
			newsItem.add(imageLabel, BorderLayout.CENTER);

			// 제목 라벨
			JLabel titleLabel = new JLabel(news_titles[i]);
			titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
			titleLabel.setPreferredSize(new Dimension(150, 40));
			newsItem.add(titleLabel, BorderLayout.SOUTH);

			// 클릭 이벤트 추가
			final String url = news_urls[i];
			newsItem.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					try {
						Desktop.getDesktop().browse(new URI(url));
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}

				@Override
				public void mouseEntered(MouseEvent e) {
					newsItem.setCursor(new Cursor(Cursor.HAND_CURSOR));
				}
			});

			newsPanel.add(newsItem); // 뉴스 항목 패널에 추가
		}

// 뉴스 패널을 메인 패널에 추가
		nPanel.add(newsPanel);


		// ToDoList 섹션
		JLabel todoLabel = new JLabel("ToDoList");
		todoLabel.setFont(new Font("Gothic", Font.BOLD, 20));
		todoLabel.setSize(100, 30);
		todoLabel.setLocation(20, 700);
		nPanel.add(todoLabel);

		// ToDoList 추가 버튼
		JButton todoAddButton = new JButton("추가");
		todoAddButton.setSize(40, 25);
		todoAddButton.setLocation(110, 703);
		nPanel.add(todoAddButton);

		// ToDoList 목록 및 완료 체크박스 추가
		checkBox = new JCheckBox[todolist.length];
		nPanel.setPreferredSize(new Dimension(500, 740 + 30 * todolist.length)); // 전체 길이
		boolean[] done = tdl.getdone(id);
		for (int i = 0; i < todolist.length; i++) {
			//done이 true이면 체크박스에 체크
			checkBox[i] = new JCheckBox(todolist[i]);
			checkBox[i].setSize(300, 25);
			checkBox[i].setLocation(20, 740 + 30 * i);
			nPanel.add(checkBox[i]);
			if (done[i]){
				checkBox[i].setSelected(true);
			}

			// 체크박스 이벤트 추가
			int finalI = i;
			checkBox[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (checkBox[finalI].isSelected()) {
						tdl.checkTodolist(finalI); // 체크 시 'done' true
					} else {
						tdl.uncheckTodolist(finalI); // 체크 해제 시 'done' false
					}
				}
			});
		}

		// 추가 버튼 클릭 이벤트
		todoAddButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String title = JOptionPane.showInputDialog("할 일을 입력하세요.");
				//user.json다시 불러와서 todolist 갱신
				if (title != null) {
					tdl.addTodolist(title); // 새 할 일 추가
                    try {
                        todolist = tdl.getTodolist(id); // 갱신된 할 일 목록 가져오기
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    } catch (ParseException ex) {
                        throw new RuntimeException(ex);
                    }
                    // 기존 체크박스 제거
					for (JCheckBox cb : checkBox) {
						nPanel.remove(cb);
					}

					// 새로운 체크박스 추가
					checkBox = new JCheckBox[todolist.length];
					nPanel.setPreferredSize(new Dimension(500, 740 + 30 * todolist.length)); // 전체 길이
					try {
						boolean[] done_new = tdl.getdone(id); // 예외 발생 가능 코드
						for (int i = 0; i < todolist.length; i++) {
							checkBox[i] = new JCheckBox(todolist[i]);
							checkBox[i].setSize(300, 25);
							checkBox[i].setLocation(20, 740 + 30 * i);
							nPanel.add(checkBox[i]);
							if (done_new[i]) {
								checkBox[i].setSelected(true);
							}

							int finalI = i;
							checkBox[i].addActionListener(new ActionListener() {
								@Override
								public void actionPerformed(ActionEvent e) {
									if (checkBox[finalI].isSelected()) {
										tdl.checkTodolist(finalI); // 체크 시 'done' true
									} else {
										tdl.uncheckTodolist(finalI); // 체크 해제 시 'done' false
									}
								}
							});
						}
					} catch (IOException | ParseException ex) {
						ex.printStackTrace(); // 예외를 콘솔에 출력
						JOptionPane.showMessageDialog(nPanel, "할 일을 갱신하는 중 문제가 발생했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
					}

					// UI 갱신
					nPanel.revalidate();
					nPanel.repaint();
				}
			}
		});

		// 로그아웃 버튼
		JButton signOutButton = new JButton("로그아웃");
		signOutButton.setSize(90, 25);
		signOutButton.setLocation(425, 40);
		nPanel.add(signOutButton);

		signOutButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// 로그아웃 버튼 클릭 시 로그인 창으로 전환
				setVisible(false);
				new loginFrame().setVisible(true);
			}
		});

		// 시간 표시
		JLabel timeLabel = new JLabel();
		timeLabel.setSize(100, 30);
		timeLabel.setLocation(450, 10);
		nPanel.add(timeLabel);

		Thread timeThread = new Thread(() -> {
			while (true) {
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
				String currentTime = sdf.format(new Date());

				SwingUtilities.invokeLater(() -> timeLabel.setText(currentTime));

				try {
					Thread.sleep(1000); // 1초마다 갱신
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		timeThread.start();
	}
}
