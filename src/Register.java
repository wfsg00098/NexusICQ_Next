import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;


class Register {
    private JTextField qianming;//��ǩ�����ı���
    private JPasswordField password;//��������ı���
    private JTextField name; //���ǳƵ��ı���
    private JTextField username;//���û������ı���
    private JTextField email;//��email���ı���
    private Base64.Encoder encoder = Base64.getEncoder();

    Register() {
        JFrame jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);
        jframe.getContentPane().setLayout(null);

        JLabel label = new JLabel("�����û�����");//����label
        label.setBounds(79, 44, 98, 15);
        jframe.getContentPane().add(label);

        JLabel label_1 = new JLabel("���趨���룺");
        label_1.setBounds(79, 94, 98, 15);
        jframe.getContentPane().add(label_1);

        JLabel label_2 = new JLabel("������ǩ����");
        label_2.setBounds(79, 144, 98, 15);

        jframe.setLocationRelativeTo(null);
        JButton button = new JButton("ע��");//ע�ᰴť
        button.setBounds(79, 286, 214, 31);
        jframe.getContentPane().add(button);
        button.addActionListener(e -> {
            String user = username.getText();
            String pass = password.getText();
            String nickname = name.getText();
            String bio = qianming.getText();
            String em = email.getText();
            JSONObject json = new JSONObject();
            json.put("type", "Register");
            json.put("username", user);
            json.put("password", pass);
            json.put("nickname", encoder.encodeToString(nickname.getBytes()));
            json.put("email", encoder.encodeToString(em.getBytes()));
            json.put("bio", encoder.encodeToString(bio.getBytes()));
            Control.connect.SendJSON(json);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("Register_info")) {
                String status = result.getString("status");
                if (status.equals("success")) {
                    JOptionPane.showMessageDialog(null, "ע��ɹ�", "�ɹ�", JOptionPane.INFORMATION_MESSAGE);
                    jframe.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "ע��ʧ��\n�������û����Ѵ��ڻ�����������ڲ�����", "����", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
            }

        });

        qianming = new JTextField();    //��ǩ�����ı���
        qianming.setBounds(203, 141, 90, 21);
        jframe.getContentPane().add(qianming);
        qianming.setColumns(10);

        password = new JPasswordField();//��������ı���
        password.setEchoChar('*');
        password.setBounds(203, 91, 90, 21);
        jframe.getContentPane().add(password);

        JLabel lblTips = new JLabel(//label
                "Tips:�û��������˺ŵ�Ψһƾ֤����������д���μ�");
        lblTips.setForeground(new Color(30, 144, 255));
        lblTips.setBounds(42, 327, 332, 23);

        jframe.setTitle("ע�����û�!");
        jframe.getContentPane().add(lblTips);

        JLabel label_3 = new JLabel("���������䣺");
        label_3.setBounds(79, 244, 98, 15);
        jframe.getContentPane().add(label_3);

        JLabel label_5 = new JLabel("������ǩ����");
        label_5.setBounds(79, 141, 90, 21);
        jframe.getContentPane().add(label_5);

        JLabel label_4 = new JLabel("���û�ע��");
        label_4.setFont(new Font("����", Font.PLAIN, 18));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(145, 10, 99, 24);
        jframe.getContentPane().add(label_4);

        JLabel lblNewLabel = new JLabel("�������ǳƣ�");
        lblNewLabel.setBounds(79, 194, 98, 15);
        jframe.getContentPane().add(lblNewLabel);

        name = new JTextField();//���ǳƵ��ı���
        name.setBounds(203, 191, 90, 21);
        jframe.getContentPane().add(name);
        name.setColumns(10);

        username = new JTextField();//���û������ı���
        username.setBounds(203, 44, 90, 21);
        jframe.getContentPane().add(username);
        username.setColumns(10);

        email = new JTextField();//��email���ı���
        email.setBounds(203, 240, 90, 21);
        jframe.getContentPane().add(email);
        email.setColumns(18);

        jframe.setVisible(true);
        jframe.setSize(400, 413);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
