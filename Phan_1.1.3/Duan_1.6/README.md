# Dự án 1.6 - Custom Scope trong Spring

Tạo scope `thread` bằng cách cài đặt `org.springframework.beans.factory.config.Scope`. Bean được lưu trong `ThreadLocal`: cùng một thread dùng chung bean, thread khác nhận bean mới.

```powershell
cd D:\J2EE\Chuong_1\Phan_1.1.3\Duan_1.6
& "$env:TEMP\apache-maven-3.9.11\bin\mvn.cmd" compile exec:java "-Dexec.mainClass=com.example.customscope.CustomScopeApplication"
```
