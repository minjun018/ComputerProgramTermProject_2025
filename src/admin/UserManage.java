package admin;

import admin.Account;
import admin.admin;
import user.user;

import java.io.*;
import java.util.*;

public class UserManage {
    static ArrayList<Account> member = new ArrayList<>();


    public static void setUserList() throws IOException {
        member.clear();
        File file = new File("src/idpw.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/idpw.txt"));) {
            String s;

            while ((s = br.readLine()) != null) {
                StringTokenizer stk = new StringTokenizer(s);
                String a = stk.nextToken();
                String b = stk.nextToken();
                Boolean c = Boolean.valueOf(stk.nextToken());
                int d = Integer.parseInt(stk.nextToken());
                member.add(new Account(a, b, c, d));
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
            return false; // [변경] 실패했으니까 false 던져주기
        } else {
            try (FileWriter fw = new FileWriter("src/idpw.txt", true);
                 BufferedWriter bw = new BufferedWriter(fw);) {

                if (new File("src/idpw.txt").length() == 0)
                    bw.write(id + "\t" + pw + "\t" + isAdmin + "\t" + major);
                else
                    bw.write("\r\n" + id + "\t" + pw + "\t" + isAdmin + "\t" + major);

                // [삭제] rg.dispose(); -> 여기서 창을 끄면 안 됨!
            }
            System.out.println(id + " 님이 등록하셨습니다.");
            return true; // [변경] 성공했으니까 true 던져주기
        }

    }


    public static Account Login(String id, String pw) throws IOException {
        setUserList();
        HashMap<String, Account> mem = new HashMap<>();
        member.forEach(m -> {
            mem.put(m.id, m);
        });
        if (mem.containsKey(id) && mem.get(id).pw.equals(pw)) {
            System.out.println("로그인 성공");
            return mem.get(id); // 성공한 계정 정보를 던져줌
        } else {
            System.out.println("로그인 실패");
            return null; // 실패하면 null
        }
    }
}