import net.sf.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.Socket;
import java.text.DecimalFormat;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

class Customer {
    private Base64.Encoder encoder = Base64.getUrlEncoder();
    private Base64.Decoder decoder = Base64.getUrlDecoder();
    static Map<String, ChatWindow> windows = new HashMap<>();
    static Map<String, ChatWindow> groupwindows = new HashMap<>();
    Map<String, String> usernames = new HashMap<>();
    Map<String, String> groupnames = new HashMap<>();

    private JFrame jframe;
    private JPanel plAll = new JPanel();

    private JPanel plOne = new JPanel();
    private static List oneChat = new List();

    private JPanel plSend = new JPanel(new BorderLayout());
    private JTextArea sendMsg = new JTextArea();
    private JButton btnSend = new JButton("发送");
    private JPanel plSearch = new JPanel();

    private JPanel plUser_msg = new JPanel();
    private static JTabbedPane tab;

    static JList userList;
    static DefaultListModel listModel;
    static JList groupList;
    static DefaultListModel grouplistmodel;
    private JScrollPane js;
    private JTextField search = new JTextField();

    class FileRecv extends JFrame implements Runnable {
        JProgressBar bar;
        File file;
        long length;

        FileRecv(File file, long length) {
            this.file = file;
            this.length = length;
            setLayout(null);
            bar = new JProgressBar();
            bar.setMaximum(100);
            bar.setMinimum(0);
        }

        @Override
        public void run() {
            try {
                setBackground(Color.WHITE);
                setUndecorated(true);
                setSize(210, 30);
                bar.setBounds(5, 5, 200, 20);
                add(bar);
                setVisible(true);
                setLocationRelativeTo(null);
                Socket FileRecv = new Socket(Settings.SERVERADDRESS, Settings.FILE_RECEIVE_PORT);
                DataInputStream dis = new DataInputStream(FileRecv.getInputStream());
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(file));
                byte[] buffer = new byte[Settings.BUFFER_SIZE];
                long recved = 0;
                while (true) {
                    int read;
                    read = dis.read(buffer);
                    if (read == -1) {
                        break;
                    }
                    dos.write(buffer, 0, read);
                    dos.flush();
                    recved += read;
                    bar.setValue((int) (recved * 100 / length));
                }
                dis.close();
                dos.close();
                FileRecv.close();
                JOptionPane.showMessageDialog(this, "文件已成功接收到" + file.getAbsolutePath(), "文件传输成功", JOptionPane.INFORMATION_MESSAGE);
                dispose();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


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
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
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
        plUser_msg.setBounds(0, 0, 181, 422);
        plUser_msg.setLayout(null);
        plOne.setBackground(Color.WHITE);
        plOne.setBounds(380, 0, 200, 422);
        plOne.setLayout(null);
        oneChat.setEnabled(false);
        oneChat.setBackground(Color.WHITE);
        oneChat.select(-1);
        oneChat.setBounds(30, 180, 203, 313);
        oneChat.add("");
        oneChat.add("账号：" + Control.username);
        oneChat.add("");
        oneChat.add("昵称：" + Control.nickname);
        oneChat.add("");
        oneChat.add("签名：" + Control.bio);
        oneChat.add("");
        oneChat.add("邮箱：" + Control.email);
        oneChat.add("");
        plOne.add(oneChat);

        JLabel label = new JLabel(InfoImage.InfoImage());
        label.setBackground(Color.WHITE);
        label.setBounds(30, 30, 203, 120);

        plOne.add(label);
        plAll.setBackground(Color.WHITE);
        plAll.setBounds(0, 0, 200, 422);
        plAll.setLayout(null);

        jframe.getContentPane().setLayout(null);

        tabView(); // 选项卡布局
        ListView(); // 好友列表布局
        GroupView();

        jframe.setSize(300, 633);
        jframe.setVisible(true);
        jframe.setResizable(false);
        jframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        jframe.setTitle(Control.nickname);
        jframe.setLocationRelativeTo(null);
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
        GetGroup();
        Control.started = true;
    }

    // 选项卡布局
    private void tabView() {
        tab = new JTabbedPane(JTabbedPane.TOP);
        tab.setForeground(Color.BLACK);
        tab.setBackground(Color.WHITE);
        tab.setBounds(5, 0, 274, 594);

        Icon icon_chat = new ImageIcon("main_icon/main_icon_chat2.jpg");
        Icon icon_all = new ImageIcon("main_icon/main_icon_all.jpg");
        Icon icon_user = new ImageIcon("main_icon/main_icon_user.jpg");
        Icon icon_search = new ImageIcon("main_icon/main_icon_search.jpg");
        tab.addTab("   ", icon_chat, plUser_msg);
        tab.addTab("   ", icon_all, plAll);
        tab.addTab("   ", icon_user, plOne);
        plSearch.setBackground(Color.WHITE);
        tab.addTab("   ", icon_search, plSearch);
        plSearch.setLayout(null);

        plSearch.add(search);
        JButton button = new JButton("加为好友");
        button.addActionListener(arg0 -> {
            JSONObject js = new JSONObject();
            js.put("type", "AddFriend");
            js.put("target", search.getText());
            Control.connect.SendJSON(js);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("AddWait")) {
                JOptionPane.showMessageDialog(null, "已发送好友请求", "信息", JOptionPane.INFORMATION_MESSAGE);
            } else if (type.equals("AddFail")) {
                JOptionPane.showMessageDialog(null, "用户不存在", "错误", JOptionPane.ERROR_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        button.setBounds(2, 38, 266, 26);
        plSearch.add(button);

        JButton AddGroup = new JButton("加入群组");
        AddGroup.addActionListener(arg0 -> {
            JSONObject js = new JSONObject();
            js.put("type", "JoinGroup");
            js.put("groupname", search.getText());
            Control.connect.SendJSON(js);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("JoinGroup")) {
                String status = result.getString("status");
                if (status.equals("success")) {
                    JOptionPane.showMessageDialog(null, "已加入群组", "信息", JOptionPane.INFORMATION_MESSAGE);
                    GetGroup();
                } else if (status.equals("GroupNotExists")) {
                    JOptionPane.showMessageDialog(null, "群组不存在", "错误", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(null, "服务器发生内部错误", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        });
        AddGroup.setBounds(2, 76, 266, 26);
        plSearch.add(AddGroup);

        JButton CreateGroup = new JButton("创建群组");
        CreateGroup.addActionListener(arg0 -> {
            try {
                String groupname = JOptionPane.showInputDialog(null, "请输入群组号码");
                String groupnickname = JOptionPane.showInputDialog(null, "请输入群组名称");
                if (!groupname.trim().equals("") && !groupnickname.trim().equals("")) {
                    JSONObject js = new JSONObject();
                    js.put("type", "CreateGroup");
                    js.put("groupname", groupname);
                    js.put("groupnickname", encoder.encodeToString(groupnickname.getBytes("GBK")));
                    Control.connect.SendJSON(js);
                    JSONObject result = Control.connect.GetJSON();
                    String type = result.getString("type");
                    if (type.equals("CreateGroup")) {
                        String status = result.getString("status");
                        if (status.equals("success")) {
                            JOptionPane.showMessageDialog(null, "已创建群组", "信息", JOptionPane.INFORMATION_MESSAGE);
                        } else if (status.equals("duplicated")) {
                            JOptionPane.showMessageDialog(null, "群组号码已被使用，不能再次创建！", "错误", JOptionPane.ERROR_MESSAGE);
                        } else {
                            JOptionPane.showMessageDialog(null, "服务器发生内部错误", "错误", JOptionPane.ERROR_MESSAGE);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "请将信息填写完整！", "错误", JOptionPane.ERROR_MESSAGE);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        CreateGroup.setBounds(2, 114, 266, 26);
        plSearch.add(CreateGroup);


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

    //好友list的布局
    private void ListView() {
        listModel = new DefaultListModel();
        userList = new JList(listModel);
        userList.setBackground(Color.WHITE);
        userList.setLocation(0, 0);
        userList.setSize(306, 595);
        userList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        userList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    String username = usernames.get(userList.getSelectedValue());
                    new ChatWindow(username, false);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (userList.getSelectedValue() == null) {
                        return;
                    }
                    String username = usernames.get(userList.getSelectedValue());
                    Object[] option = {"是", "否"};
                    int op = JOptionPane.showOptionDialog(null, "要删除好友 " + username + " 吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, option, option[1]);
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
        js.setSize(269, 553);
        plUser_msg.add(js);
    }


    private void GroupView() {
        grouplistmodel = new DefaultListModel();
        groupList = new JList(grouplistmodel);
        groupList.setBackground(Color.WHITE);
        groupList.setLocation(0, 0);
        groupList.setSize(306, 595);
        groupList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        groupList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    String groupname = groupnames.get(groupList.getSelectedValue());
                    new ChatWindow(groupname, true);
                }
                if (e.getButton() == MouseEvent.BUTTON3) {
                    if (groupList.getSelectedValue() == null) {
                        return;
                    }
                    String groupname = groupnames.get(groupList.getSelectedValue());
                    Object[] option = {"是", "否"};
                    int op = JOptionPane.showOptionDialog(null, "要离开群组 " + groupname + " 吗？", "提示", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null, option, option[1]);
                    if (op == 0) {
                        JSONObject js = new JSONObject();
                        js.put("type", "LeaveGroup");
                        js.put("groupname", groupname);
                        Control.connect.SendJSON(js);
                        JSONObject result = Control.connect.GetJSON();
                        String type = result.getString("type");
                        if (type.equals("LeaveGroup")) {
                            String status = result.getString("status");
                            if (status.equals("success")) {
                                JOptionPane.showMessageDialog(null, "已离开群组", "提醒", JOptionPane.INFORMATION_MESSAGE);
                            } else {
                                JOptionPane.showMessageDialog(null, "服务器发生内部错误", "错误", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
                        }
                        GetGroup();
                    }
                }

            }
        });
        js = new JScrollPane(groupList);
        js.setLocation(0, 0);
        js.setSize(269, 553);
        plAll.add(js);
    }

    private void mini() {
        SystemTray tray = SystemTray.getSystemTray();
        // 创建托盘图标：1.显示图标Image 2.停留提示text 3.弹出菜单popupMenu 4.创建托盘图标实例
        // 1.创建Image图像
        Image image = Toolkit.getDefaultToolkit().getImage("main_icon/icon.jpg");
        // 2.停留提示text

        PopupMenu popMenu = new PopupMenu();
        MenuItem itmOpen = new MenuItem("打开");
        itmOpen.addActionListener(e -> Show());
        MenuItem itmHide = new MenuItem("隐藏");
        itmHide.addActionListener(e -> UnVisible());
        MenuItem itmExit = new MenuItem("退出");
        itmExit.addActionListener(e -> Exit());
        popMenu.add(itmOpen);
        popMenu.add(itmHide);
        popMenu.add(itmExit);

        TrayIcon trayIcon = new TrayIcon(image, "Gram_Client：" + Control.nickname + "(" + Control.username + ")", popMenu);
        trayIcon.addActionListener(e -> Show());
        // 将托盘图标加到托盘上
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


    void GetGroupTextMessage(JSONObject json) {
        String fromGroup = json.getString("fromGroup");
        if (!groupwindows.containsKey(fromGroup)) {
            new ChatWindow(fromGroup, true);
        }
        groupwindows.get(fromGroup).GetFocus();
        groupwindows.get(fromGroup).AddGroupTextMessage(json);
    }


    void GetTextMessage(JSONObject json) {
        String from = json.getString("from");
        if (!windows.containsKey(from)) {
            new ChatWindow(from, false);
        }
        windows.get(from).GetFocus();
        windows.get(from).AddTextMessage(json);
    }

    void GetShakeMessage(JSONObject json) {
        String from = json.getString("from");
        if (!windows.containsKey(from)) {
            new ChatWindow(from, false);
        }
        windows.get(from).GetFocus();
        windows.get(from).ShakeWindow();
    }

    void GetAddRequest(JSONObject json) {
        Object[] option = {"同意", "拒绝"};
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
                op = JOptionPane.showOptionDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") 请求添加你为好友", "提醒", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            if (op == 0) {
                JSONObject js1 = new JSONObject();
                js1.put("type", "AgreeAdd");
                js1.put("target", from);
                Control.connect.SendJSON(js1);
                GetFriend();
                try {
                    JOptionPane.showMessageDialog(null, "已添加 " + new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") ", "提醒", JOptionPane.INFORMATION_MESSAGE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            } else if (op == 1) {
                JSONObject js1 = new JSONObject();
                js1.put("type", "RejectAdd");
                js1.put("target", from);
                Control.connect.SendJSON(js1);
                try {
                    JOptionPane.showMessageDialog(null, "已拒绝 " + new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") 的好友申请", "提醒", JOptionPane.INFORMATION_MESSAGE);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        } else {
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") 已同意你的好友请求", "提醒", JOptionPane.INFORMATION_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") 拒绝了你的好友请求", "提醒", JOptionPane.WARNING_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, new String(decoder.decode(result.getString("nickname")), "GBK") + "(" + from + ") 已不再是你的好友", "提醒", JOptionPane.WARNING_MESSAGE);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
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
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result1.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void GetGroup() {
        grouplistmodel.removeAllElements();
        groupnames.clear();
        JSONObject js = new JSONObject();
        js.put("type", "GetGroup");
        Control.connect.SendJSON(js);
        JSONObject result = Control.connect.GetJSON();
        int count = Integer.parseInt(result.getString("count"));
        for (int i = 0; i < count; i++) {
            String target = result.getString("groupname" + i);
            JSONObject json = new JSONObject();
            json.put("type", "GetGroupNickname");
            json.put("groupname", target);
            Control.connect.SendJSON(json);
            JSONObject result1 = Control.connect.GetJSON();
            String type = result1.getString("type");
            if (type.equals("GetGroupNickname")) {
                try {
                    grouplistmodel.addElement(new String(decoder.decode(result1.getString("GroupNickname")), "GBK") + "(" + target + ")");
                    groupnames.put(new String(decoder.decode(result1.getString("GroupNickname")), "GBK") + "(" + target + ")", target);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result1.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    void FileRequest(JSONObject json) {
        try {
            String from = json.getString("from");
            String filename = json.getString("filename");
            long length = Long.parseLong(json.getString("length"));
            String size;
            DecimalFormat df = new DecimalFormat("#0.00");
            double len = length;
            if (len >= 1024) {
                if (len >= 1024 * 1024) {
                    if (len >= 1024 * 1024 * 1024) {
                        len /= (1024 * 1024 * 1024);
                        size = df.format(len) + "GB";
                    } else {
                        len /= (1024 * 1024);
                        size = df.format(len) + "MB";
                    }
                } else {
                    len /= 1024;
                    size = df.format(len) + "KB";
                }
            } else {
                size = df.format(len) + "B";
            }
            JSONObject js = new JSONObject();
            js.put("type", "GetInfo");
            js.put("username", from);
            Control.connect.SendJSON(js);
            JSONObject result = Control.connect.GetJSON();
            String type = result.getString("type");
            if (type.equals("GetInfo")) {
                String nickname = new String(decoder.decode(result.getString("nickname")), "GBK");
                Object[] option = {"是", "否"};
                int op = JOptionPane.showOptionDialog(null, nickname + "(" + from + ") 想要给您发送文件" + new String(decoder.decode(filename), "GBK") + "\n文件大小：" + size + "\n请问您是否要接收该文件？", "文件传输", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, option, option[0]);
                JSONObject result1 = new JSONObject();
                result1.put("type", "FileRequest");
                result1.put("filename", filename);
                if (op == 0) {
                    result1.put("status", "accepted");
                    JFileChooser jfc = new JFileChooser();
                    jfc.setFileSelectionMode(JFileChooser.FILES_ONLY);
                    jfc.showSaveDialog(null);
                    File file = jfc.getSelectedFile();
                    new Thread(new FileRecv(file, length)).start();
                } else {
                    result1.put("status", "declined");
                }
                Control.connect.SendJSON(result1);
            } else {
                JOptionPane.showMessageDialog(null, "服务器返回未知消息\n" + result.toString(), "错误", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
