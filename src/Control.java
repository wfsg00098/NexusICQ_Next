import javax.swing.*;
import java.net.Socket;

public class Control {
    static Connect connect;
    static String username;
    static String nickname;
    static String email;
    static String bio;
    static Customer customer;
    static volatile boolean started = false;

    public static void main(String[] args) {

        Socket socket;
        try {
            socket = new Socket(Settings.SERVERADDRESS, Settings.SERVER_PORT);
            connect = new Connect(socket);
            connect.start();
            new LoginView();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "�޷����ӵ�������", "����", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

    }
}
