import javax.swing.*;

class InfoImage {
    static Icon InfoImage() {
        String[] img = {"pinfo1", "pinfo2", "pinfo3", "pinfo4", "pinfo5"};
        int i = (int) (Math.random() * 4) + 1;
        return new ImageIcon("person_info/" + img[i] + ".jpg");
    }
}

