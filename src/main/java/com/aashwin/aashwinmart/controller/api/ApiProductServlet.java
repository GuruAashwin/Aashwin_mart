package com.aashwin.aashwinmart.controller.api;

import com.aashwin.aashwinmart.dto.ApiResponse;
import com.aashwin.aashwinmart.dto.ProductDTO;
import com.aashwin.aashwinmart.exception.AppException;
import com.aashwin.aashwinmart.service.ProductService;
import com.aashwin.aashwinmart.service.ProductServiceImpl;
import com.aashwin.aashwinmart.util.JsonUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REST API controller for Products.
 * URL: /api/v1/products
 */
@WebServlet(name = "ApiProductServlet", urlPatterns = {"/api/v1/products/*"})
public class ApiProductServlet extends HttpServlet {

    private final ProductService productService;

    public ApiProductServlet() {
        this.productService = new ProductServiceImpl();
    }

    public ApiProductServlet(ProductService productService) {
        this.productService = productService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();

        try {
            if (pathInfo != null && pathInfo.length() > 1) {
                Long productId = Long.parseLong(pathInfo.substring(1));
                ProductDTO product = productService.getProductById(productId);
                JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(product));
                return;
            }

            String keyword = request.getParameter("q");
            String category = request.getParameter("category");
            String sort = request.getParameter("sort");
            int page = 1;
            int limit = 12;

            try {
                if (request.getParameter("page") != null) {
                    page = Math.max(1, Integer.parseInt(request.getParameter("page")));
                }
                if (request.getParameter("limit") != null) {
                    limit = Math.min(50, Math.max(1, Integer.parseInt(request.getParameter("limit"))));
                }
            } catch (NumberFormatException ignored) {
            }

            List<ProductDTO> products = productService.getProductsWithFilters(keyword, category, sort, page, limit);
            int total = productService.countProductsWithFilters(keyword, category);

            Map<String, Object> data = new HashMap<>();
            data.put("products", products);
            data.put("total", total);
            data.put("page", page);
            data.put("limit", limit);

            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_OK, ApiResponse.success(data));
        } catch (AppException e) {
            JsonUtil.writeJsonResponse(response, e.getHttpStatus(), ApiResponse.error(e.getErrorCode(), e.getMessage()));
        } catch (Exception e) {
            JsonUtil.writeJsonResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ApiResponse.error("INTERNAL_SERVER_ERROR", "An unexpected error occurred."));
        }
    }
}
