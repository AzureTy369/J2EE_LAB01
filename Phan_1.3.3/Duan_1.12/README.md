# Dự án 1.12 - RESTful API tra cứu điểm

Ứng dụng Spring Boot cung cấp API cho web application hoặc mobile application tra cứu điểm thí sinh theo số báo danh (SBD). API chỉ trả dữ liệu khi `User-Token` hợp lệ.

## Chạy ứng dụng

Yêu cầu Java 17 trở lên và Maven:

```powershell
cd D:\J2EE\Chuong_1\Phan_1.3.3\Duan_1.12
$env:API_USER_TOKEN = "token-cua-ban"
mvn spring-boot:run
```

Nếu không đặt biến môi trường, token mặc định cho việc thử local là `demo-token-123`.

## API

### Tra cứu điểm

```http
GET http://localhost:8080/api/diem/tra-cuu?SBD=100001
User-Token: demo-token-123
```

Phản hồi thành công (`200 OK`):

```json
{
  "sbd": "100001",
  "hoTen": "Nguyen Van An",
  "diemToan": 8.50,
  "diemVan": 7.75,
  "diemAnhVan": 8.00
}
```

Để tương thích với client đơn giản, token cũng có thể truyền bằng query string:
`/api/diem/tra-cuu?SBD=100001&User-Token=demo-token-123`. Khi truyền cả hai, header được ưu tiên.

Mã lỗi:

- `400`: thiếu SBD
- `401`: User-Token không hợp lệ hoặc bị thiếu
- `404`: không có thí sinh tương ứng với SBD

Dữ liệu mẫu nằm tại `src/main/resources/data/diem-thi.csv`; có thể thay bằng dữ liệu từ dự án trước nhưng phải giữ đúng 5 cột như dòng tiêu đề.
