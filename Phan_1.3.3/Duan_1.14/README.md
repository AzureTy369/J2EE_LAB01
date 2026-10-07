# Dự án 1.14 - Ứng dụng phản hồi dịch vụ

Website thu thập ý kiến khách hàng theo yêu cầu Chương 1, phần 1.3.3. Giao diện responsive gồm đánh giá sao, chủ đề, nội dung phản hồi và thông tin liên hệ tùy chọn.

## Chạy local

```powershell
cd D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.14
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run
```

Mở `http://localhost:8080`.

Nếu cổng 8080 đang được dự án khác sử dụng:

```powershell
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run "-Dspring-boot.run.arguments=--server.port=8082"
```

Sau đó mở `http://localhost:8082`.

## Luồng hoạt động

1. Người dùng chọn số sao, chủ đề và nhập phản hồi.
2. Có thể nhập họ tên/email để được liên hệ lại.
3. Server kiểm tra dữ liệu bằng Bean Validation.
4. Dữ liệu hợp lệ được xử lý tại `FeedbackService` và hiển thị thông báo gửi thành công.

Hiện tại phản hồi được ghi log ở server để phù hợp phạm vi bài tập. Khi triển khai thật, thay `FeedbackService` bằng repository lưu vào cơ sở dữ liệu hoặc gửi email.
