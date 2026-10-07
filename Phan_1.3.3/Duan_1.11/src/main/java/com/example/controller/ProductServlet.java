package com.example.controller;

import com.example.model.Product;
import com.example.service.ProductService;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/")
public class ProductServlet extends HttpServlet {
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("products", productService.getAllProducts());
        RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/products.jsp");
        dispatcher.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String category = request.getParameter("category");
        String priceStr = request.getParameter("price");

        if (name != null && !name.trim().isEmpty() && category != null && !category.trim().isEmpty() && priceStr != null && !priceStr.trim().isEmpty()) {
            Product product = new Product();
            product.setName(name.trim());
            product.setCategory(category.trim());
            product.setPrice(Double.parseDouble(priceStr));
            productService.addProduct(product);
        }

        response.sendRedirect(request.getContextPath() + "/");
    }
}
