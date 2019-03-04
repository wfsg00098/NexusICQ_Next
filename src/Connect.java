import net.sf.json.JSONObject;

import java.io.*;
import java.net.Socket;

class Connect extends Thread {
    private Socket socket;
    private Socket socket2;
    private BufferedReader br = null;
    private PrintWriter pw = null;
    private BufferedReader br2 = null;
    private PrintWriter pw2 = null;
    private boolean EXIT = false;

    Connect(Socket s) {
        this.socket = s;
        try {
            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            pw = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            socket2 = new Socket(Settings.SERVERADDRESS, Settings.SERVER_PORT2);
            br2 = new BufferedReader(new InputStreamReader(socket2.getInputStream()));
            pw2 = new PrintWriter(new OutputStreamWriter(socket2.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (!EXIT) {
            while (!Control.started) {
            }
            try {
                Judge(JSONObject.fromObject(br.readLine()));
            } catch (java.net.SocketTimeoutException e) {
                if (EXIT) {
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("连接断开");
                break;
            }
        }
    }

    private synchronized void Judge(JSONObject json) {
        try {
            if (json.isNullObject()) {
                EXIT = true;
                return;
            }
            String type = json.getString("type");
            switch (type) {
                case "TextMessage":
                    TextMessage(json);
                    break;
                case "ShakeMessage":
                    ShakeMessage(json);
                    break;
                case "AddRequest":
                    AddRequest(json);
                    break;
                case "AddAgreed":
                    AddAgreed(json);
                    break;
                case "AddRejected":
                    AddRejected(json);
                    break;
                case "WasDeleted":
                    WasDeleted(json);
                    break;
                default:
                    System.out.println("接收到未知消息类型 ---- " + type);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            EXIT = true;
        }
    }

    synchronized void SendJSON(JSONObject json) {
        pw.write(json.toString() + "\n");
        pw.flush();
    }

    synchronized JSONObject GetJSON() {
        try {
            String result = br2.readLine();
            return JSONObject.fromObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private synchronized void TextMessage(JSONObject json) {
        Control.customer.GetTextMessage(json);
    }

    private synchronized void ShakeMessage(JSONObject json) {
        Control.customer.GetShakeMessage(json);
    }

    private synchronized void AddRequest(JSONObject json) {
        Control.customer.GetAddRequest(json);
    }

    private synchronized void AddAgreed(JSONObject json) {
        Control.customer.GetAddAgreed(json);
    }

    private synchronized void AddRejected(JSONObject json) {
        Control.customer.GetAddRejected(json);
    }

    private synchronized void WasDeleted(JSONObject json) {
        Control.customer.GetWasDeleted(json);
    }
}
