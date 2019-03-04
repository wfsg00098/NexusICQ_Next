import javax.swing.*;

/**
 * …Ë÷√¥∞ø⁄±≥æ∞Õº∆¨
 */
class BgWindow {
    static void WidnowBackground(JFrame jframe) {
        String[] imgurl = {"main", "main1", "main2", "main3", "main4", "main5"};
        int i = (int) (Math.random() * 4) + 1;
        String url = "background/" + imgurl[i] + ".jpg";
        Icon img = new ImageIcon(url);
        JLabel imgbel = new JLabel(img);
        jframe.getLayeredPane().add(imgbel, Integer.valueOf(Integer.MIN_VALUE));
        imgbel.setBounds(0, 0, jframe.getWidth(), jframe.getHeight());
        imgbel.setBounds(0, 0, img.getIconWidth(), img.getIconHeight());
        ((JPanel) jframe.getContentPane()).setOpaque(false);
    }

}

