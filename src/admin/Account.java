package admin;

import user.user;

import java.io.*;
import java.util.*;

public class Account {
    String id;
    String pw;
    boolean manage; // 학생인지 과사무실분들인지 구분하는 인자, 관리자면 true 사용자면 false
    int uni_number;  // 단과대 구분

    public Account(String id, String pw, boolean manage, int uni_number) { //admin.Account 생성자
        this.id = id;
        this.pw = pw;
        this.manage = manage;
        this.uni_number = uni_number;
    }
}


class UserManage {
    static ArrayList<Account> member = new ArrayList<>();


    public static void setUserList() throws IOException {
        member.clear();
        File file = new File("src/idpw.txt");
        if (!file.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader("src/idpw.txt"));) {
            String s;

            while((s = br.readLine())!=null){
                StringTokenizer stk = new StringTokenizer(s);
                String a = stk.nextToken();
                String b = stk.nextToken();
                Boolean c = Boolean.valueOf(stk.nextToken());
                int d = Integer.parseInt(stk.nextToken());
                member.add(new Account(a,b,c,d));
            }
        }
    }

    public static void register(String id, String pw, Boolean isAdmin, int major, Register rg) throws IOException { //유저등록 메소드
        setUserList();
        Set<String> idset = new HashSet<>();
        member.forEach(m -> { idset.add(m.id); });

        if (!idset.add(id)) System.out.println("아이디 겹침");
        else {
            try (FileWriter fw = new FileWriter("src/idpw.txt",true);
                 BufferedWriter bw = new BufferedWriter(fw);) {
                if (new File("src/idpw.txt").length() == 0) bw.write(id+"\t"+pw+"\t"+isAdmin+"\t"+major);
                else bw.write("\r\n"+id+"\t"+pw+"\t"+isAdmin+"\t"+major);

                rg.dispose();
            }
            System.out.println(id + " 님이 등록하셨습니다.");
        }

    }


    public static void Login(String id, String pw, login_Window lw) throws IOException {
        setUserList();
        HashMap<String, Account> mem = new HashMap<>();
        member.forEach(m -> { mem.put(m.id, m); });

        try {
            if (mem.get(id).pw.equals(pw)) {
                System.out.println("login");
                lw.dispose();
                if(mem.get(id).manage) {
                    new Admin(mem.get(id));
                }
                else if(!mem.get(id).manage){
                    new user();
                }
            } else throw new Exception();

        } catch (Exception e) {
            System.out.println("아이디와 비밀번호가 일치하지 않습니다."); // 아이디 일치하는데 비번 일치하지 않을 때, 없는 계정일 때
        }
    }
}