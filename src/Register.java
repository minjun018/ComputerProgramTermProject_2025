import admin.UserManage;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

class Register extends JFrame {
    Register() {
        String[] major = {"컴퓨터융합학부", "---"};

        JCheckBox isad = new JCheckBox("관리자 확인용");
        JLabel id = new JLabel("아이디");
        JLabel pw = new JLabel("패스워드");
        JLabel mj = new JLabel("학과 선택");
        JButton rg = new JButton("회원가입");
        JTextField id_text = new JTextField(18);
        JTextField pw_text = new JTextField(18);
        JComboBox<String> mjr = new JComboBox<>(major); // 전공에 따른 강의실 선택


        JPanel p1 = new JPanel();
        JPanel p2 = new JPanel();
        JPanel p3 = new JPanel();

        p1.add(id);
        p1.add(id_text);
        p2.add(pw);
        p2.add(pw_text);
        p3.add(mj);
        p3.add(mjr);

        rg.addActionListener(e -> {
            String a = id_text.getText();
            String b = pw_text.getText();
            Boolean c = isad.isSelected() ? true : false;
            int d = mjr.getSelectedIndex();

            try {
                UserManage.register(a, b, c, d);
            } catch (IOException x) {
                System.out.println("오류 발생");
            }
        });

        setTitle("회원가입");
        setLayout(new GridLayout(0, 1, 0, 2));
        add(isad);
        add(p1);
        add(p2);
        add(p3);
        add(rg);

        pack();
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
}