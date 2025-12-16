package admin;
import com.toedter.calendar.JCalendar;

import javax.swing.*;
import java.util.*;
public class admin extends JFrame{


    public admin(AccountDTO accountDTO){
        setTitle("admin");
        setSize(1600,1000);

        ReservationCalendar rc = new ReservationCalendar();
        JCalendar cal = rc.cal;
        add(cal);
        add(new Reservation(rc.today_year, rc.today_month, rc.today_date), "South");

        cal.addPropertyChangeListener("calendar",e -> {
            rc.setCalendarColors(cal);
            this.getContentPane().removeAll();
            add(cal);
            try { Calendar selected = (Calendar)e.getNewValue();
                add(new Reservation(selected.get(Calendar.YEAR), (selected.get(Calendar.MONTH))+1, selected.get(Calendar.DATE)),
                        "South");
                this.getContentPane().revalidate(); } catch(Exception ex) {}
        });

        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
}

