# Dự án 1.11 - J2EE

Dự án này là phiên bản mẫu cho bài tập Java EE/J2EE, được xây dựng theo kiểu web đơn giản với Servlet + JSP.

> Lưu ý: file hướng dẫn chi tiết của bài tập chưa có trong workspace hiện tại, nên mình đã tạo một project mẫu đúng cấu trúc J2EE và có thể mở rộng theo đúng yêu cầu nếu bạn cung cấp file đề bài.

## Cấu trúc chính

- `src/main/java/com/example/model/Product.java`: model dữ liệu
- `src/main/java/com/example/service/ProductService.java`: xử lý dữ liệu
- `src/main/java/com/example/controller/ProductServlet.java`: controller Servlet
- `src/main/webapp/WEB-INF/views/products.jsp`: giao diện hiển thị và thêm dữ liệu

## Chạy thử

1. Tải servlet API:
   `Invoke-WebRequest -Uri "https://repo1.maven.org/maven2/javax/servlet/javax.servlet-api/4.0.1/javax.servlet-api-4.0.1.jar" -OutFile "D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.11\libs\javax.servlet-api-4.0.1.jar"`
2. Chạy build script:
   `D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.11\build.bat`
3. Để chạy ứng dụng thực tế, cần Tomcat/Jetty/GlassFish và deploy file WAR.

## Mục tiêu mẫu

- Hiển thị danh sách sản phẩm
- Thêm sản phẩm mới qua form
- Sử dụng Servlet + JSP theo mô hình MVC cơ bản
