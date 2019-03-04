//加好友时的用户信息窗口

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PersonInfo implements ActionListener {
    private Icon welcome = null;
    private JLabel account;
    private JLabel name;
    private JLabel qianming;
    private JLabel sex;

    String url = null;
    String num = null;

    private PersonInfo() {

        JFrame jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);
        jframe.getContentPane().setBackground(Color.WHITE);
        jframe.getContentPane().setLayout(null);

        JLabel lblNewLabel = new JLabel(InfoImage.InfoImage());
        lblNewLabel.setBounds(150, 20, 100, 96);
        jframe.getContentPane().add(lblNewLabel);

        account = new JLabel("账号：");
        account.setFont(new Font("宋体", Font.PLAIN, 16));
        account.setBounds(100, 140, 274, 25);
        jframe.getContentPane().add(account);

        name = new JLabel("昵称：");
        name.setFont(new Font("宋体", Font.PLAIN, 16));
        name.setBounds(100, 190, 274, 25);
        jframe.getContentPane().add(name);

        qianming = new JLabel("签名：");
        qianming.setFont(new Font("宋体", Font.PLAIN, 16));
        qianming.setBounds(100, 240, 274, 25);
        jframe.getContentPane().add(qianming);

        sex = new JLabel("性别：");
        sex.setFont(new Font("宋体", Font.PLAIN, 16));
        sex.setBounds(100, 290, 274, 25);
        jframe.getContentPane().add(sex);

        JButton button = new JButton("加为好友");
        button.setBounds(150, 328, 93, 23);
        button.addActionListener(this);
        jframe.getContentPane().add(button);

        jframe.setTitle("用户信息!");
        jframe.setVisible(true);
        jframe.setSize(400, 400);
        jframe.setLocationRelativeTo(jframe.getOwner());
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setResizable(false);
        result();
    }

    private void result() {//获取用户信息，设置搜索界面的结果
        /*
        for (Search s : search_info) {
            if (s.getTel().equals(num)) {
                account.setText("帐户：" + s.getTel());
                name.setText("昵称：" + s.getAccount());
                qianming.setText("签名：" + s.getQianming());
                sex.setText("性别：" + s.getSex());
            }
        }

        */
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

    }


}


