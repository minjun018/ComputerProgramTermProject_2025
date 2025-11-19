import java.util.*;
public class Account {
    String id;
    String pw;
    boolean manage;//학생인지 과사무실분들인지 구분하는 인자
    int uni_number;
    //관리자면 true 사용자면 false
    public static List<Account> member = new ArrayList<>();
    public Account(String id,String pw,boolean manage,int uni_number){//Account 생성자
        this.id = id;
        this.pw = pw;
        this.manage = manage;
        this.uni_number = uni_number;
    }
    public static void Register(Account person){//유저등록 메소드
        member.add(person);
        System.out.println(person.id+"님이 등록하셨습니다.");//디버깅용 프린트문
    }
}
