import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;

class LoginView {
    private JFrame jframe;
    private JTextField text_account;
    private JPasswordField text_password;


    LoginView() {
        jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);
        jframe.getContentPane().setBackground(Color.WHITE);
        jframe.getContentPane().setLayout(null);

        text_account = new JTextField();//输入账户的文本条
        text_account.setBounds(198, 157, 93, 21);
        jframe.getContentPane().add(text_account);
        text_account.setColumns(10);

        JLabel lblNewLabel = new JLabel("请输入用户名:");//两个label
        lblNewLabel.setBounds(95, 160, 100, 15);
        jframe.getContentPane().add(lblNewLabel);

        JLabel lblNewLabel_1 = new JLabel("请输入密码:");
        lblNewLabel_1.setBounds(95, 200, 80, 15);
        jframe.getContentPane().add(lblNewLabel_1);

        JButton btnNewButton = new JButton("登录");//登录按钮
        btnNewButton.setBounds(95, 280, 93, 23);
        btnNewButton.addActionListener(arg0 -> {
            String username = text_account.getText();
            String password = text_password.getText();
            JSONObject json = new JSONObject();
            json.put("type", "Login");
            json.put("username", username);
            json.put("password", password);
            Control.connect.SendJSON(json);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("Login")) {
                String status = result.getString("status");
                if (status.equals("success")) {
                    JOptionPane.showMessageDialog(null, "登录成功", "成功", JOptionPane.INFORMATION_MESSAGE);
                    Control.username = username;
                    Control.customer = new Customer();
                    jframe.dispose();
                } else if (status.equals("duplicated")) {
                    JOptionPane.showMessageDialog(null, "登录失败\n不允许重复登录。", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "登录失败\n可能是用户名或密码错误，或服务器发生内部错误。", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }

        });
        jframe.getContentPane().add(btnNewButton);

        JButton btnNewButton_1 = new JButton("注册");//注册按钮
        btnNewButton_1.setBounds(198, 280, 93, 23);
        jframe.getContentPane().add(btnNewButton_1);
        //注册的listener
        btnNewButton_1.addActionListener(arg0 -> {
            new Register();
        });

        text_password = new JPasswordField();//输入密码的文本条
        text_password.setBounds(198, 197, 93, 21);
        jframe.getContentPane().add(text_password);

        JLabel label = new JLabel(InfoImage.InfoImage());//一个label
        label.setBounds(95, 20, 196, 113);
        jframe.getContentPane().add(label);

        jframe.setVisible(true);
        jframe.setSize(400, 370);
        jframe.setResizable(false);
        jframe.setLocationRelativeTo(jframe.getOwner());
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

}
