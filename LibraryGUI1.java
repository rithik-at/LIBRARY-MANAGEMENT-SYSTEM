import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class LibraryGUI1 extends JFrame {

    // Database credentials
    private static final String URL = "jdbc:mysql://localhost:3306/library_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root"; // Your MySQL username
    private static final String PASS = "Arul_2007"; // Your MySQL password

    private Connection conn;
    private DefaultTableModel tableModel;
    private JTable table;
    private JTextField titleField, authorField, isbnField, idField;

    public LibraryGUI1() {
        setTitle("Library Management System");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Gradient background
        JPanel mainPanel = new JPanel() {
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                GradientPaint gp = new GradientPaint(0, 0,
                        new Color(0, 102, 204), getWidth(), getHeight(), new Color(0, 204, 153));
                g2.setPaint(gp);
                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setLayout(new BorderLayout());
        add(mainPanel);

        // Header
        JLabel header = new JLabel("Library Management System", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 30));
        header.setForeground(Color.WHITE);
        header.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        mainPanel.add(header, BorderLayout.NORTH);

        // Table section
        String[] cols = {"ID", "Title", "Author", "ISBN", "Status"};
        tableModel = new DefaultTableModel(cols, 0);
        table = new JTable(tableModel);
        table.setBackground(Color.WHITE);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setRowHeight(26);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(0, 51, 102), 2));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Input section
        JPanel inputPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        inputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        inputPanel.setOpaque(false);

        titleField = new JTextField();
        authorField = new JTextField();
        isbnField = new JTextField();
        idField = new JTextField();

        inputPanel.add(labeledField("Title", titleField));
        inputPanel.add(labeledField("Author", authorField));
        inputPanel.add(labeledField("ISBN", isbnField));
        inputPanel.add(labeledField("Book ID (for Issue/Remove)", idField));

        // Buttons panel
        JPanel btnPanel = new JPanel(new GridLayout(1, 5, 10, 10));
        btnPanel.setOpaque(false);

        JButton addBtn = createButton("Add Book", new Color(0, 153, 102));
        JButton viewBtn = createButton("View All", new Color(51, 153, 255));
        JButton issueBtn = createButton("Issue", new Color(255, 153, 0));
        JButton returnBtn = createButton("Return", new Color(46, 204, 113));
        JButton removeBtn = createButton("Remove", new Color(231, 76, 60));

        btnPanel.add(addBtn);
        btnPanel.add(viewBtn);
        btnPanel.add(issueBtn);
        btnPanel.add(returnBtn);
        btnPanel.add(removeBtn);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setOpaque(false);
        bottomPanel.add(inputPanel, BorderLayout.CENTER);
        bottomPanel.add(btnPanel, BorderLayout.SOUTH);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        // Button actions
        addBtn.addActionListener(e -> addBook());
        viewBtn.addActionListener(e -> loadBooks());
        issueBtn.addActionListener(e -> issueBook());
        returnBtn.addActionListener(e -> returnBook());
        removeBtn.addActionListener(e -> removeBook());

        // Connect to database
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); // Load MySQL Driver
            conn = DriverManager.getConnection(URL, USER, PASS);
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Database connection failed:\n" + e.getMessage());
        }
    }

    private JPanel labeledField(String label, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout(5, 5));
        panel.setOpaque(false);
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lbl.setForeground(Color.WHITE);
        panel.add(lbl, BorderLayout.NORTH);
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
        panel.add(field, BorderLayout.CENTER);
        return panel;
    }

    private JButton createButton(String text, Color color) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(color);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                btn.setBackground(color.darker());
            }
            public void mouseExited(MouseEvent e) {
                btn.setBackground(color);
            }
        });
        return btn;
    }

    private void addBook() {
        try {
            String title = titleField.getText().trim();
            String author = authorField.getText().trim();
            String isbn = isbnField.getText().trim();

            if (title.isEmpty() || author.isEmpty() || isbn.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            PreparedStatement ps = conn.prepareStatement(
                    "INSERT INTO books (title, author, isbn, status) VALUES (?, ?, ?, 'Available')");
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, isbn);
            ps.executeUpdate();
            loadBooks();
            clearFields();
            JOptionPane.showMessageDialog(this, "Book added successfully!");
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error adding book:\n" + e.getMessage());
        }
    }

    private void issueBook() {
        try {
            int id = Integer.parseInt(idField.getText());
            PreparedStatement ps1 = conn.prepareStatement("SELECT status FROM books WHERE id=?");
            ps1.setInt(1, id);
            ResultSet rs = ps1.executeQuery();

            if (rs.next()) {
                if (rs.getString("status").equals("Available")) {
                    PreparedStatement ps2 = conn.prepareStatement("UPDATE books SET status='Issued' WHERE id=?");
                    ps2.setInt(1, id);
                    ps2.executeUpdate();
                    JOptionPane.showMessageDialog(this, "Book issued successfully!");
                } else {
                    JOptionPane.showMessageDialog(this, "Book is already issued!");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid Book ID!");
            }
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error issuing book:\n" + e.getMessage());
        }
    }

    private void returnBook() {
        try {
            int id = Integer.parseInt(idField.getText());
            PreparedStatement ps = conn.prepareStatement("UPDATE books SET status='Available' WHERE id=?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Book returned successfully!");
            else
                JOptionPane.showMessageDialog(this, "Invalid Book ID!");
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error returning book:\n" + e.getMessage());
        }
    }

    private void removeBook() {
        try {
            int id = Integer.parseInt(idField.getText());
            PreparedStatement ps = conn.prepareStatement("DELETE FROM books WHERE id=?");
            ps.setInt(1, id);
            int rows = ps.executeUpdate();

            if (rows > 0)
                JOptionPane.showMessageDialog(this, "Book removed successfully!");
            else
                JOptionPane.showMessageDialog(this, "Invalid Book ID!");
            loadBooks();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error removing book:\n" + e.getMessage());
        }
    }

    private void loadBooks() {
        try {
            tableModel.setRowCount(0);
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM books");

            while (rs.next()) {
                Object[] row = {
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("isbn"),
                        rs.getString("status")
                };
                tableModel.addRow(row);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading books:\n" + e.getMessage());
        }
    }

    private void clearFields() {
        titleField.setText("");
        authorField.setText("");
        isbnField.setText("");
        idField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LibraryGUI1().setVisible(true));
    }
}
