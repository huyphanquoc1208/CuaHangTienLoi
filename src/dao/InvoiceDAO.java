package dao;

import entity.Invoice;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class InvoiceDAO {
    private Connection connection;

    public InvoiceDAO(Connection connection) {
        this.connection = connection;
    }

    public void addInvoice(Invoice invoice) throws SQLException {
        String sql = "INSERT INTO invoices (invoice_id, invoice_date, total_amount) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoice.getInvoiceId());
            stmt.setString(2, invoice.getInvoiceDate());
            stmt.setDouble(3, invoice.getTotalAmount());
            stmt.executeUpdate();
        }
    }

    public List<Invoice> getAllInvoices() throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Invoice invoice = new Invoice();
                invoice.setInvoiceId(rs.getString("invoice_id"));
                invoice.setInvoiceDate(rs.getString("invoice_date"));
                invoice.setTotalAmount(rs.getDouble("total_amount"));
                invoices.add(invoice);
            }
        }
        return invoices;
    }

    public Invoice getInvoiceById(String invoiceId) throws SQLException {
        String sql = "SELECT * FROM invoices WHERE invoice_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, invoiceId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    return invoice;
                }
            }
        }
        return null;
    }

    public List<Invoice> getInvoicesByDate(String date) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE invoice_date = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, date);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByMonth(int month) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 4, 2) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", month));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByYear(int year) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 7, 4) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }

    public List<Invoice> getInvoicesByMonthYear(int month, int year) throws SQLException {
        List<Invoice> invoices = new ArrayList<>();
        String sql = "SELECT * FROM invoices WHERE SUBSTRING(invoice_date, 4, 2) = ? AND SUBSTRING(invoice_date, 7, 4) = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, String.format("%02d", month));
            stmt.setString(2, String.valueOf(year));
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Invoice invoice = new Invoice();
                    invoice.setInvoiceId(rs.getString("invoice_id"));
                    invoice.setInvoiceDate(rs.getString("invoice_date"));
                    invoice.setTotalAmount(rs.getDouble("total_amount"));
                    invoices.add(invoice);
                }
            }
        }
        return invoices;
    }
}