package com.aashwin.aashwinmart.controller.common;

import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * Controller rendering the luxurious Royal Gold homepage with featured collections and trust indicators.
 */
@WebServlet(name = "HomeServlet", urlPatterns = {"", "/home"})
public class HomeServlet extends HttpServlet {

    private final ProductService productService;

    public HomeServlet() {
        this.productService = new ProductServiceImpl();
    }

    public HomeServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        List<ProductDTO> featuredProducts = productService.getFeaturedProducts(8);
        List<String> categories = productService.getAllCategories();

        request.setAttribute("featuredProducts", featuredProducts);
        request.setAttribute("categories", categories);
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}
