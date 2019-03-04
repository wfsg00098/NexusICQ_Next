import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;


class ChatWindow {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private String username;
    private String nickname;
    private String email;
    private String bio;
    private Base64.Encoder encoder = Base64.getEncoder();
    private Base64.Decoder decoder = Base64.getDecoder();
    private JPanel plSend = new JPanel();
    private JTextArea sendMsg = new JTextArea();//发消息的文本栏
    private JButton btnSend = new JButton("发送消息");
    private JPanel parent_window = new JPanel();

    static JList userList;
    static DefaultListModel listModel;
    private JScrollPane js;
    private JButton button = new JButton("发送窗口抖动");
    JFrame jframe;


    ChatWindow(String target) {
        username = target;
        jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);
        jframe.getContentPane().setLayout(null);
        plSend.setBounds(10, 261, 556, 79);
        plSend.setBackground(Color.WHITE);
        plSend.setLayout(null);
        JScrollPane jsc = new JScrollPane(sendMsg);
        jsc.setLocation(0, 0);
        jsc.setSize(556, 79);
        sendMsg.setBounds(0, 0, 556, 79);
        sendMsg.setBackground(Color.WHITE);
        sendMsg.setForeground(Color.BLACK);
        sendMsg.setLineWrap(true);
        plSend.add(jsc);
        jframe.getContentPane().add(plSend);

        parent_window.setBackground(Color.WHITE);
        parent_window.setBounds(10, 10, 556, 241);
        jframe.getContentPane().add(parent_window);

        btnSend.setBounds(490, 340, 76, 36);
        jframe.getContentPane().add(btnSend);
        btnSend.setForeground(Color.BLACK);
        btnSend.setBackground(Color.WHITE);


        listModel = new DefaultListModel();
        parent_window.setLayout(null);
        userList = new JList(listModel);
        js = new JScrollPane(userList);
        js.setLocation(0, 0);
        js.setSize(556, 241);
        parent_window.add(js);
        userList.setBounds(0, 0, 556, 241);

        jframe.getContentPane().setBackground(new Color(175, 238, 238));
        button.setBounds(344, 340, 143, 36);
        jframe.getContentPane().add(button);
        jframe.setBackground(Color.WHITE);
        jframe.setSize(592, 415);
        jframe.setVisible(true);
        jframe.setLocationRelativeTo(jframe.getOwner());
        jframe.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                Customer.windows.remove(target);
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });
        Customer.windows.put(username, this);


        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", username);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            try {
                nickname = new String(decoder.decode(result.getString("nickname")), "GBK");
                bio = new String(decoder.decode(result.getString("bio")), "GBK");
                email = new String(decoder.decode(result.getString("email")), "GBK");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
        }
        jframe.setTitle(nickname);

        btnSend.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    JSONObject js = new JSONObject();
                    js.put("type", "TextMessage");
                    js.put("receiver", target);
                    String message = sendMsg.getText();
                    byte[] textByte = message.getBytes("GBK");
                    String encodedText = encoder.encodeToString(textByte);
                    js.put("message", encodedText);
                    listModel.addElement("您 (" + dateFormat.format(new Date()) + "):");
                    listModel.addElement(sendMsg.getText());
                    Control.connect.SendJSON(js);
                    sendMsg.setText("");
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JSONObject js = new JSONObject();
                js.put("type", "ShakeMessage");
                js.put("receiver", target);
                listModel.addElement("您 (" + dateFormat.format(new Date()) + ") 发送了一个窗口抖动");
                Control.connect.SendJSON(js);
                Rectangle r = jframe.getBounds();
                try {
                    for (int i = 0; i < 6; i++) {
                        r.x -= 10;
                        jframe.setBounds(r);
                        Thread.sleep(30);
                        r.y += 10;
                        jframe.setBounds(r);
                        Thread.sleep(30);
                        r.x += 10;
                        jframe.setBounds(r);
                        Thread.sleep(30);
                        r.y -= 10;
                        jframe.setBounds(r);
                        Thread.sleep(30);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {

            }

            @Override
            public void mouseReleased(MouseEvent e) {

            }

            @Override
            public void mouseEntered(MouseEvent e) {

            }

            @Override
            public void mouseExited(MouseEvent e) {

            }
        });
    }

    void ShakeWindow() {
        listModel.addElement(nickname + " (" + dateFormat.format(new Date()) + ") 发送了一个窗口抖动");
        Rectangle r = jframe.getBounds();
        try {
            for (int i = 0; i < 6; i++) {
                r.x -= 10;
                jframe.setBounds(r);
                Thread.sleep(30);
                r.y += 10;
                jframe.setBounds(r);
                Thread.sleep(30);
                r.x += 10;
                jframe.setBounds(r);
                Thread.sleep(30);
                r.y -= 10;
                jframe.setBounds(r);
                Thread.sleep(30);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    void AddTextMessage(JSONObject json) {
        try {
            String time = json.getString("Time");
            String encodedmessage = json.getString("message");
            String message = new String(decoder.decode(encodedmessage), "GBK");
            listModel.addElement(nickname + " (" + time + "):");
            listModel.addElement(message);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    void GetFocus() {
        jframe.toFront();
    }

    void Close() {
        jframe.dispose();
    }


}

