# Dự án 1.1 - Spring Container với Java Configuration

Dự án minh họa cách tạo Spring Container bằng `SpringApplication`, cấu hình `GreetingService` trong `AppConf` và truy vấn bean từ context.

## Chạy

```powershell
cd D:\J2EE\Chuong_1\Phan_1.1.3\Duan_1.1
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" spring-boot:run
```

Vì đây là ứng dụng console không có web server, cách chạy trực tiếp lớp Java cũng được thực hiện sau khi build:

```powershell
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" package
java -cp "target\classes;$env:USERPROFILE\.m2\repository\org\springframework\spring-context\7.1.2\spring-context-7.1.2.jar" com.example.container.GreetingApplication
```

Kết quả mong đợi:

```text
Xin chào, Spring Container!
Bean name: greetingService
Bean type: GreetingService
```

Lệnh Maven khuyến nghị:

```powershell
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" exec:java "-Dexec.mainClass=com.example.container.GreetingApplication"
```
