//�Ӻ���ʱ���û���Ϣ����

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

        account = new JLabel("�˺ţ�");
        account.setFont(new Font("����", Font.PLAIN, 16));
        account.setBounds(100, 140, 274, 25);
        jframe.getContentPane().add(account);

        name = new JLabel("�ǳƣ�");
        name.setFont(new Font("����", Font.PLAIN, 16));
        name.setBounds(100, 190, 274, 25);
        jframe.getContentPane().add(name);

        qianming = new JLabel("ǩ����");
        qianming.setFont(new Font("����", Font.PLAIN, 16));
        qianming.setBounds(100, 240, 274, 25);
        jframe.getContentPane().add(qianming);

        sex = new JLabel("�Ա�");
        sex.setFont(new Font("����", Font.PLAIN, 16));
        sex.setBounds(100, 290, 274, 25);
        jframe.getContentPane().add(sex);

        JButton button = new JButton("��Ϊ����");
        button.setBounds(150, 328, 93, 23);
        button.addActionListener(this);
        jframe.getContentPane().add(button);

        jframe.setTitle("�û���Ϣ!");
        jframe.setVisible(true);
        jframe.setSize(400, 400);
        jframe.setLocationRelativeTo(jframe.getOwner());
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setResizable(false);
        result();
    }

    private void result() {//��ȡ�û���Ϣ��������������Ľ��
        /*
        for (Search s : search_info) {
            if (s.getTel().equals(num)) {
                account.setText("�ʻ���" + s.getTel());
                name.setText("�ǳƣ�" + s.getAccount());
                qianming.setText("ǩ����" + s.getQianming());
                sex.setText("�Ա�" + s.getSex());
            }
        }

        */
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {

    }


}


