package com.example.captcha.service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Properties;
import java.util.random.RandomGenerator;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class CaptchaService {

    public static final int TOTAL = 20;

    private final RandomGenerator random;
    private final Properties answers = new Properties();

    public CaptchaService() throws IOException {
        this(new SecureRandom());
    }

    CaptchaService(RandomGenerator random) throws IOException {
        this.random = random;
        // Đáp án của 20 hình, sinh cùng lúc với hình bởi tools/CaptchaGenerator.java
        try (Reader reader = new InputStreamReader(
                new ClassPathResource("captcha-answers.properties").getInputStream(), StandardCharsets.UTF_8)) {
            answers.load(reader);
        }
    }

    /**
     * c) Chọn ngẫu nhiên 1 số trong 1..20.
     * Nếu trùng với hình vừa hiện ở lần trước thì chọn lại, để mỗi lần truy cập luôn thấy hình khác.
     */
    public int pickRandom(Integer previous) {
        int index;
        do {
            index = random.nextInt(TOTAL) + 1;
        } while (previous != null && index == previous);
        return index;
    }

    // d) Đường dẫn tới file hình trong static/captcha, trình duyệt tải hình qua URL này
    public String imageUrl(int index) {
        return String.format("/captcha/captcha%02d.png", index);
    }

    public boolean check(int index, String input) {
        String expected = answers.getProperty(String.format("captcha%02d", index));
        return expected != null && input != null && expected.equalsIgnoreCase(input.trim());
    }
}
