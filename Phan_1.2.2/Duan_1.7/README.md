# Dự án 1.7 - Building an Application with Spring Boot

Làm theo hướng dẫn chính hãng: <https://spring.io/guides/gs/spring-boot>

Mục tiêu của bài:
* Tạo một **RESTful API** đơn giản bằng Spring Boot.
* Biết cách **kiểm thử** một dự án Spring: unit test với MockMvc và integration test với server thật.
* Biết cách **theo dõi vận hành** ứng dụng bằng **Spring Boot Actuator**.

Công nghệ: Spring Boot 4.1.1 (Spring Framework 7), Java 17, Maven.

## Cấu trúc dự án

```
Duan_1.7
├── pom.xml                                   khai báo thư viện (starter)
└── src
    ├── main
    │   ├── java/com/example/springboot
    │   │   ├── Application.java              ★ điểm vào: hàm main()
    │   │   └── HelloController.java          REST controller: GET /
    │   └── resources
    │       └── application.properties        cổng 8081 + cấu hình Actuator
    └── test/java/com/example/springboot
        ├── HelloControllerTest.java          test bằng MockMvc (không mở cổng mạng)
        └── HelloControllerIntegrationTest.java  test với server thật ở cổng ngẫu nhiên
```

## Cách chạy trên IntelliJ IDEA

### Bước 1. Mở dự án

1. **File → Open…**, chọn thư mục `Duan_1.7`, rồi bấm **OK**. Nếu được hỏi, chọn **Trust Project**.
2. Nếu bạn đang mở thư mục cha chứa nhiều dự án: chuột phải vào `Duan_1.7/pom.xml` và chọn **Add as Maven Project**.
3. Chờ IntelliJ tải xong thư viện (thanh tiến trình ở góc dưới bên phải).
4. **File → Project Structure… → Project**: chọn **SDK** là JDK 17 trở lên.

### Bước 2. Chạy ứng dụng

Đây là ứng dụng Spring Boot **có hàm `main()`** và chạy được trực tiếp:

1. Mở file `src/main/java/com/example/springboot/Application.java`.
2. Bấm biểu tượng ▶ màu xanh bên cạnh dòng `public class Application`, rồi chọn **Run 'Application'**.
3. Cửa sổ **Run** in ra danh sách tất cả bean mà Spring Boot đã tạo. Chờ đến khi thấy dòng `Tomcat started on port 8081`.

Hoặc chạy bằng Terminal của IntelliJ (`Alt+F12`), đứng ở thư mục `Duan_1.7`:

```powershell
mvn spring-boot:run
```

### Bước 3. Kiểm tra kết quả

| URL | Kết quả |
|---|---|
| <http://localhost:8081/> | `Greetings from Spring Boot!` |
| <http://localhost:8081/actuator> | Danh sách các endpoint của Actuator |
| <http://localhost:8081/actuator/health> | `{"status":"UP", ...}` cùng chi tiết ổ đĩa, ... |
| <http://localhost:8081/actuator/beans> | Tất cả bean trong ứng dụng |
| <http://localhost:8081/actuator/mappings> | Tất cả URL mà ứng dụng xử lý |
| <http://localhost:8081/actuator/metrics> | Các chỉ số: bộ nhớ, CPU, số request, ... |

Tắt ứng dụng từ xa qua Actuator (gõ trong Terminal):

```powershell
Invoke-RestMethod -Method Post -Uri http://localhost:8081/actuator/shutdown -ContentType "application/json"
```

Kết quả: `Shutting down, bye...` và ứng dụng tự dừng.

> Phải có `-ContentType "application/json"`. Thiếu nó, PowerShell gửi kiểu dữ liệu form và Actuator trả về lỗi **415**.

### Bước 4. Chạy test

* Chuột phải vào thư mục `src/test/java` và chọn **Run 'All Tests'**.
* Hoặc gõ trong Terminal: `mvn test`

Kết quả đúng: **3 test đều đạt** (màu xanh).

## Giải thích cách hoạt động

### 1. Khởi động: `Application.java`

```java
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
```

`@SpringBootApplication` gộp 3 annotation:

| Annotation | Ý nghĩa |
|---|---|
| `@Configuration` | Lớp này được phép khai báo bean bằng `@Bean` |
| `@EnableAutoConfiguration` | Spring Boot tự cấu hình dựa trên thư viện có trong classpath. Ví dụ thấy `spring-boot-starter-webmvc` thì tự tạo Tomcat nhúng và `DispatcherServlet` |
| `@ComponentScan` | Quét package `com.example.springboot` để tìm `@Controller`, `@Service`, ... |

`SpringApplication.run(...)` tạo **ApplicationContext** (nơi chứa mọi bean), khởi động Tomcat nhúng ở cổng 8081 rồi bắt đầu nhận request. Không cần cài Tomcat riêng như Dự án 1.11.

Bean `CommandLineRunner` chạy **một lần** ngay sau khi khởi động xong. Nó in ra tên mọi bean, để bạn thấy Spring Boot đã tự tạo rất nhiều bean nhờ auto-configuration.

### 2. Xử lý request: `HelloController.java`

```java
@RestController
public class HelloController {
    @GetMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }
}
```

* `@RestController` = `@Controller` + `@ResponseBody`: giá trị trả về được ghi thẳng vào HTTP response, không đi qua trang HTML.
* `@GetMapping("/")`: gắn phương thức với request `GET /`.

Luồng một request:

```
Trình duyệt ── GET http://localhost:8081/ ──▶ Tomcat nhúng
                                               │
                                               ▼
                                        DispatcherServlet  (Spring MVC tự tạo)
                                               │ tìm phương thức có @GetMapping("/")
                                               ▼
                                        HelloController.index()
                                               │ trả về String
                                               ▼
              ◀── 200 OK "Greetings from Spring Boot!" ──
```

### 3. Kiểm thử

| File | Cách làm | Ưu điểm |
|---|---|---|
| `HelloControllerTest` | `@AutoConfigureMockMvc` + `MockMvc` giả lập request đi thẳng vào `DispatcherServlet` | Nhanh, không mở cổng mạng |
| `HelloControllerIntegrationTest` | `webEnvironment = RANDOM_PORT` khởi động server thật ở cổng ngẫu nhiên. `RestTestClient` gửi HTTP request thật | Kiểm tra toàn bộ ứng dụng như khi chạy thật |

Integration test còn kiểm tra `/actuator/health` trả về `"status":"UP"`.

### 4. Theo dõi vận hành: Spring Boot Actuator

Chỉ cần thêm `spring-boot-starter-actuator` vào `pom.xml`, ứng dụng có thêm các endpoint `/actuator/...`. Cấu hình trong `application.properties`:

```properties
# Chọn endpoint nào được mở qua HTTP (mặc định chỉ có health)
management.endpoints.web.exposure.include=health,info,beans,mappings,metrics,shutdown
# Hiện chi tiết trong /actuator/health
management.endpoint.health.show-details=always
# Cho phép POST /actuator/shutdown (mặc định bị khóa)
management.endpoint.shutdown.access=unrestricted
```

> ⚠️ Khi triển khai thật, **không nên** mở `shutdown` và `beans` công khai. Cần bảo vệ chúng bằng Spring Security (xem Dự án 1.8).

## Lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `Port 8081 was already in use` | Ứng dụng đang chạy ở cửa sổ khác. Tắt nó, hoặc đổi `server.port` trong `application.properties` |
| Không thấy nút ▶ cạnh `Application` | Maven chưa load. Chuột phải `pom.xml` và chọn **Add as Maven Project**, hoặc bấm ⟳ trong tab **Maven** |
| `release version 17 not supported` | **Project Structure → Project → SDK** đang chọn JDK cũ. Chọn JDK 17 trở lên |
| `/actuator/shutdown` trả về 415 | Thiếu `-ContentType "application/json"` (xem bước 3) |
