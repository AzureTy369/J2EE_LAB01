# Dự án 1.2 - Spring Scope với Prototype

Dự án minh họa `@Scope("prototype")`. Mỗi lần gọi `context.getBean(...)`, Spring tạo một đối tượng `VehicleServices` mới, nên hai hashcode khác nhau và phép so sánh `==` trả về `false`.

## Chạy

```powershell
cd D:\J2EE\Chuong_1\Phan_1.1.3\Duan_1.2
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" compile exec:java "-Dexec.mainClass=com.example.scope.ScopeApplication"
```

Kết quả mong đợi:

```text
VehicleServices bean is a prototype scoped bean
```

Nếu đổi `@Scope("prototype")` thành `@Scope("singleton")`, hai lần lấy bean sẽ trả về cùng một đối tượng.
