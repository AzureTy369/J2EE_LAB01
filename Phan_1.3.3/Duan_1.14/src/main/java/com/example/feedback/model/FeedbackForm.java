package com.example.feedback.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class FeedbackForm {
    @Min(value = 1, message = "Vui lòng chọn số sao đánh giá")
    @Max(value = 5, message = "Số sao không hợp lệ")
    private int rating;

    @NotBlank(message = "Vui lòng chọn chủ đề phản hồi")
    private String topic;

    @NotBlank(message = "Vui lòng nhập nội dung phản hồi")
    @Size(min = 10, max = 1000, message = "Nội dung phải từ 10 đến 1000 ký tự")
    private String message;

    @Size(max = 80, message = "Họ tên tối đa 80 ký tự")
    private String name;

    @Email(message = "Email không hợp lệ")
    private String email;

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getTopic() { return topic; }
    public void setTopic(String topic) { this.topic = topic; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
