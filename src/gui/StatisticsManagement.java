package gui;

import connectDB.DatabaseConnection;
import dao.InvoiceDAO;
import entity.Invoice;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class StatisticsManagement extends JFrame {
    private JPanel navPanel, mainPanel, leftPanel, rightPanel;
    private JPanel filterPanel, invoiceTablePanel, chartPanel;
    private JButton btnEmployee, btnProduct, btnPayment, btnInvoice, btnStatistics, btnLogout;
    private JButton btnSearch, btnPrint;
    private JComboBox<String> cmbMonth, cmbYear;
    private JLabel lblTotalAmount;
    private JTable invoiceTable;
    private JLabel lblTitle;
    private DecimalFormat currencyFormat = new DecimalFormat("#,###");
    private InvoiceDAO invoiceDAO;

    public StatisticsManagement() {
        try {
            Connection connection = DatabaseConnection.getConnection();
            invoiceDAO = new InvoiceDAO(connection);
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi kết nối cơ sở dữ liệu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        setTitle("Thống Kê");
        //setSize(1200, 750);
        setExtendedState(JFrame.MAXIMIZED_BOTH);// cửa sổ full màn hình
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        createNavigationPanel();
        createMainPanel();

        add(navPanel, BorderLayout.NORTH);
        add(mainPanel, BorderLayout.CENTER);

        loadInvoiceData();

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
        btnStatistics = createNavButton("Thống Kê");

        btnStatistics.setBackground(new Color(200, 200, 200));

        leftButtons.add(btnEmployee);
        leftButtons.add(btnProduct);
        leftButtons.add(btnPayment);
        leftButtons.add(btnInvoice);
        leftButtons.add(btnStatistics);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        rightButtons.setBackground(new Color(26, 82, 118));

        btnLogout = createNavButton("Đăng suất");
        rightButtons.add(btnLogout);

        navPanel.add(leftButtons, BorderLayout.WEST);
        navPanel.add(rightButtons, BorderLayout.EAST);

        btnEmployee.addActionListener(e -> navigateTo(new EmployeeManagement()));
        btnProduct.addActionListener(e -> navigateTo(new ProductManagement()));
        btnPayment.addActionListener(e -> navigateTo(new PaymentManagement()));
        btnInvoice.addActionListener(e -> navigateTo(new InvoiceManagement()));
        btnStatistics.addActionListener(e -> JOptionPane.showMessageDialog(this, "Đang ở trang Thống Kê"));
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

        lblTitle = new JLabel("THỐNG KÊ DOANH THU", JLabel.CENTER);
        lblTitle.setFont(new Font("Arial", Font.BOLD, 22));
        lblTitle.setForeground(new Color(26, 82, 118));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));
        mainPanel.add(lblTitle, BorderLayout.NORTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setResizeWeight(0.35);
        splitPane.setDividerLocation(650); //Tăng không gian bên trái
        splitPane.setDividerSize(5);
        splitPane.setBorder(null);

        createLeftPanel();
        splitPane.setLeftComponent(leftPanel);

        createRightPanel();
        splitPane.setRightComponent(rightPanel);

        mainPanel.add(splitPane, BorderLayout.CENTER);
    }

    private void createLeftPanel() {
        leftPanel = new JPanel();
        leftPanel.setLayout(new BorderLayout(0, 10));
        leftPanel.setBackground(new Color(240, 240, 240));

        createFilterPanel();
        leftPanel.add(filterPanel, BorderLayout.NORTH);

        createInvoiceTablePanel();
        leftPanel.add(invoiceTablePanel, BorderLayout.CENTER);
    }

    private void createFilterPanel() {
        filterPanel = new JPanel();
        filterPanel.setLayout(new BorderLayout());
        filterPanel.setBackground(Color.WHITE);

        TitledBorder titledBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(26, 82, 118)),
                "Thông tin thống kê");
        titledBorder.setTitleFont(new Font("Arial", Font.BOLD, 14));
        titledBorder.setTitleColor(new Color(26, 82, 118));
        titledBorder.setTitlePosition(TitledBorder.TOP);
        titledBorder.setTitleJustification(TitledBorder.LEFT);
        filterPanel.setBorder(BorderFactory.createCompoundBorder(
                titledBorder,
                BorderFactory.createEmptyBorder(10, 10, 10, 10)));

        JPanel fieldsPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        fieldsPanel.setBackground(Color.WHITE);

        JPanel monthPanel = createFieldPanel("Tháng:");
        String[] months = {"Tất cả", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12"};
        cmbMonth = new JComboBox<>(months);
        cmbMonth.setBackground(Color.WHITE);
        monthPanel.add(cmbMonth, BorderLayout.CENTER);

        JPanel searchBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        searchBtnPanel.setBackground(Color.WHITE);
        btnSearch = createActionButton("Tìm", new Color(70, 130, 180));
        searchBtnPanel.add(btnSearch);

        JPanel yearPanel = createFieldPanel("Năm:");
        String[] years = getYearList();
        cmbYear = new JComboBox<>(years);
        cmbYear.setBackground(Color.WHITE);
        yearPanel.add(cmbYear, BorderLayout.CENTER);

        JPanel printBtnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        printBtnPanel.setBackground(Color.WHITE);
        btnPrint = createActionButton("In", new Color(139, 69, 19));
        printBtnPanel.add(btnPrint);

        JPanel totalPanel = createFieldPanel("Tổng tiền:");
        lblTotalAmount = new JLabel("0 VNĐ");
        lblTotalAmount.setFont(new Font("Arial", Font.BOLD, 14));
        totalPanel.add(lblTotalAmount, BorderLayout.CENTER);

        JPanel emptyPanel = new JPanel();
        emptyPanel.setBackground(Color.WHITE);

        fieldsPanel.add(monthPanel);
        fieldsPanel.add(searchBtnPanel);
        fieldsPanel.add(yearPanel);
        fieldsPanel.add(printBtnPanel);
        fieldsPanel.add(totalPanel);
        fieldsPanel.add(emptyPanel);

        filterPanel.add(fieldsPanel, BorderLayout.CENTER);
    }

    private void createInvoiceTablePanel() {
        invoiceTablePanel = new JPanel(new BorderLayout());
        invoiceTablePanel.setBackground(Color.WHITE);
        invoiceTablePanel.setBorder(BorderFactory.createLineBorder(new Color(26, 82, 118)));

        String[] columnNames = {"Mã hóa đơn", "Ngày", "Tổng tiền"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        invoiceTable = new JTable(model);
        invoiceTable.setRowHeight(30);
        invoiceTable.setSelectionBackground(new Color(173, 216, 230));
        invoiceTable.setGridColor(new Color(200, 200, 200));
        invoiceTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        invoiceTable.getTableHeader().setBackground(new Color(26, 82, 118));
        invoiceTable.getTableHeader().setForeground(Color.WHITE);
        invoiceTable.setFont(new Font("Arial", Font.PLAIN, 12));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < invoiceTable.getColumnCount(); i++) {
            invoiceTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        JScrollPane scrollPane = new JScrollPane(invoiceTable);
        scrollPane.setPreferredSize(new Dimension(600, 400)); // Tăng chiều ngang
        invoiceTablePanel.add(scrollPane, BorderLayout.CENTER);
    }

    private void createRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BorderLayout(0, 10));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.setBorder(BorderFactory.createLineBorder(new Color(26, 82, 118)));

        createChartPanel();
        rightPanel.add(chartPanel, BorderLayout.CENTER);
    }

    private void createChartPanel() {
        chartPanel = new JPanel(new BorderLayout());
        chartPanel.setBackground(Color.WHITE);

        DefaultCategoryDataset dataset = createDataset();

        JFreeChart chart = ChartFactory.createBarChart(
                "Biểu đồ thống kê doanh thu theo năm",
                "Năm",
                "Triệu đồng",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 65, 122));
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setItemMargin(0.1);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setPreferredSize(new Dimension(600, 400));
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.WHITE);

        this.chartPanel.add(chartPanel, BorderLayout.CENTER);

        btnSearch.addActionListener(e -> updateStatistics());
        btnPrint.addActionListener(e -> printStatistics());
    }

    private JPanel createFieldPanel(String labelText) {
        JPanel panel = new JPanel(new BorderLayout(10, 0));
        panel.setBackground(Color.WHITE);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.PLAIN, 13));
        label.setPreferredSize(new Dimension(100, 25));

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

    private String[] getYearList() {
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        String[] years = new String[11];
        years[0] = "Tất cả";
        for (int i = 0; i < 10; i++) {
            years[i + 1] = String.valueOf(currentYear - i);
        }
        return years;
    }

    private DefaultCategoryDataset createDataset() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        try {
            List<Invoice> invoices = invoiceDAO.getAllInvoices();
            int currentYear = Calendar.getInstance().get(Calendar.YEAR);
            for (int year = currentYear - 10; year <= currentYear; year++) {
                double totalRevenue = 0;
                for (Invoice invoice : invoices) {
                    String invoiceYear = invoice.getInvoiceDate().split("/")[2];
                    if (invoiceYear.equals(String.valueOf(year))) {
                        totalRevenue += invoice.getTotalAmount();
                    }
                }
                dataset.addValue(totalRevenue / 1_000_000, "Doanh thu", String.valueOf(year));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu biểu đồ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
        return dataset;
    }

    private void loadInvoiceData() {
        try {
            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
            model.setRowCount(0);
            for (Invoice invoice : invoiceDAO.getAllInvoices()) {
                model.addRow(new Object[]{
                        invoice.getInvoiceId(),
                        invoice.getInvoiceDate(),
                        currencyFormat.format(invoice.getTotalAmount())
                });
            }
            calculateTotal();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi tải dữ liệu hóa đơn!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void calculateTotal() {
        double total = 0;
        for (int i = 0; i < invoiceTable.getRowCount(); i++) {
            String amountStr = invoiceTable.getValueAt(i, 2).toString().replace(",", "");
            total += Double.parseDouble(amountStr);
        }
        lblTotalAmount.setText(currencyFormat.format(total) + " VNĐ");
    }

    private void updateStatistics() {
        String month = cmbMonth.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();

        try {
            DefaultTableModel model = (DefaultTableModel) invoiceTable.getModel();
            model.setRowCount(0);

            List<Invoice> invoices;
            if (!month.equals("Tất cả") && !year.equals("Tất cả")) {
                invoices = invoiceDAO.getInvoicesByMonthYear(Integer.parseInt(month), Integer.parseInt(year));
            } else if (!month.equals("Tất cả")) {
                invoices = invoiceDAO.getInvoicesByMonth(Integer.parseInt(month));
            } else if (!year.equals("Tất cả")) {
                invoices = invoiceDAO.getInvoicesByYear(Integer.parseInt(year));
            } else {
                invoices = invoiceDAO.getAllInvoices();
            }

            for (Invoice invoice : invoices) {
                model.addRow(new Object[]{
                        invoice.getInvoiceId(),
                        invoice.getInvoiceDate(),
                        currencyFormat.format(invoice.getTotalAmount())
                });
            }

            calculateTotal();
            updateChart();

            if (invoices.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn cho " +
                        (month.equals("Tất cả") ? "tất cả các tháng" : "tháng " + month) + " năm " +
                        (year.equals("Tất cả") ? "tất cả các năm" : year), "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật thống kê!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateChart() {
        chartPanel.removeAll();
        DefaultCategoryDataset dataset = createDataset();
        JFreeChart chart = ChartFactory.createBarChart(
                "Biểu đồ thống kê doanh thu theo năm",
                "Năm",
                "Triệu đồng",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false);

        CategoryPlot plot = (CategoryPlot) chart.getPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);

        BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, new Color(41, 65, 122));
        renderer.setDrawBarOutline(false);
        renderer.setShadowVisible(false);
        renderer.setItemMargin(0.1);

        CategoryAxis domainAxis = plot.getDomainAxis();
        domainAxis.setCategoryMargin(0.2);
        domainAxis.setLowerMargin(0.02);
        domainAxis.setUpperMargin(0.02);

        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        ChartPanel newChartPanel = new ChartPanel(chart);
        newChartPanel.setPreferredSize(new Dimension(600, 400));
        newChartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        newChartPanel.setBackground(Color.WHITE);

        chartPanel.add(newChartPanel, BorderLayout.CENTER);
        chartPanel.revalidate();
        chartPanel.repaint();
    }

    private void printStatistics() {
        String month = cmbMonth.getSelectedItem().toString();
        String year = cmbYear.getSelectedItem().toString();
        JOptionPane.showMessageDialog(this,
                "Đang in báo cáo thống kê cho " +
                        (month.equals("Tất cả") ? "tất cả các tháng" : "tháng " + month) + " năm " +
                        (year.equals("Tất cả") ? "tất cả các năm" : year),
                "Thông báo", JOptionPane.INFORMATION_MESSAGE);
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
        SwingUtilities.invokeLater(() -> new StatisticsManagement());
    }
}