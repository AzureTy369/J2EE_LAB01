package com.example.cv.controller;

import com.example.cv.model.CvProfile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class CvController {

    @GetMapping({"/", "/cv"})
    public String home(Model model) {
        model.addAttribute("profile", profile());
        return "cv";
    }

    private CvProfile profile() {
        return new CvProfile(
                "Nguyễn Văn A",
                "Java Backend Developer",
                "Lập trình viên Java yêu thích xây dựng các sản phẩm web rõ ràng, ổn định và dễ mở rộng. Tôi tập trung vào Spring Boot, REST API và trải nghiệm người dùng thực tế.",
                "an.nguyen@example.com",
                "+84 912 345 678",
                "Thành phố Hồ Chí Minh, Việt Nam",
                "github.com/nguyenvanan",
                List.of("Java", "Spring Boot", "RESTful API", "SQL", "Git", "HTML/CSS"),
                List.of(
                        new CvProfile.Experience("2024 — nay", "Tech Solutions", "Java Backend Developer",
                                "Phát triển REST API, thiết kế nghiệp vụ và phối hợp với frontend để triển khai các tính năng web."),
                        new CvProfile.Experience("2022 — 2024", "Digital Lab", "Junior Developer",
                                "Xây dựng module quản lý dữ liệu, viết truy vấn SQL và hỗ trợ kiểm thử tích hợp.")
                ),
                List.of(
                        new CvProfile.Project("Student Score API", "API tra cứu điểm theo số báo danh với xác thực User-Token và phản hồi JSON.", "Spring Boot · REST · Java", "#"),
                        new CvProfile.Project("Personal Portfolio", "Website CV responsive giới thiệu năng lực, kinh nghiệm và các dự án cá nhân.", "Spring Boot · Thymeleaf · CSS", "#")
                )
        );
    }
}
