import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.util.Base64;


class Register {
    private JTextField qianming;//放签名的文本条
    private JPasswordField password;//放密码的文本条
    private JTextField name; //放昵称的文本条
    private JTextField username;//放用户名的文本条
    private JTextField email;//放email的文本条
    private Base64.Encoder encoder = Base64.getEncoder();

    Register() {
        JFrame jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);
        jframe.getContentPane().setLayout(null);

        JLabel label = new JLabel("您的用户名：");//几个label
        label.setBounds(79, 44, 98, 15);
        jframe.getContentPane().add(label);

        JLabel label_1 = new JLabel("请设定密码：");
        label_1.setBounds(79, 94, 98, 15);
        jframe.getContentPane().add(label_1);

        JLabel label_2 = new JLabel("请输入签名：");
        label_2.setBounds(79, 144, 98, 15);

        jframe.setLocationRelativeTo(null);
        JButton button = new JButton("注册");//注册按钮
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
                    JOptionPane.showMessageDialog(null, "注册成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    jframe.dispose();
                } else {
                    JOptionPane.showMessageDialog(null, "注册失败\n可能是用户名已存在或服务器发生内部错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }

        });

        qianming = new JTextField();    //放签名的文本条
        qianming.setBounds(203, 141, 90, 21);
        jframe.getContentPane().add(qianming);
        qianming.setColumns(10);

        password = new JPasswordField();//放密码的文本条
        password.setEchoChar('*');
        password.setBounds(203, 91, 90, 21);
        jframe.getContentPane().add(password);

        JLabel lblTips = new JLabel(//label
                "Tips:用户名是您账号的唯一凭证，请认真填写并牢记");
        lblTips.setForeground(new Color(30, 144, 255));
        lblTips.setBounds(42, 327, 332, 23);

        jframe.setTitle("注册新用户!");
        jframe.getContentPane().add(lblTips);

        JLabel label_3 = new JLabel("请输入邮箱：");
        label_3.setBounds(79, 244, 98, 15);
        jframe.getContentPane().add(label_3);

        JLabel label_5 = new JLabel("请输入签名：");
        label_5.setBounds(79, 141, 90, 21);
        jframe.getContentPane().add(label_5);

        JLabel label_4 = new JLabel("新用户注册");
        label_4.setFont(new Font("宋体", Font.PLAIN, 18));
        label_4.setHorizontalAlignment(SwingConstants.CENTER);
        label_4.setBounds(145, 10, 99, 24);
        jframe.getContentPane().add(label_4);

        JLabel lblNewLabel = new JLabel("请输入昵称：");
        lblNewLabel.setBounds(79, 194, 98, 15);
        jframe.getContentPane().add(lblNewLabel);

        name = new JTextField();//放昵称的文本条
        name.setBounds(203, 191, 90, 21);
        jframe.getContentPane().add(name);
        name.setColumns(10);

        username = new JTextField();//放用户名的文本条
        username.setBounds(203, 44, 90, 21);
        jframe.getContentPane().add(username);
        username.setColumns(10);

        email = new JTextField();//放email的文本条
        email.setBounds(203, 240, 90, 21);
        jframe.getContentPane().add(email);
        email.setColumns(18);

        jframe.setVisible(true);
        jframe.setSize(400, 413);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

}
