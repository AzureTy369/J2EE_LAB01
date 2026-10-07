# Dự án 1.13 - Website CV cá nhân

Website hiển thị sơ yếu lý lịch cá nhân theo yêu cầu Chương 1, phần 1.3.3. Giao diện responsive, có thể mở trên máy tính/điện thoại và có nút in hoặc lưu thành PDF.

## Chạy local

Yêu cầu Java 17 trở lên. Nếu đã cài Maven:

```powershell
cd D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.13
mvn spring-boot:run
```

Nếu Maven chưa có trong PATH, dùng Maven đã tải ở dự án trước:

```powershell
cd D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.13
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run
```

Mở trình duyệt tại [http://localhost:8080](http://localhost:8080).

Nếu cổng 8080 đang được Dự án 1.12 sử dụng, dừng ứng dụng cũ bằng `Ctrl + C`, hoặc chạy dự án này ở cổng 8081:

```powershell
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run "-Dspring-boot.run.arguments=--server.port=8081"
```

## Tùy chỉnh thông tin

Thay tên, email, kỹ năng, kinh nghiệm và dự án trong:

`src/main/java/com/example/cv/controller/CvController.java`

Thay màu sắc và bố cục trong:

`src/main/resources/static/css/cv.css`

## Đưa lên Internet

Có thể tạo repository GitHub, push thư mục dự án và triển khai bằng Render, Railway hoặc một máy chủ có Java. Khi host, hãy đổi thông tin mẫu trong `CvController.java`, sau đó chạy:

```powershell
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" clean package
java -jar target\duan-1-13.jar
```
