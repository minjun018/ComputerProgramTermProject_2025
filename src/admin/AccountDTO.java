package admin;

public class AccountDTO {
    public String id;
    public String pw;
    public boolean manage; // 학생인지 과사무실분들인지 구분하는 인자, 관리자면 true 사용자면 false
    public int uni_number;  // 단과대 구분

    public AccountDTO(String id, String pw, boolean manage, int uni_number) { //admin.Account 생성자
        this.id = id;
        this.pw = pw;
        this.manage = manage;
        this.uni_number = uni_number;
    }
}

