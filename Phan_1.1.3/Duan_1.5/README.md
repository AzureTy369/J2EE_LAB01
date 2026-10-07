# Dự án 1.5 - `@Autowired` và `@Qualifier`

Có hai implementation của `PaymentService`. `OrderService` dùng `@Autowired` kết hợp `@Qualifier("cardPayment")` để Spring chọn đúng implementation.

```powershell
cd D:\J2EE\Chuong_1\Phan_1.1.3\Duan_1.5
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" compile exec:java "-Dexec.mainClass=com.example.autowired.AutowiredApplication"
```
