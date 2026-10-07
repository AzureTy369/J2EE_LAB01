# Dự án 1.10 - Captcha ngẫu nhiên

Ứng dụng web **hiển thị một hình captcha khác nhau cho mỗi lần truy cập** vào URL của ứng dụng. Người dùng nhập mã trong hình và ứng dụng kiểm tra đúng hay sai.

Cách làm theo gợi ý của đề:

| Gợi ý | Thể hiện trong dự án |
|---|---|
| a) Tạo dự án web như các bài trên | Spring Boot + Thymeleaf, giống Dự án 1.9 |
| b) Thu thập 20 hình captcha độc đáo | `tools/CaptchaGenerator.java` vẽ 20 hình. Mỗi hình có màu nền, font, góc xoay và đường nhiễu riêng. Hình nằm ở `src/main/resources/static/captcha/` |
| c) Chọn ngẫu nhiên 1 số từ 20 số | `CaptchaService.pickRandom()` |
| d) Hiển thị hình đó trên web | `index.html` hiện hình bằng `<img th:src="...">` |

Công nghệ: Spring Boot 4.1.1, Thymeleaf, Java 17, Maven.

## Cấu trúc dự án

```
Duan_1.10
├── pom.xml
├── tools
│   └── CaptchaGenerator.java              công cụ vẽ 20 hình captcha (chạy riêng, không thuộc web app)
└── src
    ├── main
    │   ├── java/com/example/captcha
    │   │   ├── CaptchaApplication.java        ★ điểm vào: hàm main()
    │   │   ├── controller/CaptchaController.java   GET / và POST /verify
    │   │   └── service/CaptchaService.java    chọn ngẫu nhiên, kiểm tra đáp án
    │   └── resources
    │       ├── application.properties         cổng 8084
    │       ├── captcha-answers.properties     đáp án của 20 hình (chỉ server đọc)
    │       ├── static/captcha/captcha01.png … captcha20.png
    │       └── templates/index.html           giao diện
    └── test/java/com/example/captcha/CaptchaApplicationTest.java   4 test
```

## Cách chạy trên IntelliJ IDEA

### Bước 1. Mở dự án

1. **File → Open…**, chọn thư mục `Duan_1.10`, rồi bấm **OK**. Nếu được hỏi, chọn **Trust Project**.
2. Nếu bạn đang mở thư mục cha chứa nhiều dự án: chuột phải vào `Duan_1.10/pom.xml` và chọn **Add as Maven Project**.
3. **File → Project Structure… → Project**: chọn **SDK** là JDK 17 trở lên.

### Bước 2. Chạy ứng dụng

1. Mở file `src/main/java/com/example/captcha/CaptchaApplication.java`.
2. Bấm ▶ bên cạnh tên lớp, rồi chọn **Run 'CaptchaApplication'**.
3. Chờ dòng `Tomcat started on port 8084`.

Hoặc dùng Terminal (`Alt+F12`), đứng ở thư mục `Duan_1.10`:

```powershell
mvn spring-boot:run
```

### Bước 3. Kiểm tra kết quả

1. Mở <http://localhost:8084/>: thấy một hình captcha và dòng *Hình số X / 20*.
2. Bấm **F5** hoặc **↻ Đổi hình khác** nhiều lần: mỗi lần là một hình khác, không bao giờ lặp lại hình vừa xem.
3. Nhập mã trong hình (không phân biệt hoa thường) rồi bấm **Kiểm tra**: hiện khung xanh *Đúng!* hoặc khung đỏ *Sai*, kèm một hình mới.

### Bước 4. Chạy test

Chuột phải `src/test/java` và chọn **Run 'All Tests'**, hoặc gõ `mvn test`. Kết quả đúng: **4 test đều đạt**:
* Đủ 20 hình đều tải được và có kiểu `image/png`.
* 100 lần truy cập liên tiếp: không lần nào trùng với lần ngay trước, và gặp hơn 10 hình khác nhau.
* Nhập đúng mã thì được chấp nhận, nhập sai thì bị từ chối.

### (Tùy chọn) Vẽ lại 20 hình captcha

Mở Terminal ở thư mục `Duan_1.10` và gõ:

```powershell
java tools/CaptchaGenerator.java
```

Lệnh này ghi đè 20 file `captcha01.png` … `captcha20.png` và file `captcha-answers.properties`, rồi in ra đáp án của từng hình. Chương trình dùng seed cố định nên chạy lại vẫn ra đúng 20 hình như cũ. Muốn có bộ hình mới, đổi số trong `new Random(20261007L)`.

## Giải thích cách hoạt động

### 1. Luồng xử lý

```
① GET /                                    ② POST /verify  (answer=...)
   │                                           │
   ▼                                           ▼
CaptchaController.index()                  CaptchaController.verify()
   │ previous = session["captchaIndex"]        │ index = session["captchaIndex"]
   │ index = pickRandom(previous)  (1..20)     │ ok = check(index, answer)
   │ session["captchaIndex"] = index           │ lưu ok vào flash attribute
   │ model.imageUrl = /captcha/captchaNN.png   │ redirect:/   ──────────▶ quay lại ①
   ▼
index.html  ── <img src="/captcha/captchaNN.png"> ──▶ trình duyệt tải hình từ static/
```

### 2. Chọn ngẫu nhiên: `CaptchaService.pickRandom()`

```java
do {
    index = random.nextInt(TOTAL) + 1;          // nextInt(20) trả 0..19, cộng 1 thành 1..20
} while (previous != null && index == previous);
```

* `SecureRandom` khó đoán hơn `Random` thường, nên người dùng không suy ra được hình tiếp theo.
* Vòng `do … while` chọn lại khi trùng hình vừa hiện. Nhờ vậy **mỗi lần truy cập luôn thấy hình khác**, đúng yêu cầu của đề. Nếu chỉ random thuần thì cứ 20 lần sẽ có khoảng 1 lần trùng.

### 3. Vì sao lưu số thứ tự hình trong session?

* `HttpSession` là vùng nhớ riêng cho từng người dùng trên server. Mỗi trình duyệt được nhận ra qua cookie `JSESSIONID`.
* Server lưu **số thứ tự hình đang hiện** trong session. Khi người dùng gửi đáp án, server biết phải so với hình nào.
* **Đáp án không bao giờ được gửi ra trình duyệt**: nó chỉ nằm trong `captcha-answers.properties` ở phía server. Tên file hình (`captcha07.png`) cũng không tiết lộ mã.
* Mỗi người dùng có session riêng nên hai người mở cùng lúc không ảnh hưởng nhau.

### 4. Flash attribute và Post/Redirect/Get

Sau khi kiểm tra, `verify()` **không** trả về trang HTML ngay. Nó lưu kết quả bằng `RedirectAttributes.addFlashAttribute(...)` rồi chuyển hướng về `/`.
* Flash attribute chỉ tồn tại **qua đúng một lần redirect**. Trang `/` đọc nó để hiện khung Đúng/Sai, sau đó nó tự biến mất.
* Vì quay về `/` nên người dùng nhận luôn một hình mới. Không thể thử nhiều đáp án trên cùng một hình.

### 5. Cách vẽ hình captcha: `tools/CaptchaGenerator.java`

Chương trình dùng **Java 2D** (`BufferedImage` + `Graphics2D`) để vẽ từng hình:
1. Nền gradient. Mỗi hình một tông màu, chia đều trên vòng màu theo số thứ tự, nên 20 hình trông khác nhau rõ rệt.
2. 250 chấm nhiễu và 4 đường cong phía sau chữ.
3. 6 ký tự ngẫu nhiên. Mỗi ký tự có font, cỡ chữ, góc xoay (±25°) và độ cao khác nhau. Bỏ các ký tự dễ nhầm như `0/O` và `1/I/L`.
4. Một đường cong đậm cắt ngang qua chữ, để máy khó tách từng ký tự.
5. Ghi ra file PNG, và ghi đáp án vào `captcha-answers.properties`.

> Captcha dạng 20 hình cố định chỉ phù hợp cho bài tập: kẻ tấn công có thể lưu sẵn đáp án của cả 20 hình. Ứng dụng thật sẽ sinh hình mới cho mỗi lần truy cập, hoặc dùng dịch vụ có sẵn như Google reCAPTCHA hay Cloudflare Turnstile.

## Lỗi thường gặp

| Hiện tượng | Cách xử lý |
|---|---|
| `Port 8084 was already in use` | Ứng dụng đang chạy ở chỗ khác. Tắt nó hoặc đổi `server.port` |
| Hình không hiện (biểu tượng ảnh vỡ) | Kiểm tra trong `src/main/resources/static/captcha/` có đủ 20 file. Nếu thiếu, chạy lại `java tools/CaptchaGenerator.java` |
| Khởi động báo `class path resource [captcha-answers.properties] cannot be opened` | Thiếu file đáp án. Chạy lại `java tools/CaptchaGenerator.java` |
| Nhập đúng mà vẫn báo sai | Session đã hết hạn hoặc đã đổi hình ở tab khác. Hãy nhập mã của hình đang hiện trên trang |
