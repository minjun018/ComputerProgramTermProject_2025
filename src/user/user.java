package user;

import java.awt.*;
import javax.swing.*;
import java.util.*;

public class user extends JFrame {
    JLabel label;
    CalendarPanel calendarPanel;

    public user() {
        setTitle("강의실 예약");
        setSize(1600, 1000);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.setBackground(Color.DARK_GRAY);
        Font font = new Font("맑은 고딕", Font.BOLD, 25);

        JButton leftBt = new JButton("<");
        leftBt.setForeground(Color.WHITE);
        leftBt.setBackground(Color.DARK_GRAY);
        leftBt.setBorderPainted(false);
        leftBt.setFocusPainted(false);
        leftBt.setFont(font);

        label = new JLabel();
        label.setForeground(Color.WHITE);
        label.setFont(font);

        JButton rightBt = new JButton(">");
        rightBt.setForeground(Color.WHITE);
        rightBt.setBackground(Color.DARK_GRAY);
        rightBt.setBorderPainted(false);
        rightBt.setFocusPainted(false);
        rightBt.setFont(font);

        topPanel.add(leftBt);
        topPanel.add(label);
        topPanel.add(rightBt);
        add(topPanel, BorderLayout.NORTH);

        JPanel Main = new JPanel(new BorderLayout());

        JPanel panel1 = new JPanel(new GridLayout(1, 7));
        String[] date = {"SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"};
        for (String d : date) {
            JLabel l = new JLabel(d, JLabel.CENTER);
            l.setFont(new Font("맑은 고딕", Font.BOLD, 20));
            if(d.equals("SUN")) {
                l.setForeground(Color.RED);
            } else if(d.equals("SAT")) {
                l.setForeground(Color.BLUE);
            } else {
                l.setForeground(Color.DARK_GRAY);
            }
            panel1.add(l);
        }
        Main.add(panel1, BorderLayout.NORTH);

        calendarPanel = new CalendarPanel(label);
        Main.add(calendarPanel, BorderLayout.CENTER);

        add(Main, BorderLayout.CENTER);

        leftBt.addActionListener(e -> calendarPanel.prevMonth());
        rightBt.addActionListener(e -> calendarPanel.nextMonth());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 7, 20));
        bottomPanel.setBackground(new Color(255, 240, 245));

        String[] yearData = new String[11];
        for(int i = 0; i < yearData.length; i++) {
            yearData[i] = (2020 + i) + "";
        }

        String[] monthData = new String[12];
        for(int i = 0; i < monthData.length; i++) {
            monthData[i] = (i + 1) + "";
        }

        String[] dayData = new String[31];
        for(int i = 0; i < dayData.length; i++) {
            dayData[i] = (i + 1) + "";
        }

        JComboBox yearCombo = new JComboBox(yearData);
        JComboBox monthCombo = new JComboBox(monthData);
        JComboBox dayCombo = new JComboBox(dayData);
        JButton reserveBt = new JButton("예약");

        Calendar now = Calendar.getInstance();
        int y = now.get(Calendar.YEAR);
        int m = now.get(Calendar.MONTH) + 1;
        int d = now.get(Calendar.DAY_OF_MONTH);

        yearCombo.setSelectedItem(y + "");
        monthCombo.setSelectedItem(m + "");
        dayCombo.setSelectedItem(d + "");

        Font f = new Font("맑은 고딕", Font.BOLD, 18);

        yearCombo.setFont(f);
        yearCombo.setBackground(Color.WHITE);

        monthCombo.setFont(f);
        monthCombo.setBackground(Color.WHITE);

        dayCombo.setFont(f);
        dayCombo.setBackground(Color.WHITE);

        reserveBt.setFont(f);
        reserveBt.setBackground(Color.BLACK);
        reserveBt.setForeground(Color.WHITE);

        JLabel yearLabel = new JLabel("년");
        yearLabel.setFont(f);
        JLabel monthLabel = new JLabel("월");
        monthLabel.setFont(f);
        JLabel dayLabel = new JLabel("일");
        dayLabel.setFont(f);

        reserveBt.addActionListener(e -> {
            int year = Integer.parseInt((String) yearCombo.getSelectedItem());
            int month = Integer.parseInt((String) monthCombo.getSelectedItem());
            int day = Integer.parseInt((String) dayCombo.getSelectedItem());

            dispose();

        });

        bottomPanel.add(yearCombo);
        bottomPanel.add(yearLabel);

        bottomPanel.add(monthCombo);
        bottomPanel.add(monthLabel);

        bottomPanel.add(dayCombo);
        bottomPanel.add(dayLabel);

        bottomPanel.add(reserveBt);

        add(bottomPanel, BorderLayout.SOUTH);

        setVisible(true);
    }

    public static void main(String[] args) {
        new user();
    }
}