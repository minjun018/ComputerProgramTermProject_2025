import com.toedter.calendar.JCalendar;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
class Admin extends JFrame{


    public Admin(Account account){
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


    public static void main(String[] args){
        new login_Window();
    }
}

class login_Window extends JFrame{//로그인창 코드
    public login_Window(){
        setTitle("로그인창");
        setSize(290,180);
        setLayout(new FlowLayout());
        JPanel login = new JPanel();
        JPanel login_text = new JPanel();
        JPanel pn1 = new JPanel();
        JPanel pn2 = new JPanel(new GridLayout(0,1,0,3));

        login.setLayout(new GridLayout(2, 1,0,7));
        login_text.setLayout(new GridLayout(2, 1,0,4));
        JLabel idl = new JLabel("아이디");
        JLabel pwl = new JLabel("비밀번호");
        JTextField id_Text = new JTextField(18);
        JTextField pw_Text = new JPasswordField(18);

        JButton enter = new JButton("로그인");
        enter.setBackground(Color.BLUE);
        enter.setForeground(Color.WHITE);
        JButton rg = new JButton("회원가입");
        rg.setBackground(Color.WHITE);
        pn2.add(enter); pn2.add(rg);

        login.add(idl);
        login_text.add(id_Text);
        login.add(pwl);
        login_text.add(pw_Text);
        pn1.add(login, BorderLayout.WEST); pn1.add(login_text, BorderLayout.EAST);

        ActionListener le = e->{
            String a = id_Text.getText();
            String b = pw_Text.getText();
            try { UserManage.Login(a,b,this); } catch (IOException x) { }
        };
        pw_Text.addActionListener(le);
        enter.addActionListener(le);

        rg.addActionListener(e -> {
            new Register();
        });

        add(pn1, BorderLayout.NORTH);
        add(pn2);

        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//로그인창이 꺼져도 앱이 죽지않게 처리해주는 명령어
    }
}

class Register extends JFrame {
    Register() {
        String[] major = {"컴퓨터융합학부", "---"};

        JCheckBox isad = new JCheckBox("관리자 확인용");
        JLabel id = new JLabel("아이디");
        JLabel pw = new JLabel("패스워드");
        JLabel mj = new JLabel("학과 선택");
        JButton rg = new JButton("회원가입");
        JTextField id_text = new JTextField(18);
        JTextField pw_text = new JTextField(18);
        JComboBox<String> mjr = new JComboBox<>(major); // 전공에 따른 강의실 선택


        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();

        p1.add(id);
        p1.add(id_text);
        p2.add(pw);
        p2.add(pw_text);
        p3.add(mj);
        p3.add(mjr);

        rg.addActionListener(e -> {
            String a = id_text.getText();
            String b = pw_text.getText();
            Boolean c = isad.isSelected()? true : false;
            int d = mjr.getSelectedIndex();

            try { UserManage.register(a,b,c,d,this); } catch(IOException x){ System.out.println("오류 발생"); }
        });

        setTitle("회원가입");
        setLayout(new GridLayout(0,1,0,2));
        add(isad);
        add(p1);
        add(p2);
        add(p3);
        add(rg);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}
