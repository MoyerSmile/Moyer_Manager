package sl.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.concurrent.ThreadLocalRandom;

/**
 * ��֤�빤����
 * 
 * @author moyer
 * @date 2018��1��21��
 */
public class VerificationCodeUtil {
    /**
     * ���������֤���key��
     */
    // static char[] chars = { 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
    // 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W',
    // 'X', 'Y', 'Z', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };
    static char[] chars = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9' };

    /**
     * ����һ��λ��Ϊcount�������֤��
     * 
     * @param count
     * @return
     */
    public static String genCaptcha(int count) {
        StringBuilder captcha = new StringBuilder();

        for (int i = 0; i < count; i++) {
            char c = chars[ThreadLocalRandom.current().nextInt(chars.length)];// ���ѡȡһ����ĸ������
            captcha.append(c);
        }
        return captcha.toString();
    }

    /**
     * Ϊһ����֤������һ��ͼƬ
     * 
     * ���ԣ�
     * - ��ɫ���
     * - ����λ�����
     * - ����λ����������ַ�֮�䲻���ص�
     * - ���������תһ���Ƕ�
     * - �����ַ�����
     * - �����ɫ��С�ַ�����������
     * - �����ַ���С�Զ�����ͼƬ��С���Զ���������ַ��ĸ���
     * 
     * @param captcha
     * @return
     */
    public static BufferedImage genCaptchaImg(String captcha) {
        ThreadLocalRandom r = ThreadLocalRandom.current();

        int count = captcha.length();
        int fontSize = 80; // code�������С
        int fontMargin = fontSize / 10; // �ַ����
        int width = (fontSize + fontMargin) * count + fontMargin; // ͼƬ����
        int height = (int) (fontSize * 1.1); // ͼƬ�߶ȣ����������С�Զ��������������ϵ�����Ե�������ռͼƬ�ı���
        int avgWidth = width / count; // �ַ�ƽ��ռλ����
        int maxDegree = 26; // �����ת����

        // ������ɫ
        Color bkColor = Color.gray;
        // ��֤�����ɫ
        Color[] catchaColor = { Color.MAGENTA, Color.BLACK, Color.BLUE,
                Color.CYAN, Color.GREEN, Color.ORANGE, Color.PINK };

        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();

        // ����ɫΪ�Ұ�
        g.setColor(bkColor);
        g.fillRect(0, 0, width, height);

        // ���߿�
        g.setColor(Color.BLACK);
        g.drawRect(0, 0, width - 1, height - 1);

        // ��������ĸ������
        int dSize = fontSize / 5; // ������ĸ��С�Ե��������ַ���С
        Font font = new Font("Fixedsys", Font.PLAIN, dSize);
        g.setFont(font);
        int dNumber = width * height / dSize / dSize;// ����������������ĸ�ĸ���
        for (int i = 0; i < dNumber; i++) {
            char d_code = chars[r.nextInt(chars.length)];
            g.setColor(
                new Color(r.nextInt(255), r.nextInt(255), r.nextInt(255)));
            g.drawString(String.valueOf(d_code), r.nextInt(width),
                r.nextInt(height));
        }

        // ��ʼ����֤�룺

        // ��������
        font = new Font(Font.MONOSPACED, Font.ITALIC | Font.BOLD, fontSize);
        // ��������
        g.setFont(font);

        for (int i = 0; i < count; i++) {
            char c = captcha.charAt(i);
            g.setColor(catchaColor[r.nextInt(catchaColor.length)]);// ���ѡȡһ����ɫ

            // �����תһ���Ƕ�[-maxDegre, maxDegree]
            int degree = r.nextInt(-maxDegree, maxDegree + 1);

            // ƫ��ϵ��������ת�Ƕȳɷ��ȣ��Ա����ַ���ͼƬ��Խ���߿�
            double offsetFactor = 1 - (Math.abs(degree) / (maxDegree + 1.0));// ����1��������ֽ��Ϊ0

            g.rotate(degree * Math.PI / 180); // ��תһ���Ƕ�
            int x = (int) (fontMargin
                    + r.nextInt(avgWidth - fontSize) * offsetFactor); // ����ƫ�Ƶľ���
            int y = (int) (fontSize
                    + r.nextInt(height - fontSize) * offsetFactor); // ����ƫ�Ƶľ���

            g.drawString(String.valueOf(c), x, y); // x,y���ַ������½ǣ�ƫ��ԭ��ľ��룡����

            g.rotate(-degree * Math.PI / 180); // ����һ���ַ�֮����ת��ԭ���ĽǶ�
            g.translate(avgWidth, 0);// �ƶ�����һ��������ԭ��
            // System.out.println(c+": x="+x+" y="+y+" degree="+degree+"
            // offset="+offsetFactor);

            // X��Y�����ں��ʵķ�Χ�����������ת��
            // g.drawString(String.valueOf(c),
            // width/count*i+r.nextInt(width/count-fontSize),
            // fontSize+r.nextInt(height-fontSize));
        }

        g.dispose();

        return image;
    }

}