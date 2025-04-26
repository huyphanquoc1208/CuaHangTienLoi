package cuahangtienloi;

import connectDB.DatabaseConnection;
import gui.EmployeeManagement;
import gui.InvoiceManagement;
import gui.PaymentManagement;
import gui.ProductManagement;
import gui.StatisticsManagement;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.SQLException;

public class Main extends JFrame {
    private JPanel navPanel, mainPanel;
    private JButton btnEmployee, btnProduct, btnPayment, btnInvoice, btnStatistics, btnLogout;
    private JLabel lblTitle;

    public Main() {
        // Kiểm tra kết nối cơ sở dữ liệu
        try {
            Connection connection = DatabaseConnection.getConnection();
            connection.close(); // Đóng ngay sau khi kiểm tra để tiết kiệm tài nguyên
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Thiết lập cửa sổ chính
        setTitle("Hệ Thống Quản Lý Cửa Hàng");
        //setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// cửa sổ full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        createNavigationPanel();
        createMainPanel();

        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

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

        // Sự kiện điều hướng
        btnEmployee.addActionListener(e -> navigateTo(new EmployeeManagement()));
        btnProduct.addActionListener(e -> navigateTo(new ProductManagement()));
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
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(new Color(240, 240, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        lblTitle = new JLabel("CHÀO MỪNG ĐẾN VỚI HỆ THỐNG QUẢN LÝ CỬA HÀNG", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        mainPanel.add(lblTitle, BorderLayout.NORTH);
    }

    private void navigateTo(JFrame frame) {
        frame.setVisible(true);
        this.dispose();
    }

    public static void main(String[] args) {
        try {
            // Thiết lập giao diện hệ thống
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        SwingUtilities.invokeLater(() -> new Main());
    }
}