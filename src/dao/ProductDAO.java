package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import entity.Product;

public class ProductDAO {
    private Connection connection;

    public ProductDAO(Connection connection) {
        this.connection = connection;
    }

    // Thêm sản phẩm mới
    public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO products (product_id, product_name, quantity, product_type, price) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getProductId());
            stmt.setString(2, product.getProductName());
            stmt.setInt(3, product.getQuantity());
            stmt.setString(4, product.getProductType());
            stmt.setDouble(5, product.getPrice());
            stmt.executeUpdate();
        }
    }

    // Lấy tất cả sản phẩm
    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Product product = new Product();
                product.setProductId(rs.getString("product_id"));
                product.setProductName(rs.getString("product_name"));
                product.setQuantity(rs.getInt("quantity"));
                product.setProductType(rs.getString("product_type"));
                product.setPrice(rs.getDouble("price"));
                products.add(product);
            }
        }
        return products;
    }

    // Lấy sản phẩm theo ID
    public Product getProductById(String productId) throws SQLException {
        String sql = "SELECT * FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getString("product_id"));
                    product.setProductName(rs.getString("product_name"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setProductType(rs.getString("product_type"));
                    product.setPrice(rs.getDouble("price"));
                    return product;
                }
            }
        }
        return null;
    }

    // Cập nhật thông tin sản phẩm
    public void updateProduct(Product product) throws SQLException {
        String sql = "UPDATE products SET product_name = ?, quantity = ?, product_type = ?, price = ? WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setInt(2, product.getQuantity());
            stmt.setString(3, product.getProductType());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getProductId());
            stmt.executeUpdate();
        }
    }

    // Xóa sản phẩm
    public void deleteProduct(String productId) throws SQLException {
        String sql = "DELETE FROM products WHERE product_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, productId);
            stmt.executeUpdate();
        }
    }

    // Xóa tất cả sản phẩm
    public void deleteAllProducts() throws SQLException {
        String sql = "DELETE FROM products";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
    }

    // Tìm kiếm sản phẩm theo ID, tên hoặc loại
    public List<Product> searchProducts(String productId, String productName, String productType) throws SQLException {
        List<Product> products = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT * FROM products WHERE 1=1");
        List<String> params = new ArrayList<>();

        if (!productId.isEmpty()) {
            sql.append(" AND product_id = ?");
            params.add(productId);
        }
        if (!productName.isEmpty()) {
            sql.append(" AND product_name LIKE ?");
            params.add("%" + productName + "%");
        }
        if (!productType.isEmpty()) {
            sql.append(" AND product_type = ?");
            params.add(productType);
        }

        try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product();
                    product.setProductId(rs.getString("product_id"));
                    product.setProductName(rs.getString("product_name"));
                    product.setQuantity(rs.getInt("quantity"));
                    product.setProductType(rs.getString("product_type"));
                    product.setPrice(rs.getDouble("price"));
                    products.add(product);
                }
            }
        }
        return products;
    }
}