import admin.AccountDTO;
import admin.admin;
import user.user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class login_Window extends JFrame {
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
            String id = id_Text.getText();
            String pw = pw_Text.getText();

            try {
                // 포트 9999로 변경
                Socket socket = new Socket("localhost", 9999);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                out.println("LOGIN|" + id + "|" + pw);

                String response = in.readLine();

                if (response != null && response.startsWith("SUCCESS")) {
                    String[] tokens = response.split("\\|");

                    String rId = tokens[1];
                    String rPw = tokens[2];
                    boolean rManage = Boolean.parseBoolean(tokens[3]);
                    int rUni = Integer.parseInt(tokens[4]);

                    AccountDTO accountDTO = new AccountDTO(rId, rPw, rManage, rUni);

                    dispose();

                    if (rManage) {
                        new admin(accountDTO);
                    } else {
                        new user();
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "아이디 또는 비밀번호가 틀렸습니다.");
                    socket.close();
                }
            } catch (Exception x) {
                x.printStackTrace();
                JOptionPane.showMessageDialog(this, "서버 연결 실패: " + x.getMessage());
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
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public static void main(String[] args){
        new login_Window();
    }
}