package com.example.captcha;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;

import com.example.captcha.service.CaptchaService;

@SpringBootTest
@AutoConfigureMockMvc
class CaptchaApplicationTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private CaptchaService captchaService;

    @Test
    void all20ImagesAreServed() throws Exception {
        for (int i = 1; i <= CaptchaService.TOTAL; i++) {
            mvc.perform(get(captchaService.imageUrl(i)))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType("image/png"));
        }
    }

    @Test
    void consecutiveVisitsNeverShowTheSameImage() throws Exception {
        MockHttpSession session = new MockHttpSession();
        Set<Integer> seen = new HashSet<>();
        Integer previous = null;
        for (int i = 0; i < 100; i++) {
            mvc.perform(get("/").session(session)).andExpect(status().isOk());
            Integer current = (Integer) session.getAttribute("captchaIndex");
            assertThat(current).isBetween(1, CaptchaService.TOTAL).isNotEqualTo(previous);
            seen.add(current);
            previous = current;
        }
        // 100 lần chọn ngẫu nhiên thì gần như chắc chắn gặp nhiều hình khác nhau
        assertThat(seen.size()).isGreaterThan(10);
    }

    @Test
    void correctAnswerIsAccepted() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(get("/").session(session));
        int index = (Integer) session.getAttribute("captchaIndex");
        String answer = loadAnswers().getProperty(String.format("captcha%02d", index));

        mvc.perform(post("/verify").session(session).param("answer", answer.toLowerCase()))
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("ok", true));
    }

    @Test
    void wrongAnswerIsRejected() throws Exception {
        MockHttpSession session = new MockHttpSession();
        mvc.perform(get("/").session(session));

        mvc.perform(post("/verify").session(session).param("answer", "000000"))
                .andExpect(flash().attribute("ok", false));
    }

    private Properties loadAnswers() throws Exception {
        Properties p = new Properties();
        try (var reader = new InputStreamReader(
                new ClassPathResource("captcha-answers.properties").getInputStream(), StandardCharsets.UTF_8)) {
            p.load(reader);
        }
        return p;
    }
}
