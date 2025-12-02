package user;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Reserve1 {
    int month;
    int date;
    int startt;
    int endt;
    int people;
    String s;

    Reserve1(int a, int b, int c, int d, int e, String f){
        this.month = a;
        this.date = b;
        this.startt = c;
        this.endt = d;
        this.people = e;
        this.s = f;
    }
}

class ReservationU extends JPanel {
    String[] time = {"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};

    JLabel rs = new JLabel("선택");
    // JLabel rs2 = new JLabel("시간");
// JLabel rs3 = new JLabel("인원 선택");
    JButton block = new JButton("이전");
    JButton block2 = new JButton("다음");
    JButton cancel = new JButton("예약 취소");
    JTextArea reason = new JTextArea(2, 22);

    JPanel pn1 = new JPanel();
    JPanel pn2 = new JPanel();
    JPanel rv1 = new JPanel();
    JPanel rv2 = new JPanel();
    JPanel rv3 = new JPanel();

    JRadioButton selectRadio = new JRadioButton(); // 선택 라디오
    //JComboBox room = new JComboBox();
    JComboBox startTime = new JComboBox(time);
    JComboBox endTime = new JComboBox(time);
    //JOptionPane alert = new JOptionPane();
    JTextField peopleCount = new JTextField("2", 3);

    static ArrayList<Reserve1> rsv = new ArrayList<>();
    public static void setRvList() throws IOException{
        String s;
        BufferedReader br = new BufferedReader(new FileReader("src/rs.txt"));
        while((s = br.readLine())!=null) {
            StringTokenizer stk = new StringTokenizer(s);
            int a = Integer.parseInt(stk.nextToken());
            int b = Integer.parseInt(stk.nextToken());
            int c = Integer.parseInt(stk.nextToken());
            int d = Integer.parseInt(stk.nextToken());
            int e = Integer.parseInt(stk.nextToken());
            String f = stk.nextToken();

            rsv.add(new Reserve1(a, b, c, d, e, f));
        }
    }

    JPanel reservationNow(int a, int b, int c, int d, int e, String f) throws IOException {
        JPanel jp1 = new JPanel();
        setRvList();

        if (!f.equals("예약됨")) {
            jp1.setLayout(new GridLayout(0,4, 200, 0));
            jp1.add(new JLabel(time[c]+" ~ "+time[d]));
            jp1.add(new JLabel(e+"명"));
            jp1.add(new JLabel("취소 사유 : "+f));
            return jp1;

        } else {
            jp1.setLayout(new GridLayout(0,4, 200, 0));
            jp1.add(new JLabel(time[c]+" ~ "+time[d]));
            jp1.add(new JLabel(e+"명"));
            JButton cc = new JButton("예약됨"); cc.setEnabled(false);
            jp1.add(cc);
            return jp1;
        }
    }


    ReservationU(int month, int date) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0,0,100,0));

        rv1.setLayout(new BorderLayout());
        rv1.setBorder(BorderFactory.createTitledBorder(month+"월 "+date+"일 기본 설정"));

        pn1.add(selectRadio);
        pn1.add(new JLabel(" "));
        pn1.add(startTime);
        pn1.add(new JLabel("~"));
        pn1.add(endTime);
        rv1.add(pn1, "West");

        pn2.setLayout(new BorderLayout());
        reason.setLineWrap(true);

        pn2.add(reason, BorderLayout.SOUTH);
        pn2.setBorder(BorderFactory.createEmptyBorder(0,200,10,400));

        rv1.add(pn2);
        rv1.add(block, "East");
        add(rv1, "North");

        block.addActionListener(e-> {
            if (startTime.getSelectedIndex()>=endTime.getSelectedIndex()) System.out.println("시간 설정이 잘못 되었습니다.");
        });

        JPanel peoplePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 0));
        peopleCount.setHorizontalAlignment(JTextField.CENTER);
        peoplePanel.add(peopleCount); peoplePanel.add(new JLabel("명"));
        pn1.add(peoplePanel);

        rv1.add(pn1, BorderLayout.CENTER);
        add(rv1, "North");

        try {
            rv2.setLayout(new GridLayout(0,1));
            rv2.add(reservationNow(month, date,0,2,4,"예약됨"));
            rv2.add(reservationNow(month, date,3,5,4,"학과 행사"));
            rv2.setBorder(BorderFactory.createTitledBorder(month+"월 "+date+"일 예약 현황"));
            add(rv2, "South");
        } catch(IOException e) { System.out.println("오류발생"); }

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        Color btnColor = new Color(64, 64, 64);

        block.setPreferredSize(new Dimension(100, 40));
        block2.setPreferredSize(new Dimension(100, 40));
        btnPanel.add(block);
        btnPanel.add(block2);

        rv3.add(btnPanel, BorderLayout.CENTER);
        add(rv2, "Center");
        add(rv3, BorderLayout.SOUTH);
    }
}