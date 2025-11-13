import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
public class admin extends JFrame{
    String[] month_Data = {"1","2","3","4","5","6","7","8","9","10","11","12"};
    public admin(){
        setTitle("admin");
        //4:3을 기준으로 size를 지정
        setSize(1600,1200);
        setLayout(new FlowLayout());
        ButtonGroup group = new ButtonGroup();
        JComboBox month = new JComboBox(month_Data);
        add(month);
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }


    public static void main(String[] args){
        login_user_Window po = new login_user_Window();
    }
}
class login_Window extends JFrame{//로그인창 코드
    public login_Window(){
        setTitle("로그인창");
        setSize(400,400);
        setLayout(new FlowLayout());
        //프레임안에 label을 넣는 패널하나 textfield를 넣는 panel하나 그리고 로그인 버튼이 들어갈 패널하나 총 3가지 패널로 구성
        JPanel login_label = new JPanel();
        JPanel login_Text = new JPanel();
        JPanel But = new JPanel();

        login_label.setLayout(new BorderLayout());
        login_Text.setLayout(new BorderLayout());
        JLabel id = new JLabel("아이디");
        JLabel pw = new JLabel("비밀번호");
        JTextField id_Text = new JTextField(20);
        JTextField pw_Text = new JTextField(20);
        JButton enter = new JButton("로그인");

        login_label.add(id,BorderLayout.NORTH);
        login_label.add(pw,BorderLayout.SOUTH);
        login_Text.add(id_Text,BorderLayout.NORTH);
        login_Text.add(pw_Text,BorderLayout.SOUTH);
        But.add(enter);
        enter.addActionListener(new ActionListener(){
           @Override
            public void actionPerformed(ActionEvent e){
               //로그인알고리즘 구현해야함
               new admin();
               login_Window.this.dispose();//프레임이 넘어가는것처럼 보이게 하는 명령어
           }
        });

        add(login_label);
        add(login_Text);
        add(But);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);//로그인창이 꺼져도 앱이 죽지않게 처리해주는 명령어
    }
}
