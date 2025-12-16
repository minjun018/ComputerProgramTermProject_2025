package admin;

import java.io.*;
import java.util.*;

public class UserManage {
    static ArrayList<AccountDTO> member = new ArrayList<>();


    public static void setUserList() throws IOException {
        member.clear();
        File file = new File("src/admin/idpw.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/admin/idpw.txt"));) {
            String s;

            while ((s = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(s);
                String a = stk.nextToken();
                String b = stk.nextToken();
                boolean c = Boolean.parseBoolean(stk.nextToken());
                int d = Integer.parseInt(stk.nextToken());
                member.add(new AccountDTO(a, b, c, d));
            }
        }
    }

    public static Boolean register(String id, String pw, Boolean isAdmin, int major) throws IOException { //유저등록 메소드
        setUserList();
        Set<String> idset = new HashSet<>();
        member.forEach(m -> {
            idset.add(m.id);
        });

        if (!idset.add(id)) {
            System.out.println("아이디 겹침");
            return false;
        } else {
            try (FileWriter fw = new FileWriter("src/admin/idpw.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);) {

                if (new File("src/idpw.txt").length() == 0)
                    bw.write(id + "\t" + pw + "\t" + isAdmin + "\t" + major);
                else
                    bw.write("\r\n" + id + "\t" + pw + "\t" + isAdmin + "\t" + major);
            }
            System.out.println(id + " 님이 등록하셨습니다.");
            return true;
        }

    }


    public static AccountDTO Login(String id, String pw) throws IOException {
        setUserList();
        HashMap<String, AccountDTO> mem = new HashMap<>();
        member.forEach(m -> {
            mem.put(m.id, m);
        });
        if (mem.containsKey(id) && mem.get(id).pw.equals(pw)) {
            System.out.println("로그인 성공");
            return mem.get(id);
        } else {
            System.out.println("로그인 실패");
            return null;
        }
    }
}