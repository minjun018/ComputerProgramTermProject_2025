package user;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.time.LocalDate;

public class CalendarPanel extends JPanel {
    Calendar cal = Calendar.getInstance();
    LocalDate now = LocalDate.now();
    int currentYear = now.getYear();
    int currentMonth = now.getMonthValue();

    int realYear = cal.get(Calendar.YEAR);
    int realMonth = cal.get(Calendar.MONTH) + 1;
    int realDay = cal.get(Calendar.DAY_OF_MONTH);

    JLabel label;

    public CalendarPanel(JLabel label) {
        this.label = label;
        this.setLayout(new GridLayout(6, 7));
        this.setBackground(Color.WHITE);
        drawCalendar();
    }

    public void prevMonth() {
        currentMonth--;
        if(currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        }
        drawCalendar();
    }

    public void nextMonth() {
        currentMonth++;
        if(currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        drawCalendar();
    }

    private void drawCalendar() {
        label.setText(currentYear + ". " + currentMonth);
        this.removeAll();

        Calendar cal2 = Calendar.getInstance();
        cal2.set(currentYear, currentMonth - 1, 1);
        int startDay = cal2.get(Calendar.DAY_OF_WEEK);
        cal2.add(Calendar.DATE, - (startDay - 1));

        for (int i = 0; i < 42; i++) {
            int y = cal2.get(Calendar.YEAR);
            int m = cal2.get(Calendar.MONTH) + 1;
            int d = cal2.get(Calendar.DAY_OF_MONTH);
            int dayOfWeek = cal2.get(Calendar.DAY_OF_WEEK);

            JPanel p1 = new JPanel(new BorderLayout()) {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.setColor(Color.LIGHT_GRAY);
                    g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);
                }
            };
            p1.setBackground(Color.WHITE);

            JLabel dLabel = new JLabel(String.valueOf(d));
            dLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
            dLabel.setBorder(BorderFactory.createEmptyBorder(5, 7, 0, 0));

            if (dayOfWeek == Calendar.SUNDAY) {
                dLabel.setForeground(Color.RED);
            } else if (dayOfWeek == Calendar.SATURDAY) {
                dLabel.setForeground(Color.BLUE);
            }

            if (m != currentMonth) {
                dLabel.setForeground(Color.LIGHT_GRAY);
                p1.setBackground(new Color(250, 250, 250));
            }

            if (y == realYear && m == realMonth && d == realDay) {
                p1.setBackground(new Color(255, 255, 224));
                dLabel.setForeground(Color.BLUE);
            }
            p1.add(dLabel, BorderLayout.NORTH);
            this.add(p1);
            cal2.add(Calendar.DATE, 1);
        }
        this.revalidate();
        this.repaint();
    }
}