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

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
        Font font = new Font("맑은 고딕", Font.BOLD, 25);

        JButton leftBt = new JButton("<");
        JLabel label = new JLabel();
        JButton rightBt = new JButton(">");

        topPanel.add(leftBt); topPanel.add(label); topPanel.add(rightBt);
        add(topPanel, BorderLayout.NORTH);

        JPanel Main = new JPanel(new BorderLayout());
        calendarPanel = new CalendarPanel(label);
        Main.add(calendarPanel, BorderLayout.CENTER);
        add(Main, BorderLayout.CENTER);

        leftBt.addActionListener(e -> calendarPanel.prevMonth());
        rightBt.addActionListener(e -> calendarPanel.nextMonth());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 20));
        bottomPanel.setBackground(new Color(255, 240, 245));

        String[] yearData = new String[11];
        for(int i=0; i<11; i++) yearData[i] = (2020+i)+"";
        String[] monthData = new String[12];
        for(int i=0; i<12; i++) monthData[i] = (i+1)+"";
        String[] dayData = new String[31];
        for(int i=0; i<31; i++) dayData[i] = (i+1)+"";

        JComboBox<String> yearCombo = new JComboBox<>(yearData);
        JComboBox<String> monthCombo = new JComboBox<>(monthData);
        JComboBox<String> dayCombo = new JComboBox<>(dayData);
        JButton reserveBt = new JButton("예약하러 가기");


        Calendar now = Calendar.getInstance();
        yearCombo.setSelectedItem(String.valueOf(now.get(Calendar.YEAR)));
        monthCombo.setSelectedItem(String.valueOf(now.get(Calendar.MONTH) + 1));
        dayCombo.setSelectedItem(String.valueOf(now.get(Calendar.DAY_OF_MONTH)));

        reserveBt.addActionListener(e -> {
            try {
                int year = Integer.parseInt((String) yearCombo.getSelectedItem());
                int month = Integer.parseInt((String) monthCombo.getSelectedItem());
                int day = Integer.parseInt((String) dayCombo.getSelectedItem());

                JFrame rvFrame = new JFrame(month + "월 " + day + "일 예약");
                rvFrame.setSize(800, 600);
                rvFrame.setLocationRelativeTo(null);


                ReservationU rvPanel = new ReservationU(year,month, day);
                rvFrame.add(rvPanel);

                rvFrame.setVisible(true);

                // (선택사항) 기존 달력 창을 닫고 싶으면 dispose(); 를 쓰면 됨.
                // 지금은 예약 창을 따로 띄우는 방식으로 했어.

            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "오류가 발생했습니다.");
            }
        });
        bottomPanel.add(yearCombo); bottomPanel.add(new JLabel("년"));
        bottomPanel.add(monthCombo); bottomPanel.add(new JLabel("월"));
        bottomPanel.add(dayCombo); bottomPanel.add(new JLabel("일"));
        bottomPanel.add(reserveBt);

        add(bottomPanel, BorderLayout.SOUTH);
        setVisible(true);
    }
}