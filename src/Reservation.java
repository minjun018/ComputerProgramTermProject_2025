import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

class Reserve {
    int year;
    int month;
    int date;
    int startt;
    int endt;
    int people;
    int rnum;
    String s;

    Reserve(int year, int month, int date, int startt, int endt, int people, int rnum, String s) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.startt = startt;
        this.endt = endt;
        this.people = people;
        this.rnum = rnum;
        this.s = s;

    }
}

class Reservation extends JPanel {
    static String[] time = {"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30",
            "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00", "21:30", "22:00", "22:30", "23:00", "23:30"};

    static String[] roomList = {"공5101", "공5102", "공5103", "공5104", "공5105", "공5106", "공5107", "공5108", "공5109", "공5110", "공5111", "공5113", "공5114",
            "공5201", "공5202", "공5203", "공5204", "공5205", "공5206", "공5207", "공5208", "공5209", "공5210", "공5211", "공5213", "공5214", "공5215",
            "공5301", "공5302", "공5303", "공5304", "공5305", "공5306", "공5307", "공5308", "공5309", "공5310", "공5311", "공5313", "공5314", "공5315", "공5316", "공5317", "공5318", "공5319", "공5320",
            "공5401", "공5402", "공5403", "공5404", "공5405", "공5406", "공5407", "공5408", "공5409", "공5410", "공5411", "공5413", "공5414", "공5415", "공5416" };

    JLabel rs = new JLabel(" 조정 사유");
    JButton block = new JButton("예약 제한");
    JButton cancel = new JButton("예약 취소");
    JTextArea reason = new JTextArea(2, 22);
    JPanel pn1 = new JPanel();
    JPanel pn2 = new JPanel();
    JPanel rv1 = new JPanel();
    JPanel rv2 = new JPanel();
    JPanel rv3 = new JPanel();


    JComboBox<String> room = new JComboBox<>(roomList); // 강의실 선택 콤보박스
    JComboBox<String> startTime = new JComboBox<>(time);
    JComboBox<String> endTime = new JComboBox<>(time);
    JOptionPane alert = new JOptionPane();

    static ArrayList<Reserve> rsv = new ArrayList<>();
    static ArrayList<Reserve> rsvAct = new ArrayList<>();

    public static void setRvList() throws IOException{
        rsv.clear();
        String s, a;
        int year = 0; int month = 0; int date = 0; int start = 0;
        int end = 0; int totalpp = 0; int roomnum = 0;
        String st = null;

        try ( BufferedReader br = new BufferedReader(new FileReader("src/rs.txt")); ) {
            while((s = br.readLine())!=null) {
                StringTokenizer stk = new StringTokenizer(s);
                a = stk.nextToken();
                year = Integer.parseInt(a.substring(0, 4));
                month = Integer.parseInt(a.substring(4, 6));
                date = Integer.parseInt(a.substring(6, 8));
                start = Integer.parseInt(stk.nextToken());
                end = Integer.parseInt(stk.nextToken());
                totalpp = Integer.parseInt(stk.nextToken());
                roomnum = Integer.parseInt(stk.nextToken());
                st = stk.nextToken();
                while (true) {
                    st += " "+stk.nextToken(); }
            }
        } catch (Exception e) { rsv.add(new Reserve(year, month, date, start, end, totalpp, roomnum, st)); }
    }

    JPanel reservationNow(int room, int start, int end, int people, String s) throws IOException {
        JPanel jp1 = new JPanel();
        setRvList();

        if (!s.equals("예약됨")) {

            jp1.setLayout(new GridLayout(0,5, 200, 0));
            jp1.add(new JLabel(roomList[room]+""));
            jp1.add(new JLabel(time[start]+" ~ "+time[end]));
            jp1.add(new JLabel(people+"명"));
            jp1.add(new JLabel("취소 사유 : "+s));
            return jp1;
        } else {
            jp1.setLayout(new GridLayout(0,5, 200, 0));
            jp1.add(new JLabel(roomList[room]+""));
            jp1.add(new JLabel(time[start]+" ~ "+time[end]));
            jp1.add(new JLabel(people+"명"));
            JButton cc = new JButton("예약됨");    cc.setEnabled(false);
            jp1.add(cc);
            return jp1;
        }
    }

    Reservation(int year, int month, int date) {
        setLayout(new BorderLayout());
        setBorder(BorderFactory.createEmptyBorder(0,0,100,0));
        rv1.setLayout(new BorderLayout());
        rv1.setBorder(BorderFactory.createTitledBorder(year+"년 "+month+"월 "+date+"일 예약 조정"));
        pn1.add(room);
        pn1.add(startTime);
        pn1.add(endTime);
        rv1.add(pn1, "West");

        pn2.setLayout(new BorderLayout());
        reason.setLineWrap(true);
        pn2.add(rs, BorderLayout.NORTH);    pn2.add(reason, BorderLayout.SOUTH);
        pn2.setBorder(BorderFactory.createEmptyBorder(0,200,10,400));
        rv1.add(pn2);
        rv1.add(block, "East");
        add(rv1, "North");

        block.addActionListener(e-> {

            if (startTime.getSelectedIndex()>=endTime.getSelectedIndex()) System.out.println("시간 설정이 잘못되었습니다.");
            else {
                Calendar now = Calendar.getInstance();
                int y = now.get(Calendar.YEAR); int m = now.get(Calendar.MONTH)+1; int d = now.get(Calendar.DATE);
                int h = now.get(Calendar.HOUR_OF_DAY); int min = now.get(Calendar.MINUTE);
                Boolean checkhour = year == y && month == m && date == d && h > startTime.getSelectedIndex()/2+9;
                Boolean checkminute = year == y && month == m && date == d && h == startTime.getSelectedIndex()/2+9 && min > startTime.getSelectedIndex()%2*30;

                if (year < y || (year == y && month < m) || (year == y && month == m && date < d) || checkhour || checkminute) {
                    System.out.println("조정 불가 시간대입니다.");
                } else {}
            } // 미완
        });

        try {
            rv2.setLayout(new GridLayout(0,1));
            rv2.add(reservationNow(3, 3,2,4,"예약됨")); // TODO : 해당 년, 월 조건 작성 필요
            rv2.add(reservationNow(3, 3,5,4,"학과 행사"));
            rv2.setBorder(BorderFactory.createTitledBorder(year+"년 "+month+"월 "+date+"일 예약 현황"));
            add(rv2, "South");
        } catch(IOException e) { System.out.println("오류발생"); }
    }
}