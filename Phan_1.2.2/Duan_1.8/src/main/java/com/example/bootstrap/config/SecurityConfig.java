package com.example.bootstrap.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

// c) Bảo mật: ai cũng được xem, chỉ ADMIN được thêm/sửa/xóa sách
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(HttpMethod.GET, "/", "/api/books/**").permitAll()
                .requestMatchers("/h2-console/**", "/error").permitAll()
                .requestMatchers("/api/books/**").hasRole("ADMIN")
                .anyRequest().authenticated())
            // Đăng nhập bằng HTTP Basic: gửi header Authorization: Basic base64(user:password)
            .httpBasic(Customizer.withDefaults())
            // REST API và H2 console không dùng form nên tắt CSRF cho hai nhóm URL này
            .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**", "/h2-console/**"))
            // H2 console hiển thị trong <frame> nên cho phép frame cùng nguồn
            .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()));
        return http.build();
    }

    // Tài khoản lưu trong bộ nhớ, chỉ dùng cho bài tập. {noop} nghĩa là mật khẩu không mã hóa
    @Bean
    public UserDetailsService users() {
        return new InMemoryUserDetailsManager(
                User.withUsername("admin").password("{noop}admin123").roles("ADMIN").build(),
                User.withUsername("user").password("{noop}user123").roles("USER").build());
    }
}
