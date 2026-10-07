import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.geom.CubicCurve2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

import javax.imageio.ImageIO;

/**
 * Công cụ sinh 20 hình captcha (bước b của đề bài: "thu thập 20 hình captcha độc đáo").
 * Không thuộc ứng dụng web; chỉ chạy một lần để tạo hình trong src/main/resources.
 *
 * Chạy từ thư mục Duan_1.10:
 *   java tools/CaptchaGenerator.java
 */
public class CaptchaGenerator {

    private static final int COUNT = 20;
    private static final int WIDTH = 220;
    private static final int HEIGHT = 80;
    private static final int LENGTH = 6;
    // Bỏ các ký tự dễ nhầm: 0/O, 1/I/L
    private static final String CHARS = "ABCDEFGHJKMNPQRSTUVWXYZ23456789";
    private static final String[] FONTS = { Font.SERIF, Font.SANS_SERIF, Font.MONOSPACED, Font.DIALOG };

    public static void main(String[] args) throws IOException {
        Path imageDir = Path.of("src/main/resources/static/captcha");
        Path answerFile = Path.of("src/main/resources/captcha-answers.properties");
        Files.createDirectories(imageDir);

        // Seed cố định: chạy lại luôn ra đúng 20 hình và đáp án như cũ
        Random random = new Random(20261007L);

        try (PrintWriter answers = new PrintWriter(Files.newBufferedWriter(answerFile, StandardCharsets.UTF_8))) {
            answers.println("# Đáp án của 20 hình captcha, sinh bởi tools/CaptchaGenerator.java");
            for (int i = 1; i <= COUNT; i++) {
                String text = randomText(random);
                BufferedImage image = draw(text, i, random);
                String name = String.format("captcha%02d", i);
                ImageIO.write(image, "png", imageDir.resolve(name + ".png").toFile());
                answers.println(name + "=" + text);
                System.out.println(name + ".png -> " + text);
            }
        }
    }

    private static String randomText(Random random) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < LENGTH; i++) {
            sb.append(CHARS.charAt(random.nextInt(CHARS.length())));
        }
        return sb.toString();
    }

    private static BufferedImage draw(String text, int index, Random random) {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        // Mỗi hình một tông màu nền riêng, chia đều vòng màu theo số thứ tự
        float hue = (index - 1) / (float) COUNT;
        Color light1 = Color.getHSBColor(hue, 0.15f, 1.0f);
        Color light2 = Color.getHSBColor(hue + 0.08f, 0.30f, 0.95f);
        g.setPaint(new GradientPaint(0, 0, light1, WIDTH, HEIGHT, light2));
        g.fillRect(0, 0, WIDTH, HEIGHT);

        // Nhiễu: chấm nhỏ
        for (int i = 0; i < 250; i++) {
            g.setColor(randomColor(random, hue, 0.4f, 0.7f));
            g.fillOval(random.nextInt(WIDTH), random.nextInt(HEIGHT), 2, 2);
        }

        // Nhiễu: đường cong phía sau chữ
        g.setStroke(new BasicStroke(1.5f));
        for (int i = 0; i < 4; i++) {
            g.setColor(randomColor(random, hue, 0.5f, 0.6f));
            g.draw(randomCurve(random));
        }

        // Vẽ từng ký tự với font, cỡ, góc xoay và màu khác nhau
        int x = 14;
        for (char c : text.toCharArray()) {
            int style = random.nextBoolean() ? Font.BOLD : Font.BOLD | Font.ITALIC;
            Font font = new Font(FONTS[random.nextInt(FONTS.length)], style, 34 + random.nextInt(10));
            double angle = Math.toRadians(random.nextInt(50) - 25);
            int y = 50 + random.nextInt(14) - 7;

            AffineTransform old = g.getTransform();
            g.translate(x, y);
            g.rotate(angle);
            g.setFont(font);
            g.setColor(randomColor(random, hue + 0.5f, 0.8f, 0.45f));
            g.drawString(String.valueOf(c), 0, 0);
            g.setTransform(old);

            x += 30 + random.nextInt(4);
        }

        // Nhiễu: một đường cong cắt ngang qua chữ
        g.setStroke(new BasicStroke(2.5f));
        g.setColor(randomColor(random, hue + 0.5f, 0.7f, 0.4f));
        g.draw(randomCurve(random));

        g.dispose();
        return image;
    }

    private static Color randomColor(Random random, float baseHue, float saturation, float brightness) {
        float hue = baseHue + (random.nextFloat() - 0.5f) * 0.3f;
        return Color.getHSBColor(hue, saturation, brightness + random.nextFloat() * 0.1f);
    }

    private static CubicCurve2D randomCurve(Random random) {
        return new CubicCurve2D.Float(
                0, random.nextInt(HEIGHT),
                WIDTH / 3f, random.nextInt(HEIGHT),
                WIDTH * 2 / 3f, random.nextInt(HEIGHT),
                WIDTH, random.nextInt(HEIGHT));
    }
}
