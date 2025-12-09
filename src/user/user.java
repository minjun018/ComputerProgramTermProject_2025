package user;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class user extends JFrame {
    JLabel label;
    CalendarPanel calendarPanel;

    public user() {
        setTitle("강의실 예약 - 날짜 선택"); // 제목 변경
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // ... (상단 패널, 달력 패널 코드는 기존과 동일하므로 생략, 그대로 두면 됨) ...
        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
        Font font = new Font("맑은 고딕", Font.BOLD, 25);

        JButton leftBt = new JButton("<");
        // ... (버튼 디자인 코드 생략) ...
        JLabel label = new JLabel();
        // ...
        JButton rightBt = new JButton(">");
        // ...

        topPanel.add(leftBt); topPanel.add(label); topPanel.add(rightBt);
        add(topPanel, BorderLayout.NORTH);

        JPanel Main = new JPanel(new BorderLayout());
        calendarPanel = new CalendarPanel(label); // 생성자 수정 필요하면 확인
        Main.add(calendarPanel, BorderLayout.CENTER);
        add(Main, BorderLayout.CENTER);

        leftBt.addActionListener(e -> calendarPanel.prevMonth());
        rightBt.addActionListener(e -> calendarPanel.nextMonth());
        // ... (여기까지 기존과 동일) ...


        // === [여기가 핵심 수정 부분!] 하단 패널 및 버튼 ===
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 20));
        bottomPanel.setBackground(new Color(255, 240, 245));

        // 콤보박스 데이터 생성 (기존 코드 활용)
        String[] yearData = new String[11];
        for(int i=0; i<11; i++) yearData[i] = (2020+i)+"";
        String[] monthData = new String[12];
        for(int i=0; i<12; i++) monthData[i] = (i+1)+"";
        String[] dayData = new String[31];
        for(int i=0; i<31; i++) dayData[i] = (i+1)+"";

        JComboBox<String> yearCombo = new JComboBox<>(yearData);
        JComboBox<String> monthCombo = new JComboBox<>(monthData);
        JComboBox<String> dayCombo = new JComboBox<>(dayData);
        JButton reserveBt = new JButton("예약하러 가기"); // 버튼 텍스트 변경

        // 현재 날짜로 초기화
        Calendar now = Calendar.getInstance();
        yearCombo.setSelectedItem(String.valueOf(now.get(Calendar.YEAR)));
        monthCombo.setSelectedItem(String.valueOf(now.get(Calendar.MONTH) + 1));
        dayCombo.setSelectedItem(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        // [중요] 예약 버튼 클릭 시 ReservationU 창 띄우기
        reserveBt.addActionListener(e -> {
            try {
                int year = Integer.parseInt((String) yearCombo.getSelectedItem());
                int month = Integer.parseInt((String) monthCombo.getSelectedItem());
                int day = Integer.parseInt((String) dayCombo.getSelectedItem());

                // 1. 새 창(JFrame)을 만든다.
                JFrame rvFrame = new JFrame(month + "월 " + day + "일 예약");
                rvFrame.setSize(800, 600); // 적당한 크기 설정
                rvFrame.setLocationRelativeTo(null); // 화면 중앙에 띄우기

                // 2. ReservationU 패널을 생성해서 넣는다.
                // (이때 ReservationU 내부에서 소켓 연결이 일어남!)
                ReservationU rvPanel = new ReservationU(year,month, day);
                rvFrame.add(rvPanel);

                // 3. 창을 보여준다.
                rvFrame.setVisible(true);

                // (선택사항) 기존 달력 창을 닫고 싶으면 dispose(); 를 쓰면 됨.
                // 지금은 예약 창을 따로 띄우는 방식으로 했어.

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
            }
        });

        // 하단 패널에 추가
        bottomPanel.add(yearCombo); bottomPanel.add(new JLabel("년"));
        bottomPanel.add(monthCombo); bottomPanel.add(new JLabel("월"));
        bottomPanel.add(dayCombo); bottomPanel.add(new JLabel("일"));
        bottomPanel.add(reserveBt);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}