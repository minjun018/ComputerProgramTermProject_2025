package admin;

import java.util.Calendar;
import com.toedter.calendar.JCalendar;
import java.awt.*;
import javax.swing.*;

public class ReservationCalendar {
    JCalendar cal = new JCalendar();
    Calendar today = Calendar.getInstance();
    int today_month = today.get(Calendar.MONTH)+1;
    int today_date = today.get(Calendar.DATE);
    int today_year = today.get(Calendar.YEAR);

    ReservationCalendar() {
        cal.setWeekOfYearVisible(false);
        cal.setBorder(BorderFactory.createEmptyBorder(30,30,175,30));
        setCalendarColors(cal);
    }

    void setCalendarColors(Component comp) {
        if (comp instanceof JButton) {
            JButton b = (JButton) comp;
            int btt;
            int year = cal.getCalendar().get(Calendar.YEAR);
            int month = cal.getCalendar().get(Calendar.MONTH)+1;
            int date = cal.getCalendar().get(Calendar.DATE);

            try {
                btt= Integer.valueOf(b.getText());
                if ((year == today_year)&&(month == today_month)) {
                    if (btt == date) {
                        b.setBackground(Color.gray);
                    } else if (btt < today_date) {
                        b.setBackground(new Color(255, 228,225));
                    } else if (btt > today_date) {
                        b.setBackground(new Color(240, 255, 240));
                    } else { b.setBackground(new Color(255,250,205)); }

                } else if ((year < today_year) || ((month < today_month)&&(year == today_year))) {
                    if (btt == date) {
                        b.setBackground(Color.gray);
                    } else { b.setBackground(new Color(255, 228,225)); }

                } else {
                    if (btt == date) {
                        b.setBackground(Color.gray);
                    } else { b.setBackground(new Color(240, 255, 240)); }
                }
            } catch (NumberFormatException e) { }

        } else if (comp instanceof Container) {
            for (Component child : ((Container) comp).getComponents()) {
                setCalendarColors(child);
            }
        }
    }

}