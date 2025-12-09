package user;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

// Reserve1 클래스는 그대로 둠
class Reserve1 {
    int month, date, startt, endt, people;
    String s;
    Reserve1(int a, int b, int c, int d, int e, String f){
        this.month = a; this.date = b; this.startt = c; this.endt = d; this.people = e; this.s = f;
    }
}

class ReservationU extends JPanel {
    int year, month, date;

    String[] time = {"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};

    JButton block = new JButton("이전");
    JButton block2 = new JButton("예약하기");
    JTextArea reason = new JTextArea(2, 22);

    JPanel pn1 = new JPanel();
    JPanel pn2 = new JPanel();
    JPanel rv1 = new JPanel();
    JPanel rv2 = new JPanel();
    JPanel rv3 = new JPanel();

    JRadioButton selectRadio = new JRadioButton();
    JComboBox<String> startTime = new JComboBox<>(time);
    JComboBox<String> endTime = new JComboBox<>(time);
    JTextField peopleCount = new JTextField("2", 3);

    static ArrayList<Reserve1> rsv = new ArrayList<>();

    // 생성자: 연도(y), 월(m), 일(d) 받기
    ReservationU(int y, int m, int d) {
        this.year = y;
        this.month = m;
        this.date = d;

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0, 0, 100, 0));
        setRvList();
        rv1.setLayout(new BorderLayout());
        rv1.setBorder(BorderFactory.createTitledBorder(year + "년 " + month + "월 " + date + "일 예약하기"));
        pn1.add(selectRadio); pn1.add(new JLabel(" "));
        pn1.add(startTime); pn1.add(new JLabel("~")); pn1.add(endTime);

        JPanel peoplePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        peoplePanel.add(peopleCount); peoplePanel.add(new JLabel("명"));

        pn1.add(peoplePanel);
        rv1.add(pn1, "West");
        pn2.setLayout(new BorderLayout());
        reason.setLineWrap(true);
        pn2.add(new JLabel("사유:"), BorderLayout.NORTH);
        pn2.add(reason, BorderLayout.CENTER);
        rv1.add(pn2, "Center");

        add(rv1, "North");


        rv2.setLayout(new GridLayout(0, 1));
        rv2.setBorder(BorderFactory.createTitledBorder("예약 현황"));
        refreshListPanel(month, date);
        add(new JScrollPane(rv2), "Center");
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        block.setPreferredSize(new Dimension(100, 40));
        block2.setPreferredSize(new Dimension(100, 40));
        block2.addActionListener(e -> {
            saveReservation(year, month, date);
        });


        block.addActionListener(e -> {
            Window w = SwingUtilities.getWindowAncestor(this);
            if(w != null) w.dispose();
        });

        btnPanel.add(block); btnPanel.add(block2);
        rv3.add(btnPanel);
        add(rv3, "South");
    }

    public static void setRvList() {
        rsv.clear();
        try (Socket socket = new Socket("localhost", 1224);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            out.println("LOAD"); // 서버에 요청

            String s;
            while ((s = in.readLine()) != null) {
                if (s.equals("EOF")) break;
                if (s.trim().isEmpty()) continue;
                try {
                    StringTokenizer stk = new StringTokenizer(s);
                    if (stk.countTokens() < 6) continue;
                    String ymd = stk.nextToken();
                    int month = Integer.parseInt(ymd.substring(4, 6));
                    int date = Integer.parseInt(ymd.substring(6, 8));
                    int start = Integer.parseInt(stk.nextToken());
                    int end = Integer.parseInt(stk.nextToken());
                    int people = Integer.parseInt(stk.nextToken());
                    int rIdx = Integer.parseInt(stk.nextToken()); // 방번호 (사용 안해도 읽긴 해야 함)
                    StringBuilder sb = new StringBuilder();
                    while (stk.hasMoreTokens()) sb.append(stk.nextToken()).append(" ");
                    rsv.add(new Reserve1(month, date, start, end, people, sb.toString().trim()));
                } catch (Exception e) {}
            }
        } catch (IOException e) {
            System.out.println("서버 연결 실패 (LOAD)");
        }
    }

    // 2. 서버에 예약 정보 저장하기 (SAVE) - 중복 제거하고 하나로 통합됨
    public void saveReservation(int y, int m, int d) {
        int sIdx = startTime.getSelectedIndex();
        int eIdx = endTime.getSelectedIndex();
        String pStr = peopleCount.getText();
        String rsn = reason.getText();

        // 유효성 검사
        if (sIdx >= eIdx) {
            JOptionPane.showMessageDialog(this, "종료 시간이 시작 시간보다 빨라야 합니다.");
            return;
        }
        if (rsn.trim().isEmpty()) rsn = "예약됨";

        int pp = 2;
        try { pp = Integer.parseInt(pStr); } catch (NumberFormatException e) { pp = 2; }

        // 포맷: YYYYMMDD start end people room reason
        String ymd = String.format("%04d%02d%02d", y, m, d);
        String msg = String.format("%s %d %d %d %d %s", ymd, sIdx, eIdx, pp, 0, rsn); // 0은 방번호 임시

        try (Socket socket = new Socket("localhost", 1224);
             PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {

            out.println("SAVE " + msg); // 서버에 전송
            JOptionPane.showMessageDialog(this, "예약되었습니다!");

            // 저장 후 화면 갱신
            setRvList();
            refreshListPanel(m, d);

        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "서버 연결 실패 (SAVE)");
            e.printStackTrace();
        }
    }

    // 3. 리스트 패널 다시 그리기
    void refreshListPanel(int month, int date) {
        rv2.removeAll(); // 기존 목록 지우기
        boolean hasData = false;

        for (Reserve1 r : rsv) {
            if (r.month == month && r.date == date) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT)); // FlowLayout으로 변경해서 보기 좋게
                row.add(new JLabel("[" + time[r.startt] + "~" + time[r.endt] + "]"));
                row.add(new JLabel(r.people + "명, "));
                row.add(new JLabel(r.s));
                rv2.add(row);
                hasData = true;
            }
        }

        if (!hasData) {
            rv2.add(new JLabel("예약된 일정이 없습니다."));
        }

        rv2.revalidate(); // 화면 갱신 필수!
        rv2.repaint();    // 화면 갱신 필수!
    }
}