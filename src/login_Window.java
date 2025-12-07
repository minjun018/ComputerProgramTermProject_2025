import admin.Account;
import admin.UserManage;
import admin.admin;
import user.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.IOException;


public class login_Window extends JFrame {//로그인창 코드
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
            try {
                Account account = UserManage.Login(a, b);
                if (account != null) {
                    // 로그인 성공! 이제 여기서 창을 끈다.
                    this.dispose();

                    // 관리자 여부에 따라 화면 이동도 여기서 결정!
                    if (account.manage) {
                        new admin(account);
                    } else {
                        new user(); // user 생성자에 account 넘겨줘야 할 수도 있음
                    }
                } else {
                    // 로그인 실패 (null)
                    JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.");
                }
            } catch (IOException x) {
                return;
            }
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
    public static void main(String[] args){
        new login_Window();
    }
}

