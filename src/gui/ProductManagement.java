package gui;

import connectDB.DatabaseConnection;
import dao.ProductDAO;
import entity.Product;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;

public class ProductManagement extends JFrame {
    private JPanel navPanel, mainPanel, formPanel, tablePanel, buttonPanel;
    private JButton btnEmployee, btnProduct, btnPayment, btnInvoice, btnStatistics, btnLogout;
    private JButton btnSearch, btnReset, btnPrint, btnAdd, btnDeleteOne, btnDelete, btnUpdate;
    private JTextField txtProductId, txtProductName, txtPrice, txtQuantity;
    private JComboBox<String> cmbProductType;
    private JTable productTable;
    private JLabel lblTitle;
    private ProductDAO productDAO;
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");

    public ProductManagement() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            productDAO = new ProductDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Quản Lý Sản Phẩm");
        //setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// cửa sổ full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        createNavigationPanel();
        createMainPanel();

        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        loadProductData();

        setVisible(true);
    }

    private void createNavigationPanel() {
        navPanel = new JPanel();
        navPanel.setLayout(new BorderLayout());
        navPanel.setBackground(new Color(26, 82, 118));
        navPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        leftButtons.setBackground(new Color(26, 82, 118));

        btnEmployee = createNavButton("Nhân viên");
        btnProduct = createNavButton("Sản phẩm");
        btnPayment = createNavButton("Thanh toán");
        btnInvoice = createNavButton("Hóa đơn");
        btnStatistics = createNavButton("Thống kê");

        btnProduct.setBackground(new Color(200, 200, 200));

        leftButtons.add(btnEmployee);
        leftButtons.add(btnProduct);
        leftButtons.add(btnPayment);
        leftButtons.add(btnInvoice);
        leftButtons.add(btnStatistics);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightButtons.setBackground(new Color(26, 82, 118));

        btnLogout = createNavButton("Đăng xuất");
        rightButtons.add(btnLogout);

        navPanel.add(leftButtons, BorderLayout.WEST);
        navPanel.add(rightButtons, BorderLayout.EAST);

        btnEmployee.addActionListener(e -> navigateTo(new EmployeeManagement()));
        btnProduct.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đang ở trang Sản phẩm"));
        btnPayment.addActionListener(e -> navigateTo(new PaymentManagement()));
        btnInvoice.addActionListener(e -> navigateTo(new InvoiceManagement()));
        btnStatistics.addActionListener(e -> navigateTo(new StatisticsManagement()));
        btnLogout.addActionListener(e -> {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn đăng xuất?", "Xác nhận", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });
    }

    private JButton createNavButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(100, 30));
        button.setBackground(Color.WHITE);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
        button.setBorderPainted(true);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        return button;
    }

    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("QUẢN LÝ SẢN PHẨM", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        createFormPanel();
        mainPanel.add(formPanel, BorderLayout.CENTER);

        createTablePanel();
        mainPanel.add(tablePanel, BorderLayout.SOUTH);
    }

    private void createFormPanel() {
        formPanel = new JPanel();
        formPanel.setLayout(new BorderLayout());
        formPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                "Thông tin sản phẩm");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(26, 82, 118));
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        formPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 15, 15));
        fieldsPanel.setBackground(Color.WHITE);

        JPanel idPanel = createFieldPanel("Mã sản Phẩm:");
        txtProductId = new JTextField();
        idPanel.add(txtProductId, BorderLayout.CENTER);

        JPanel pricePanel = createFieldPanel("Mức giá:");
        txtPrice = new JTextField();
        pricePanel.add(txtPrice, BorderLayout.CENTER);

        JPanel namePanel = createFieldPanel("Tên sản phẩm:");
        txtProductName = new JTextField();
        namePanel.add(txtProductName, BorderLayout.CENTER);

        JPanel quantityPanel = createFieldPanel("Số lượng:");
        txtQuantity = new JTextField();
        quantityPanel.add(txtQuantity, BorderLayout.CENTER);

        JPanel typePanel = createFieldPanel("Loại sản phẩm:");
        cmbProductType = new JComboBox<>(new String[]{"", "Đồ ăn", "Đồ uống", "Gia dụng"});
        cmbProductType.setBackground(Color.WHITE);
        typePanel.add(cmbProductType, BorderLayout.CENTER);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        buttonsPanel.setBackground(Color.WHITE);

        btnSearch = createActionButton("Tìm", new Color(70, 130, 180));
        btnReset = createActionButton("Làm mới", new Color(100, 149, 237));
        btnPrint = createActionButton("In", new Color(139, 69, 19));

        buttonsPanel.add(btnSearch);
        buttonsPanel.add(btnReset);
        buttonsPanel.add(btnPrint);

        fieldsPanel.add(idPanel);
        fieldsPanel.add(pricePanel);
        fieldsPanel.add(namePanel);
        fieldsPanel.add(quantityPanel);
        fieldsPanel.add(typePanel);
        fieldsPanel.add(buttonsPanel);

        formPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    private JPanel createFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(120, 25));

        panel.add(label, BorderLayout.WEST);
        return panel;
    }

    private JButton createActionButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(100, 30));
        return button;
    }

    private void createTablePanel() {
        tablePanel = new JPanel(new BorderLayout(0, 10));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        String[] columnNames = {"Mã sản Phẩm", "Tên sản phẩm", "Số lượng", "Loại sản phẩm", "Mức giá"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(model);
        productTable.setRowHeight(30);
        productTable.setSelectionBackground(new Color(173, 216, 230));
        productTable.setGridColor(new Color(200, 200, 200));
        productTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        productTable.getTableHeader().setBackground(new Color(26, 82, 118));
        productTable.getTableHeader().setForeground(Color.WHITE);
        productTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < productTable.getColumnCount(); i++) {
            productTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(productTable);
        scrollPane.setPreferredSize(new Dimension(1000, 600)); // Tăng kích thước bảng
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        btnAdd = createActionButton("Thêm", new Color(46, 139, 87));
        btnDeleteOne = createActionButton("Xóa 1 SP", new Color(178, 34, 34));
        btnDelete = createActionButton("Xóa", new Color(178, 34, 34));
        btnUpdate = createActionButton("Cập nhật", new Color(70, 130, 180));

        buttonPanel.add(btnAdd);
        buttonPanel.add(btnDeleteOne);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnUpdate);

        tablePanel.add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> addProduct());
        btnDeleteOne.addActionListener(e -> deleteSelectedProduct());
        btnDelete.addActionListener(e -> deleteAllProducts());
        btnUpdate.addActionListener(e -> updateProduct());
        btnPrint.addActionListener(e -> printProductList());
        btnSearch.addActionListener(e -> searchProduct());
        btnReset.addActionListener(e -> resetForm());

        productTable.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = productTable.getSelectedRow();
                if (selectedRow >= 0) {
                    displayProductData(selectedRow);
                }
            }
        });
    }

    private void loadProductData() {
        try {
            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);
            for (Product prod : productDAO.getAllProducts()) {
                model.addRow(new Object[]{
                        prod.getProductId(),
                        prod.getProductName(),
                        prod.getQuantity(),
                        prod.getProductType(),
                        currencyFormat.format(prod.getPrice())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addProduct() {
        try {
            Product product = new Product();
            product.setProductId(txtProductId.getText().trim());
            product.setProductName(txtProductName.getText().trim());
            String quantityText = txtQuantity.getText().trim();
            product.setQuantity(quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText));
            product.setProductType(cmbProductType.getSelectedItem().toString());
            String priceText = txtPrice.getText().trim().replace(",", "");
            product.setPrice(priceText.isEmpty() ? 0 : Double.parseDouble(priceText));

            if (product.getProductId().isEmpty() || product.getProductName().isEmpty() || product.getProductType().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã, tên và loại sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            productDAO.addProduct(product);
            loadProductData();
            resetForm();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng hoặc mức giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteSelectedProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm này?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                try {
                    String productId = productTable.getValueAt(selectedRow, 0).toString();
                    productDAO.deleteProduct(productId);
                    loadProductData();
                    resetForm();
                    JOptionPane.showMessageDialog(this, "Đã xóa sản phẩm!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi xóa sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteAllProducts() {
        int option = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa tất cả sản phẩm?", "Xác nhận xóa", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            try {
                productDAO.deleteAllProducts();
                loadProductData();
                resetForm();
                JOptionPane.showMessageDialog(this, "Đã xóa tất cả sản phẩm!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi xóa tất cả sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void updateProduct() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow >= 0) {
            try {
                Product product = new Product();
                product.setProductId(txtProductId.getText().trim());
                product.setProductName(txtProductName.getText().trim());
                String quantityText = txtQuantity.getText().trim();
                product.setQuantity(quantityText.isEmpty() ? 0 : Integer.parseInt(quantityText));
                product.setProductType(cmbProductType.getSelectedItem().toString());
                String priceText = txtPrice.getText().trim().replace(",", "");
                product.setPrice(priceText.isEmpty() ? 0 : Double.parseDouble(priceText));

                if (product.getProductId().isEmpty() || product.getProductName().isEmpty() || product.getProductType().isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mã, tên và loại sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                productDAO.updateProduct(product);
                loadProductData();
                resetForm();
                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
            } catch (SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Số lượng hoặc mức giá không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần cập nhật!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printProductList() {
        JOptionPane.showMessageDialog(this, "Đang in danh sách sản phẩm...");
    }

    private void searchProduct() {
        try {
            String id = txtProductId.getText().trim();
            String name = txtProductName.getText().trim();
            String type = cmbProductType.getSelectedItem().toString();
            if (id.isEmpty() && name.isEmpty() && type.isEmpty()) {
                loadProductData();
                return;
            }

            DefaultTableModel model = (DefaultTableModel) productTable.getModel();
            model.setRowCount(0);
            for (Product prod : productDAO.searchProducts(id, name, type)) {
                model.addRow(new Object[]{
                        prod.getProductId(),
                        prod.getProductName(),
                        prod.getQuantity(),
                        prod.getProductType(),
                        currencyFormat.format(prod.getPrice())
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tìm kiếm sản phẩm!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void resetForm() {
        txtProductId.setText("");
        txtProductName.setText("");
        txtQuantity.setText("");
        cmbProductType.setSelectedIndex(0);
        txtPrice.setText("");
        productTable.clearSelection();
    }

    private void displayProductData(int row) {
        txtProductId.setText(productTable.getValueAt(row, 0).toString());
        txtProductName.setText(productTable.getValueAt(row, 1).toString());
        txtQuantity.setText(productTable.getValueAt(row, 2).toString());
        cmbProductType.setSelectedItem(productTable.getValueAt(row, 3).toString());
        txtPrice.setText(productTable.getValueAt(row, 4).toString().replace(",", ""));
    }

    private void navigateTo(JFrame frame) {
        frame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new ProductManagement());
    }
}