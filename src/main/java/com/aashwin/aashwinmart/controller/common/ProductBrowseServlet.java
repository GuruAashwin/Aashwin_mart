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
 * Controller handling Product Catalog browsing, keyword search, category filtering, and sorting (F3).
 */
@WebServlet(name = "ProductBrowseServlet", urlPatterns = {"/shop", "/products"})
public class ProductBrowseServlet extends HttpServlet {

    private static final int DEFAULT_PAGE_SIZE = 9;
    private final ProductService productService;

    public ProductBrowseServlet() {
        this.productService = new ProductServiceImpl();
    }

    public ProductBrowseServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String keyword = request.getParameter("q");
        String category = request.getParameter("category");
        String sort = request.getParameter("sort");
        String pageStr = request.getParameter("page");

        int page = 1;
        if (pageStr != null) {
            try {
                page = Math.max(1, Integer.parseInt(pageStr));
            } catch (NumberFormatException ignored) {
            }
        }

        List<ProductDTO> products = productService.getProductsWithFilters(keyword, category, sort, page, DEFAULT_PAGE_SIZE);
        int totalProducts = productService.countProductsWithFilters(keyword, category);
        int totalPages = (int) Math.ceil((double) totalProducts / DEFAULT_PAGE_SIZE);
        List<String> categories = productService.getAllCategories();

        request.setAttribute("products", products);
        request.setAttribute("categories", categories);
        request.setAttribute("selectedCategory", category != null ? category : "ALL");
        request.setAttribute("keyword", keyword != null ? keyword : "");
        request.setAttribute("sort", sort != null ? sort : "newest");
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPages", Math.max(1, totalPages));
        request.setAttribute("totalCount", totalProducts);

        request.getRequestDispatcher("/WEB-INF/views/shop.jsp").forward(request, response);
    }
}
