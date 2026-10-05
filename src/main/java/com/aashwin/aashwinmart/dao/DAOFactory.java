package com.aashwin.aashwinmart.dao;

/**
 * Factory class implementing the Factory Design Pattern for DAO creation and management.
 */
public final class DAOFactory {

    private static final UserDAO USER_DAO = new UserDAOImpl();
    private static final ProductDAO PRODUCT_DAO = new ProductDAOImpl();
    private static final CartItemDAO CART_ITEM_DAO = new CartItemDAOImpl();
    private static final OrderItemDAO ORDER_ITEM_DAO = new OrderItemDAOImpl();
    private static final OrderDAO ORDER_DAO = new OrderDAOImpl(ORDER_ITEM_DAO);
    private static final ReviewDAO REVIEW_DAO = new ReviewDAOImpl();

    private DAOFactory() {
    }

    public static UserDAO getUserDAO() {
        return USER_DAO;
    }

    public static ProductDAO getProductDAO() {
        return PRODUCT_DAO;
    }

    public static CartItemDAO getCartItemDAO() {
        return CART_ITEM_DAO;
    }

    public static OrderItemDAO getOrderItemDAO() {
        return ORDER_ITEM_DAO;
    }

    public static OrderDAO getOrderDAO() {
        return ORDER_DAO;
    }

    public static ReviewDAO getReviewDAO() {
        return REVIEW_DAO;
    }
}
