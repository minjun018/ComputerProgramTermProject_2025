package admin;

import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.StringTokenizer;

class Reserve {
    int year, month, date;
    int startt, endt;
    int people;
    int rnum;
    String reason;

    Reserve(int year, int month, int date, int startt, int endt, int people, int rnum, String reason) {
        this.year = year;
        this.month = month;
        this.date = date;
        this.startt = startt;
        this.endt = endt;
        this.people = people;
        this.rnum = rnum;
        this.reason = reason;
    }
    public String toFileString() {
        String ymd = String.format("%04d%02d%02d", year, month, date);
        return String.format("%s %d %d %d %d %s", ymd, startt, endt, people, rnum, reason);
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
    JButton block = new JButton("차단 실행");
    JTextArea reason = new JTextArea(2, 22);
    JComboBox<String> roomCmb = new JComboBox<>(roomList);
    JComboBox<String> startTime = new JComboBox<>(time);
    JComboBox<String> endTime = new JComboBox<>(time);

    JPanel rv1 = new JPanel();
    JPanel rv2 = new JPanel();

    static ArrayList<Reserve> rsv = new ArrayList<>();

    private final String FILE_PATH = "src/admin/rs.txt";

    public void loadReservations() {
        rsv.clear();
        File file = new File(FILE_PATH);
        if(!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if(line.trim().isEmpty()) continue;

                StringTokenizer stk = new StringTokenizer(line);
                if(stk.countTokens() < 6) continue;

                String ymd = stk.nextToken();
                int year = Integer.parseInt(ymd.substring(0, 4));
                int month = Integer.parseInt(ymd.substring(4, 6));
                int date = Integer.parseInt(ymd.substring(6, 8));

                int start = Integer.parseInt(stk.nextToken());
                int end = Integer.parseInt(stk.nextToken());
                int pp = Integer.parseInt(stk.nextToken());
                int rIdx = Integer.parseInt(stk.nextToken());

                StringBuilder sb = new StringBuilder();
                while(stk.hasMoreTokens()) {
                    sb.append(stk.nextToken()).append(" ");
                }
                String reasonStr = sb.toString().trim();

                rsv.add(new Reserve(year, month, date, start, end, pp, rIdx, reasonStr));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveReservation(Reserve newRv) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            if (new File(FILE_PATH).length() > 0) bw.newLine();
            bw.write(newRv.toFileString());
            rsv.add(newRv);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "파일 저장 중 오류 발생");
        }
    }

    public void rewriteAllReservations() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (int i = 0; i < rsv.size(); i++) {
                bw.write(rsv.get(i).toFileString());
                if (i < rsv.size() - 1) {
                    bw.newLine();
                }
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "파일 삭제/저장 중 오류 발생");
            e.printStackTrace();
        }
    }

    public boolean isConflict(int rIdx, int y, int m, int d, int sTime, int eTime) {
        for (Reserve r : rsv) {
            if (r.year == y && r.month == m && r.date == d && r.rnum == rIdx) {
                if (r.startt < eTime && r.endt > sTime) {
                    return true;
                }
            }
        }
        return false;
    }

    Reservation(int year, int month, int date) {
        setLayout(new BorderLayout());

        loadReservations();

        rv1.setLayout(new BorderLayout());
        rv1.setBorder(BorderFactory.createTitledBorder(year+"년 "+month+"월 "+date+"일 예약 관리"));

        JPanel inputPanel = new JPanel();
        inputPanel.add(roomCmb);
        inputPanel.add(new JLabel("시작:"));
        inputPanel.add(startTime);
        inputPanel.add(new JLabel("종료:"));
        inputPanel.add(endTime);
        rv1.add(inputPanel, "West");

        JPanel reasonPanel = new JPanel(new BorderLayout());
        reason.setLineWrap(true);
        reasonPanel.add(new JLabel("사유:"), BorderLayout.NORTH);
        reasonPanel.add(new JScrollPane(reason), BorderLayout.CENTER);
        rv1.add(reasonPanel, "Center");
        rv1.add(block, "East");

        add(rv1, "North");
        rv2.setLayout(new GridLayout(0, 1));
        JScrollPane scrollPane = new JScrollPane(rv2);
        scrollPane.setBorder(BorderFactory.createTitledBorder("현재 예약 현황"));
        add(scrollPane, "Center");
        updateReservationListPanel(year, month, date);

        block.addActionListener(e -> {
            int sIdx = startTime.getSelectedIndex();
            int eIdx = endTime.getSelectedIndex();
            int rIdx = roomCmb.getSelectedIndex();
            String reasonText = reason.getText();

            if (sIdx >= eIdx) {
                JOptionPane.showMessageDialog(this, "종료 시간이 시작 시간보다 빨라야 합니다.");
                return;
            }
            if (reasonText.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "사유를 입력해주세요.");
                return;
            }

            if (isConflict(rIdx, year, month, date, sIdx, eIdx)) {
                JOptionPane.showMessageDialog(this, "이미 예약/차단된 시간대입니다.");
                return;
            }

            Reserve newRv = new Reserve(year, month, date, sIdx, eIdx, 0, rIdx, reasonText);
            saveReservation(newRv);

            JOptionPane.showMessageDialog(this, "차단되었습니다.");
            updateReservationListPanel(year, month, date);
            reason.setText("");
        });
    }

    void updateReservationListPanel(int y, int m, int d) {
        rv2.removeAll();

        for (Reserve r : rsv) {
            if (r.year == y && r.month == m && r.date == d) {
                JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
                row.add(new JLabel("[" + roomList[r.rnum] + "]"));
                row.add(new JLabel(time[r.startt] + " ~ " + time[r.endt]));
                row.add(new JLabel("- " + r.reason + "  "));

                JButton deleteBtn = new JButton("삭제");
                deleteBtn.setPreferredSize(new Dimension(60, 20));
                deleteBtn.addActionListener(e -> {
                    int confirm = JOptionPane.showConfirmDialog(this, "이 예약을 삭제하시겠습니까?", "삭제 확인", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        rsv.remove(r);
                        rewriteAllReservations();
                        updateReservationListPanel(y, m, d);
                    }
                });
                row.add(deleteBtn);
                rv2.add(row);
            }
        }
        rv2.revalidate();
        rv2.repaint();
    }
}