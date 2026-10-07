# Dự án 1.8 - Bootstrap a Simple Application

Làm theo bài viết: <https://www.baeldung.com/spring-boot-start>

Bài này giới thiệu một số thành phần cơ bản của hệ sinh thái Spring:

| Yêu cầu của đề | Thể hiện trong dự án |
|---|---|
| a) `spring-boot-starter-web` | REST API `/api/books` (Spring Boot 4 đổi tên thành `spring-boot-starter-webmvc`) |
| a) `spring-boot-starter-data-jpa` | `BookRepository` đọc/ghi CSDL mà không cần viết SQL |
| a) `spring-boot-starter-thymeleaf` | Trang chủ `home.html` |
| a) `spring-boot-starter-security` | `SecurityConfig`: phân quyền xem / thêm / sửa / xóa |
| b) Làm việc với CSDL quan hệ | H2 chạy trong bộ nhớ, bảng `BOOK`, xem trực tiếp qua `/h2-console` |
| c) Bảo mật ứng dụng web | Đăng nhập HTTP Basic, phân quyền theo vai trò `ADMIN` / `USER` |

Công nghệ: Spring Boot 4.1.1, Java 17, Maven, H2 Database.

## Cấu trúc dự án

```
Duan_1.8
├── pom.xml
└── src
    ├── main
    │   ├── java/com/example/bootstrap
    │   │   ├── SpringBootBootstrapApplication.java   ★ điểm vào: hàm main()
    │   │   ├── config/SecurityConfig.java             phân quyền + tài khoản
    │   │   ├── persistence
    │   │   │   ├── model/Book.java                    @Entity ↔ bảng BOOK
    │   │   │   └── repo/BookRepository.java           truy cập CSDL (Spring Data JPA)
    │   │   └── web
    │   │       ├── BookController.java                REST API /api/books (JSON)
    │   │       ├── SimpleController.java              trang chủ / (HTML)
    │   │       ├── RestExceptionHandler.java          chuyển exception → mã lỗi HTTP
    │   │       └── exception/                         BookNotFoundException, BookIdMismatchException
    │   └── resources
    │       ├── application.properties                 cổng 8082, cấu hình H2, JPA, Thymeleaf
    │       ├── data.sql                               3 cuốn sách mẫu, nạp khi khởi động
    │       └── templates/home.html                    giao diện Thymeleaf
    └── test/java/com/example/bootstrap/BookApiTest.java   9 test
```

## Cách chạy trên IntelliJ IDEA

### Bước 1. Mở dự án

1. **File → Open…**, chọn thư mục `Duan_1.8`, rồi bấm **OK**. Nếu được hỏi, chọn **Trust Project**.
2. Nếu bạn đang mở thư mục cha chứa nhiều dự án: chuột phải vào `Duan_1.8/pom.xml` và chọn **Add as Maven Project**.
3. **File → Project Structure… → Project**: chọn **SDK** là JDK 17 trở lên.

### Bước 2. Chạy ứng dụng

1. Mở file `src/main/java/com/example/bootstrap/SpringBootBootstrapApplication.java`.
2. Bấm ▶ bên cạnh tên lớp, rồi chọn **Run 'SpringBootBootstrapApplication'**.
3. Chờ dòng `Tomcat started on port 8082`. Phía trên sẽ thấy các câu SQL mà Hibernate tự sinh (`create table book ...`).

Hoặc dùng Terminal (`Alt+F12`), đứng ở thư mục `Duan_1.8`:

```powershell
mvn spring-boot:run
```

### Bước 3. Kiểm tra bằng trình duyệt

| URL | Kết quả |
|---|---|
| <http://localhost:8082/> | Trang chủ: `Welcome to Bootstrap Spring Boot` và bảng 3 cuốn sách |
| <http://localhost:8082/api/books> | JSON danh sách sách |
| <http://localhost:8082/api/books/1> | JSON sách có id = 1 |
| <http://localhost:8082/api/books/99> | **404** `Book not found` |
| <http://localhost:8082/h2-console> | Trang quản trị CSDL (xem bên dưới) |

**Xem CSDL bằng H2 console:** mở `/h2-console`, điền **JDBC URL** là `jdbc:h2:mem:bootapp`, **User Name** là `sa`, **Password** để trống, rồi bấm **Connect**. Gõ `SELECT * FROM BOOK` để xem dữ liệu.

### Bước 4. Thử thêm / sửa / xóa (cần đăng nhập)

Ứng dụng có sẵn 2 tài khoản:

| Tài khoản | Mật khẩu | Vai trò | Quyền |
|---|---|---|---|
| `admin` | `admin123` | ADMIN | Xem, thêm, sửa, xóa |
| `user` | `user123` | USER | Chỉ xem |

Gõ lần lượt trong Terminal (PowerShell):

```powershell
# Tạo header đăng nhập HTTP Basic cho admin
$admin = @{ Authorization = "Basic " + [Convert]::ToBase64String([Text.Encoding]::UTF8.GetBytes("admin:admin123")) }

# Thêm sách -> 201 Created, trả về sách mới có id
Invoke-RestMethod -Method Post -Uri http://localhost:8082/api/books -Headers $admin -ContentType "application/json" -Body '{"title":"Refactoring","author":"Martin Fowler"}'

# Sửa sách id = 4 (id trong URL phải trùng id trong body)
Invoke-RestMethod -Method Put -Uri http://localhost:8082/api/books/4 -Headers $admin -ContentType "application/json" -Body '{"id":4,"title":"Refactoring 2nd","author":"Martin Fowler"}'

# Xóa sách id = 4
Invoke-RestMethod -Method Delete -Uri http://localhost:8082/api/books/4 -Headers $admin

# Thêm sách khi KHÔNG đăng nhập -> lỗi 401 Unauthorized
Invoke-RestMethod -Method Post -Uri http://localhost:8082/api/books -ContentType "application/json" -Body '{"title":"X","author":"Y"}'
```

Nếu dùng **Postman**: tab **Authorization → Basic Auth**, nhập `admin` / `admin123`.

### Bước 5. Chạy test

Chuột phải `src/test/java` và chọn **Run 'All Tests'**, hoặc gõ `mvn test`. Kết quả đúng: **9 test đều đạt**.

## Giải thích cách hoạt động

### 1. Tầng dữ liệu: Entity + Repository (Spring Data JPA)

```java
@Entity
public class Book {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(nullable = false, unique = true)
    private String title;
    @Column(nullable = false)
    private String author;
```

* `@Entity`: mỗi đối tượng `Book` ứng với một dòng trong bảng `BOOK`.
* Vì `spring.jpa.hibernate.ddl-auto=create-drop`, khi khởi động Hibernate **tự tạo bảng** từ lớp này, khi tắt thì xóa bảng.
* `unique = true` trên `title`: CSDL không cho hai sách trùng tên.

```java
public interface BookRepository extends CrudRepository<Book, Long> {
    List<Book> findByTitle(String title);
}
```

* Đây chỉ là **interface**, không có code cài đặt. Spring Data JPA tự sinh lớp cài đặt lúc chạy.
* Kế thừa `CrudRepository` là có sẵn `save`, `findById`, `findAll`, `deleteById`, ...
* `findByTitle`: Spring đọc **tên phương thức** để sinh câu truy vấn `WHERE title = ?`.

`data.sql` được chạy sau khi Hibernate tạo bảng xong, nhờ `spring.jpa.defer-datasource-initialization=true`. File này nạp 3 cuốn sách mẫu.

### 2. Tầng web: REST API + trang HTML

| Lớp | Annotation | Trả về |
|---|---|---|
| `BookController` | `@RestController` + `@RequestMapping("/api/books")` | **JSON**. Jackson tự chuyển `Book` thành JSON |
| `SimpleController` | `@Controller` | **Tên view** `"home"`. Thymeleaf tìm `templates/home.html` rồi điền dữ liệu |

Các API trong `BookController`:

| Method | URL | Chức năng | Quyền |
|---|---|---|---|
| GET | `/api/books` | Lấy tất cả | Ai cũng được |
| GET | `/api/books/{id}` | Lấy theo id, không có thì 404 | Ai cũng được |
| GET | `/api/books/title/{title}` | Tìm theo tên | Ai cũng được |
| POST | `/api/books` | Thêm mới, trả 201 | ADMIN |
| PUT | `/api/books/{id}` | Sửa, id trong URL khác body thì 400 | ADMIN |
| DELETE | `/api/books/{id}` | Xóa | ADMIN |

Trong `home.html`:
* `th:text="${appName}"` in giá trị `spring.application.name` mà controller lấy bằng `@Value`.
* `th:each="book : ${books}"` lặp qua danh sách để tạo các dòng của bảng.

### 3. Xử lý lỗi tập trung: `RestExceptionHandler`

`@ControllerAdvice` áp dụng cho mọi controller. Khi có exception, Spring tìm `@ExceptionHandler` phù hợp:

| Exception | Khi nào | Mã HTTP |
|---|---|---|
| `BookNotFoundException` | Không có sách với id đó | 404 |
| `BookIdMismatchException` | PUT có id trong URL khác id trong body | 400 |
| `DataIntegrityViolationException` | Vi phạm ràng buộc CSDL, ví dụ trùng `title` | 400 |

Nhờ vậy client nhận mã lỗi có ý nghĩa thay vì lỗi 500 chung chung.

### 4. Bảo mật: `SecurityConfig`

Spring Security đặt một chuỗi **filter** chạy **trước** controller. Mọi request đều đi qua đây:

```
Request ──▶ Security Filter Chain ──▶ DispatcherServlet ──▶ Controller
               │
               ├─ GET /, GET /api/books/**, /h2-console/**  → cho qua
               ├─ POST/PUT/DELETE /api/books/**              → cần đăng nhập + vai trò ADMIN
               │      chưa đăng nhập         → 401 Unauthorized
               │      đăng nhập bằng "user"  → 403 Forbidden
               └─ còn lại                                    → cần đăng nhập
```

* **HTTP Basic**: client gửi header `Authorization: Basic base64(user:password)` kèm mỗi request.
* **Tài khoản** được lưu trong bộ nhớ (`InMemoryUserDetailsManager`). Tiền tố `{noop}` nghĩa là mật khẩu không mã hóa. Cách này **chỉ dùng cho bài tập**, ứng dụng thật phải lưu tài khoản trong CSDL và mã hóa mật khẩu bằng BCrypt.
* **CSRF** được tắt cho `/api/**` vì REST API không dùng form và cookie phiên đăng nhập.
* `frameOptions().sameOrigin()`: H2 console hiển thị trong `<frame>`, mặc định Security chặn frame nên phải mở cho cùng nguồn.

> So với bài gốc của Baeldung (cho phép tất cả bằng `permitAll()`), dự án này phân quyền thật để thể hiện yêu cầu c) của đề.

## Lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `Port 8082 was already in use` | Ứng dụng đang chạy ở chỗ khác. Tắt nó hoặc đổi `server.port` |
| H2 console báo `Database "mem:bootapp" not found` | **JDBC URL** phải là đúng `jdbc:h2:mem:bootapp` |
| POST/PUT/DELETE trả về 401 | Chưa gửi tài khoản. Xem bước 4 |
| POST/PUT/DELETE trả về 403 | Đăng nhập bằng `user`. Cần dùng `admin` |
| Thêm sách trả về 400 | Trùng `title` với sách đã có, hoặc thiếu `author` |
| Thêm sách xong, tắt ứng dụng thì mất | Bình thường: H2 chạy trong bộ nhớ (`jdbc:h2:mem:`) |
