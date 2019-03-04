import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class Customer {
    private Base64.Decoder decoder = Base64.getDecoder();
    static Map<String, ChatWindow> windows = new HashMap<>();
    Map<String, String> usernames = new HashMap<>();
    private JFrame jframe;
    private JPanel plAll = new JPanel();
    private static List allChat = new List();

    private JPanel plOne = new JPanel();
    private static List oneChat = new List();

    private JPanel plSend = new JPanel(new BorderLayout());
    private JTextArea sendMsg = new JTextArea();
    private JButton btnSend = new JButton("����");
    private JButton sendAll = new JButton("����ȫ����Ϣ");
    private JPanel plSearch = new JPanel();

    private JPanel plUser_msg = new JPanel();
    private static JTabbedPane tab;

    static JList userList;
    static DefaultListModel listModel;
    static int j = 0; // �б���
    private JScrollPane js;
    private final JTextField search = new JTextField();//�������ܵ��ı���


    Customer() {
        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", Control.username);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            try {
                Control.nickname = new String(decoder.decode(result.getString("nickname")), "GBK");
                Control.bio = new String(decoder.decode(result.getString("bio")), "GBK");
                Control.email = new String(decoder.decode(result.getString("email")), "GBK");
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            JOptionPane.showMessageDialog(null, "���յ�δ֪JSON\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
            return;
        }

        search.setHorizontalAlignment(SwingConstants.CENTER);
        search.setBounds(0, 0, 269, 26);
        search.setBackground(Color.WHITE);
        search.setColumns(10);

        jframe = new JFrame();
        BgWindow.WidnowBackground(jframe);

        jframe.getContentPane().setBackground(Color.WHITE);
        plSend.add(sendMsg, BorderLayout.CENTER);
        plSend.add(btnSend, BorderLayout.EAST);
        plUser_msg.setBackground(Color.WHITE);
        plUser_msg.setBounds(0, 0, 181, 392);
        plUser_msg.setLayout(null);
        plOne.setBackground(Color.WHITE);
        plOne.setBounds(380, 0, 200, 392);
        plOne.setLayout(null);
        oneChat.setEnabled(false);
        oneChat.setBackground(Color.WHITE);
        oneChat.select(-1);
        oneChat.setBounds(30, 180, 203, 283);
        oneChat.add("");
        oneChat.add("�˺ţ�" + Control.username);
        oneChat.add("");
        oneChat.add("�ǳƣ�" + Control.nickname);
        oneChat.add("");
        oneChat.add("ǩ����" + Control.bio);
        oneChat.add("");
        oneChat.add("���䣺" + Control.email);
        oneChat.add("");
        plOne.add(oneChat);

        JLabel label = new JLabel(InfoImage.InfoImage());
        label.setBackground(Color.WHITE);
        label.setBounds(30, 30, 203, 120);

        plOne.add(label);
        plAll.setBackground(Color.WHITE);
        plAll.setBounds(181, 0, 200, 392);
        plAll.setLayout(null);
        allChat.setMultipleMode(true);
        allChat.setBounds(0, 22, 269, 495);
        plAll.add(allChat);

        JLabel label_1 = new JLabel("ȫ����Ϣ��¼");
        label_1.setForeground(Color.DARK_GRAY);
        label_1.setBackground(Color.WHITE);
        label_1.setBounds(0, 1, 93, 22);
        plAll.add(label_1);

        sendAll.setBounds(0, 518, 269, 33);
        sendAll.setActionCommand("all");
        plAll.add(sendAll);
        jframe.getContentPane().setLayout(null);

        tabView(); // ѡ�����
        ListView(); // �����б���

        jframe.setSize(300, 633);
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setTitle(Control.nickname);
        jframe.setLocation(950, 50);
        jframe.addWindowListener(new WindowListener() {
            public void windowIconified(WindowEvent arg0) {
                jframe.dispose();
            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowOpened(WindowEvent e) {

            }
        });
        mini();
        GetFriend();
        Control.started = true;
    }

    // ѡ�����
    private void tabView() {
        tab = new JTabbedPane(JTabbedPane.TOP);
        tab.setForeground(Color.BLACK);
        tab.setBackground(Color.WHITE);
        tab.setBounds(5, 0, 274, 594);

        Icon icon_chat = new ImageIcon("main_icon/main_icon_chat2.jpg");
        //Icon icon_all = new ImageIcon("main_icon/main_icon_all.jpg");
        Icon icon_user = new ImageIcon("main_icon/main_icon_user.jpg");
        Icon icon_search = new ImageIcon("main_icon/main_icon_search.jpg");
        tab.addTab("   ", icon_chat, plUser_msg);
        //tab.addTab("   ", icon_all, plAll);
        tab.addTab("   ", icon_user, plOne);
        plSearch.setBackground(Color.WHITE);
        tab.addTab("   ", icon_search, plSearch);
        plSearch.setLayout(null);

        plSearch.add(search);
        JButton button = new JButton("��Ϊ����");
        button.addActionListener(arg0 -> {
            JSONObject js = new JSONObject();
            js.put("type", "AddFriend");
            js.put("target", search.getText());
            Control.connect.SendJSON(js);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("AddWait")) {
                JOptionPane.showMessageDialog(null, "�ѷ��ͺ�������", "��Ϣ", JOptionPane.INFORMATION_MESSAGE);
            } else if (type.equals("AddFail")) {
                JOptionPane.showMessageDialog(null, "�û�������", "����", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
            }
        });
        button.setBounds(2, 38, 266, 26);
        plSearch.add(button);


        tab.addChangeListener(arg0 -> {
            int index = tab.getSelectedIndex();
            if (index == 0) {
                tab.setTitleAt(0, "   ");
            } else if (index == 1) {
                tab.setTitleAt(1, "   ");
            }
        });
        jframe.getContentPane().add(tab);
    }

    //����list�Ĳ���
    private void ListView() {
        listModel = new DefaultListModel();
        userList = new JList(listModel);
        userList.setBackground(Color.WHITE);
        userList.setLocation(0, 0);
        userList.setSize(306, 565);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    String username = usernames.get(userList.getSelectedValue());
                    new ChatWindow(username);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (userList.getSelectedValue() == null) {
                        return;
                    }
                    String username = usernames.get(userList.getSelectedValue());
                    Object[] option = {"��", "��"};
                    int op = JOptionPane.showOptionDialog(null, "Ҫɾ������ " + username + " ��", "��ʾ", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, option, option[1]);
                    if (op == 0) {
                        JSONObject js = new JSONObject();
                        js.put("type", "DelFriend");
                        js.put("target", username);
                        Control.connect.SendJSON(js);
                        GetFriend();
                    }
                }

            }
        });
        js = new JScrollPane(userList);
        js.setLocation(0, 0);
        js.setSize(269, 523);
        plUser_msg.add(js);

    }

    private void mini() {
        SystemTray tray = SystemTray.getSystemTray();
        // ��������ͼ�꣺1.��ʾͼ��Image 2.ͣ����ʾtext 3.�����˵�popupMenu 4.��������ͼ��ʵ��
        // 1.����Imageͼ��
        Image image = Toolkit.getDefaultToolkit().getImage("main_icon/icon.jpg");
        // 2.ͣ����ʾtext

        PopupMenu popMenu = new PopupMenu();
        MenuItem itmOpen = new MenuItem("��");
        itmOpen.addActionListener(e -> Show());
        MenuItem itmHide = new MenuItem("����");
        itmHide.addActionListener(e -> UnVisible());
        MenuItem itmExit = new MenuItem("�˳�");
        itmExit.addActionListener(e -> Exit());
        popMenu.add(itmOpen);
        popMenu.add(itmHide);
        popMenu.add(itmExit);

        TrayIcon trayIcon = new TrayIcon(image, "Gram_Client��" + Control.nickname + "(" + Control.username + ")", popMenu);
        trayIcon.addActionListener(e -> Show());
        // ������ͼ��ӵ�������
        try {
            tray.add(trayIcon);
        } catch (AWTException e1) {
            e1.printStackTrace();
        }

    }

    private void UnVisible() {
        jframe.setVisible(false);
    }

    private void Show() {
        jframe.setVisible(true);
    }

    private void Exit() {
        JSONObject js = new JSONObject();
        js.put("type", "Logout");
        Control.connect.SendJSON(js);
        System.exit(0);
    }

    void GetTextMessage(JSONObject json) {
        String from = json.getString("from");
        if (!windows.containsKey(from)) {
            ChatWindow cw = new ChatWindow(from);
        }
        windows.get(from).GetFocus();
        windows.get(from).AddTextMessage(json);
    }

    void GetShakeMessage(JSONObject json) {
        String from = json.getString("from");
        if (!windows.containsKey(from)) {
            ChatWindow cw = new ChatWindow(from);
        }
        windows.get(from).GetFocus();
        windows.get(from).ShakeWindow();
    }

    void GetAddRequest(JSONObject json) {
        Object[] option = {"ͬ��", "�ܾ�"};
        String from = json.getString("from");
        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", from);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            int op = 0;
            try {
                op = JOptionPane.showOptionDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") ���������Ϊ����", "����", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println(op);
            if (op == 0) {
                JSONObject js1 = new JSONObject();
                js1.put("type", "AgreeAdd");
                js1.put("target", from);
                Control.connect.SendJSON(js1);
                GetFriend();
                try {
                    JOptionPane.showMessageDialog(null, "����� " + new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") ", "����", JOptionPane.INFORMATION_MESSAGE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else if (op == 1) {
                JSONObject js1 = new JSONObject();
                js1.put("type", "RejectAdd");
                js1.put("target", from);
                Control.connect.SendJSON(js1);
                try {
                    JOptionPane.showMessageDialog(null, "�Ѿܾ� " + new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") �ĺ�������", "����", JOptionPane.INFORMATION_MESSAGE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
        }

    }

    void GetAddAgreed(JSONObject json) {
        String from = json.getString("from");
        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", from);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            try {
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") ��ͬ����ĺ�������", "����", JOptionPane.INFORMATION_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
        }
        GetFriend();
    }

    void GetAddRejected(JSONObject json) {
        String from = json.getString("from");
        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", from);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            try {
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") �ܾ�����ĺ�������", "����", JOptionPane.WARNING_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
        }
    }

    void GetWasDeleted(JSONObject json) {
        String from = json.getString("from");
        JSONObject js = new JSONObject();
        js.put("type", "GetInfo");
        js.put("username", from);
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        String type = result.getString("type");
        if (type.equals("GetInfo")) {
            try {
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") �Ѳ�������ĺ���", "����", JOptionPane.WARNING_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
        }
        GetFriend();
        if (windows.get(from) != null) {
            windows.get(from).Close();
        }
    }

    void GetFriend() {
        listModel.removeAllElements();
        usernames.clear();
        JSONObject js = new JSONObject();
        js.put("type", "GetFriend");
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        int count = Integer.parseInt(result.getString("count"));
        for (int i = 0; i < count; i++) {
            String target = result.getString("username" + i);
            JSONObject json = new JSONObject();
            json.put("type", "GetInfo");
            json.put("username", target);
            Control.connect.SendJSON(json);
            JSONObject result1 = Control.connect.GetJSON();
            String type = result1.getString("type");
            if (type.equals("GetInfo")) {
                try {
                    listModel.addElement(new String(decoder.decode(result1.getString("nickname")), "GBK") + "(" + target + ") - " + new String(decoder.decode(result1.getString("bio")), "GBK"));
                    usernames.put(new String(decoder.decode(result1.getString("nickname")), "GBK") + "(" + target + ") - " + new String(decoder.decode(result1.getString("bio")), "GBK"), target);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "����������δ֪��Ϣ\n" + result.toString(), "����", JOptionPane.ERROR_MESSAGE);
            }
        }

    }
}
