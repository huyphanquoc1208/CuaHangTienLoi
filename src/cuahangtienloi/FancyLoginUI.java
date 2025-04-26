package cuahangtienloi;

import cuahangtienloi.Main;

import javax.swing.*;
import java.awt.*;

public class FancyLoginUI extends JFrame {

    public FancyLoginUI() {
        setTitle("Đăng nhập");
        setSize(600, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Panel chính chứa avatar + form login
        JPanel mainPanel = new JPanel(new GridLayout(1, 2));
        mainPanel.setBackground(Color.WHITE);

        // ===== Avatar bên trái =====
        JLabel avatarLabel = new JLabel();
        avatarLabel.setHorizontalAlignment(JLabel.CENTER);
        ImageIcon img = new ImageIcon("img/Login.jpg");

        // Resize ảnh (200x200)
        Image scaledImage = img.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(scaledImage);
        avatarLabel.setIcon(resizedIcon);

        mainPanel.add(avatarLabel);

        // ===== Form login bên phải =====
        JPanel loginPanel = new JPanel();
        loginPanel.setBackground(Color.WHITE);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(40, 30, 40, 30));

        JLabel lblTitle = new JLabel("Đăng nhập", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        loginPanel.add(lblTitle);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JTextField tfEmail = new JTextField();
        tfEmail.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        tfEmail.setBorder(BorderFactory.createTitledBorder("  📧 Tên đăng nhập"));
        loginPanel.add(tfEmail);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 10)));

        JPasswordField pfPassword = new JPasswordField();
        pfPassword.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        pfPassword.setBorder(BorderFactory.createTitledBorder("  🔒 Mật khẩu"));
        loginPanel.add(pfPassword);
        loginPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        JButton btnLogin = new JButton("Đăng nhập");
        btnLogin.setBackground(new Color(0, 200, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginPanel.add(btnLogin);

        loginPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        mainPanel.add(loginPanel);
        add(mainPanel, BorderLayout.CENTER);

        // Sự kiện đăng nhập
        btnLogin.addActionListener(e -> {
            String username = tfEmail.getText().trim();
            String password = new String(pfPassword.getPassword()).trim();

            if (username.equals("admin") && password.equals("123")) {
                // Đăng nhập thành công, chuyển đến Main
                SwingUtilities.invokeLater(() -> {
                    new Main().setVisible(true);
                    dispose(); // Đóng cửa sổ đăng nhập
                });
            } else {
                // Đăng nhập thất bại, hiển thị thông báo
                JOptionPane.showMessageDialog(this, "Số điện thoại hoặc mật khẩu không đúng!", "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
                tfEmail.setText("");
                pfPassword.setText("");
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(FancyLoginUI::new);
    }
}