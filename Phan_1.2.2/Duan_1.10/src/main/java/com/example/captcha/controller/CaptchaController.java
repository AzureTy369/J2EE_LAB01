package com.example.captcha.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.captcha.service.CaptchaService;

import jakarta.servlet.http.HttpSession;

@Controller
public class CaptchaController {

    // Số thứ tự hình đang hiển thị được giữ trong session, không gửi đáp án ra trình duyệt
    private static final String SESSION_KEY = "captchaIndex";

    private final CaptchaService captchaService;

    public CaptchaController(CaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    // Mỗi lần truy cập (hoặc F5) chọn một hình mới
    @GetMapping("/")
    public String index(HttpSession session, Model model) {
        Integer previous = (Integer) session.getAttribute(SESSION_KEY);
        int index = captchaService.pickRandom(previous);
        session.setAttribute(SESSION_KEY, index);

        model.addAttribute("index", index);
        model.addAttribute("total", CaptchaService.TOTAL);
        model.addAttribute("imageUrl", captchaService.imageUrl(index));
        return "index";
    }

    @PostMapping("/verify")
    public String verify(@RequestParam String answer, HttpSession session, RedirectAttributes redirect) {
        Integer index = (Integer) session.getAttribute(SESSION_KEY);
        boolean ok = index != null && captchaService.check(index, answer);

        redirect.addFlashAttribute("ok", ok);
        redirect.addFlashAttribute("answer", answer);
        // Quay về trang chủ: dù đúng hay sai cũng đổi sang hình khác
        return "redirect:/";
    }
}
