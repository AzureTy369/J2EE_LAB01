<%@ page import="java.util.List, com.example.model.Product" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<% 
    List<Product> products = (List<Product>) request.getAttribute("products");
    if (products == null) {
        products = java.util.Collections.emptyList();
    }
%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Quản lý sản phẩm</title>
    <style>
        body { font-family: Arial, sans-serif; background: #f6f7fb; margin: 0; padding: 30px; }
        .container { max-width: 900px; margin: 0 auto; background: #fff; padding: 24px; border-radius: 12px; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
        h1 { text-align: center; color: #2c3e50; }
        form { display: grid; grid-template-columns: repeat(3, 1fr); gap: 12px; margin-bottom: 24px; }
        input, button { padding: 10px 12px; border-radius: 8px; border: 1px solid #dfe3e8; }
        button { background: #1e88e5; color: white; cursor: pointer; }
        table { width: 100%; border-collapse: collapse; }
        th, td { border-bottom: 1px solid #e5e7eb; padding: 10px; text-align: left; }
        th { background: #eef4ff; }
    </style>
</head>
<body>
<div class="container">
    <h1>Quản lý sản phẩm</h1>

    <form method="post" action="${pageContext.request.contextPath}/">
        <input type="text" name="name" placeholder="Tên sản phẩm" required>
        <input type="text" name="category" placeholder="Loại sản phẩm" required>
        <input type="number" name="price" placeholder="Giá" min="0" step="1000" required>
        <button type="submit">Thêm mới</button>
    </form>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Tên</th>
                <th>Loại</th>
                <th>Giá</th>
            </tr>
        </thead>
        <tbody>
            <% for (Product p : products) { %>
            <tr>
                <td><%= p.getId() %></td>
                <td><%= p.getName() %></td>
                <td><%= p.getCategory() %></td>
                <td><%= String.format("%,.0f", p.getPrice()) %> VNĐ</td>
            </tr>
            <% } %>
        </tbody>
    </table>
</div>
</body>
</html>
