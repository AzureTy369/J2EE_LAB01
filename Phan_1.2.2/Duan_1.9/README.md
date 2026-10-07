# Dự án 1.9 - Spring Boot and Thymeleaf

Làm theo bài viết: <https://o7planning.org/11545/spring-boot-and-thymeleaf>

Ứng dụng web **liệt kê danh sách các thành viên của công ty** và cho phép thêm thành viên mới qua form. Giao diện dùng **Thymeleaf**.

Công nghệ: Spring Boot 4.1.1, Thymeleaf, Java 17, Maven.

## Cấu trúc dự án

```
Duan_1.9
├── pom.xml
└── src
    ├── main
    │   ├── java/com/example/thymeleaf
    │   │   ├── SpringBootThymeleafApplication.java   ★ điểm vào: hàm main()
    │   │   ├── controller/MainController.java         xử lý 4 URL
    │   │   ├── model/Person.java                      dữ liệu một thành viên
    │   │   └── form/PersonForm.java                   nhận dữ liệu từ form
    │   └── resources
    │       ├── application.properties                 cổng 8083 + các câu thông báo
    │       ├── static/css/style.css                   file tĩnh, trình duyệt tải trực tiếp
    │       └── templates                              view Thymeleaf
    │           ├── index.html                         trang chào
    │           ├── personList.html                    bảng danh sách
    │           └── addPerson.html                     form thêm mới
    └── test/java/com/example/thymeleaf/MainControllerTest.java   4 test
```

## Cách chạy trên IntelliJ IDEA

### Bước 1. Mở dự án

1. **File → Open…**, chọn thư mục `Duan_1.9`, rồi bấm **OK**. Nếu được hỏi, chọn **Trust Project**.
2. Nếu bạn đang mở thư mục cha chứa nhiều dự án: chuột phải vào `Duan_1.9/pom.xml` và chọn **Add as Maven Project**.
3. **File → Project Structure… → Project**: chọn **SDK** là JDK 17 trở lên.

### Bước 2. Chạy ứng dụng

1. Mở file `src/main/java/com/example/thymeleaf/SpringBootThymeleafApplication.java`.
2. Bấm ▶ bên cạnh tên lớp, rồi chọn **Run 'SpringBootThymeleafApplication'**.
3. Chờ dòng `Tomcat started on port 8083`.

Hoặc dùng Terminal (`Alt+F12`), đứng ở thư mục `Duan_1.9`:

```powershell
mvn spring-boot:run
```

### Bước 3. Kiểm tra kết quả

1. Mở <http://localhost:8083/>: thấy tiêu đề **Welcome** màu xanh và dòng **Hello Thymeleaf** màu đỏ.
2. Bấm **Person List**: bảng có Bill Gates và Steve Jobs.
3. Bấm **Add Person**, nhập First Name và Last Name, rồi bấm **Create**: quay về danh sách và thấy người mới.
4. Thử để trống một ô rồi bấm **Create**: hiện dòng chữ đỏ *First Name & Last Name is required!*

### Bước 4. Chạy test

Chuột phải `src/test/java` và chọn **Run 'All Tests'**, hoặc gõ `mvn test`. Kết quả đúng: **4 test đều đạt**.

## Giải thích cách hoạt động

### 1. Mô hình MVC trong Spring

```
                 ┌─────────────── Controller ───────────────┐
Trình duyệt ───▶ │ MainController                           │
   request       │  - đọc/ghi Model (danh sách persons)     │
                 │  - trả về TÊN VIEW, ví dụ "personList"   │
                 └───────────────────┬──────────────────────┘
                                     ▼
                 Thymeleaf tìm templates/personList.html
                 và thay các thuộc tính th:* bằng dữ liệu trong Model
                                     ▼
Trình duyệt ◀─── HTML hoàn chỉnh
```

So với Dự án 1.11 (Servlet + JSP), Spring làm thay nhiều việc:
* Không cần `request.getParameter(...)`: Spring tự điền dữ liệu form vào `PersonForm`.
* Không cần `RequestDispatcher.forward(...)`: chỉ cần trả về tên view.
* Không cần cài Tomcat riêng: Tomcat được nhúng sẵn.

### 2. Các URL do `MainController` xử lý

| Method | URL | Phương thức | Việc làm | View |
|---|---|---|---|---|
| GET | `/` hoặc `/index` | `index()` | Đưa `welcome.message` vào Model | `index` |
| GET | `/personList` | `personList()` | Đưa danh sách `persons` vào Model | `personList` |
| GET | `/addPerson` | `showAddPersonPage()` | Tạo `PersonForm` rỗng cho form | `addPerson` |
| POST | `/addPerson` | `savePerson()` | Kiểm tra dữ liệu, thêm vào danh sách | `redirect:/personList`, hoặc `addPerson` kèm lỗi |

**Lấy cấu hình bằng `@Value`:**

```java
@Value("${welcome.message}")
private String message;          // = "Hello Thymeleaf" trong application.properties
```

Nhờ vậy muốn đổi câu thông báo chỉ cần sửa `application.properties`, không phải sửa code.

**Post/Redirect/Get:** sau khi thêm thành công, controller trả `"redirect:/personList"`. Trình duyệt nhận mã 302 và tự gửi `GET /personList`. Nhờ vậy bấm F5 không gửi lại form và không bị thêm trùng người.

### 3. Các thuộc tính Thymeleaf đã dùng

| Thuộc tính | Ví dụ | Ý nghĩa |
|---|---|---|
| `th:href="@{...}"` | `th:href="@{/css/style.css}"` | Tạo URL đúng theo context path của ứng dụng |
| `th:utext` | `th:utext="${message}"` | In giá trị **không** escape HTML |
| `th:text` | `th:text="${person.firstName}"` | In giá trị **có** escape HTML (an toàn) |
| `th:each` | `th:each="person : ${persons}"` | Lặp, mỗi phần tử sinh ra một `<tr>` |
| `th:object` + `th:field` | `th:object="${personForm}"`, `th:field="*{firstName}"` | Gắn form với đối tượng. `*{firstName}` sinh ra `name="firstName"` và điền sẵn giá trị cũ |
| `th:if` | `th:if="${errorMessage}"` | Chỉ hiện thẻ khi có lỗi |

> Trong `personList.html`, dự án dùng `th:text` thay cho `th:utext` của bài gốc. Lý do: tên người do người dùng nhập. Nếu ai đó nhập `<script>...</script>` thì `th:utext` sẽ chạy đoạn script đó (lỗi XSS), còn `th:text` chỉ hiện nó như chữ thường.

### 4. File tĩnh

Mọi file trong `src/main/resources/static` được phục vụ trực tiếp, không qua controller. Ví dụ `static/css/style.css` có URL `/css/style.css`.

### 5. Dữ liệu

Danh sách `persons` là một `static List` trong controller. Dữ liệu chỉ nằm trong bộ nhớ nên **tắt ứng dụng là mất** những người vừa thêm. Muốn lưu lâu dài thì dùng Spring Data JPA như Dự án 1.8.

## Lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `Port 8083 was already in use` | Ứng dụng đang chạy ở chỗ khác. Tắt nó hoặc đổi `server.port` |
| `Could not resolve placeholder 'welcome.message'` | Thiếu dòng `welcome.message=...` trong `application.properties` |
| `Error resolving template [xxx]` | Tên view trả về không khớp với tên file trong `templates/` |
| Sửa HTML mà không thấy thay đổi | Kiểm tra đã có `spring.thymeleaf.cache=false`. Trong IntelliJ, bấm **Build → Build Project** (`Ctrl+F9`) để copy file mới vào `target`, rồi refresh trình duyệt |
