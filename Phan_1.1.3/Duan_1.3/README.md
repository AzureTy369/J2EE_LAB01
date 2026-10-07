# Dự án 1.3 - Singleton và Prototype Scope

Minh họa `@Scope("singleton")` và `@Scope("prototype")`. Singleton trả về cùng một object; prototype tạo object mới ở mỗi lần `getBean`.

```powershell
cd D:\J2EE\Chuong_1\Phan_1.1.3\Duan_1.3
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" compile exec:java "-Dexec.mainClass=com.example.scope.ScopeApplication"
```
