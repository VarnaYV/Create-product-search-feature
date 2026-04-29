package ui;

import model.Product;
import service.ProductService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

/**
 * 3. UI Class: ProductUI
 * Creates the Swing window, components, layouts, and wires up event handlers.
 */
public class ProductUI extends JFrame {
    private ProductService service;
    
    // UI Components
    private DefaultTableModel tableModel;
    private JTable resultTable;
    private JTextField nameField;
    private JComboBox<String> categoryCombo;
    private JTextField minPriceField;
    private JTextField maxPriceField;
    private JLabel statusLabel;

    public ProductUI() {
        // Initialize service internally keeping concerns separated
        service = new ProductService();
        
        // Window Setup
        setTitle("Product Search System");
        setSize(800, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center on screen
        
        initUI();
        
        // Load initial data
        loadResults(service.getAllProducts());
    }
    
    /**
     * Initializes all panels and layouts.
     */
    private void initUI() {
        // Main standard BorderLayout
        setLayout(new BorderLayout(10, 10));
        
        // --- Top Panel: Input Form (North) ---
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BorderLayout(5, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));

        // Use GridLayout for the form fields
        JPanel formPanel = new JPanel(new GridLayout(2, 4, 10, 10));
        
        formPanel.add(new JLabel("Product Name:"));
        nameField = new JTextField();
        formPanel.add(nameField);
        
        formPanel.add(new JLabel("Category:"));
        categoryCombo = new JComboBox<>(new String[]{"All", "Electronics", "Accessories", "Furniture"});
        formPanel.add(categoryCombo);
        
        formPanel.add(new JLabel("Min Price:"));
        minPriceField = new JTextField();
        formPanel.add(minPriceField);
        
        formPanel.add(new JLabel("Max Price:"));
        maxPriceField = new JTextField();
        formPanel.add(maxPriceField);
        
        topPanel.add(formPanel, BorderLayout.CENTER);
        
        // --- Buttons Panel ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton searchBtn = new JButton("Search");
        JButton resetBtn = new JButton("Reset");
        
        // Lambda expressions for event handling
        searchBtn.addActionListener(e -> handleSearch());
        resetBtn.addActionListener(e -> resetForm());
        
        buttonPanel.add(searchBtn);
        buttonPanel.add(resetBtn);
        
        topPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(topPanel, BorderLayout.NORTH);
        
        // --- Center Panel: Results Table (Center) ---
        String[] columns = {"ID", "Name", "Category", "Price ($)"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Keep table read-only
                return false;
            }
        };
        resultTable = new JTable(tableModel);
        resultTable.setRowHeight(25);
        resultTable.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 12));
        
        JScrollPane scrollPane = new JScrollPane(resultTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Search Results"));
        
        // Add some margin to the scroll pane
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 15, 0, 15));
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);
        
        // --- Bottom Panel: Status (South) ---
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));
        statusLabel = new JLabel("Ready");
        statusLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        bottomPanel.add(statusLabel);
        add(bottomPanel, BorderLayout.SOUTH);
    }
    
    /**
     * Input validation and exception handling before searching.
     */
    private void handleSearch() {
        String name = nameField.getText();
        String category = (String) categoryCombo.getSelectedItem();
        
        Double minPrice = null;
        Double maxPrice = null;
        
        try {
            // Validate Min Price
            if (!minPriceField.getText().trim().isEmpty()) {
                minPrice = Double.parseDouble(minPriceField.getText().trim());
                if (minPrice < 0) throw new NumberFormatException("Negative min price");
            }
            
            // Validate Max Price
            if (!maxPriceField.getText().trim().isEmpty()) {
                maxPrice = Double.parseDouble(maxPriceField.getText().trim());
                if (maxPrice < 0) throw new NumberFormatException("Negative max price");
            }
            
            // Validate Range Logic
            if (minPrice != null && maxPrice != null && minPrice > maxPrice) {
                JOptionPane.showMessageDialog(this, 
                        "Min Price cannot be greater than Max Price.", 
                        "Validation Error", 
                        JOptionPane.WARNING_MESSAGE);
                return;
            }
            
        } catch (NumberFormatException ex) {
            // Exception handling for non-numeric or negative input mapping to user warning
            JOptionPane.showMessageDialog(this, 
                    "Please enter valid positive numbers for price fields.", 
                    "Input Error", 
                    JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        // Combined search execution after validation
        List<Product> results = service.searchProducts(name, category, minPrice, maxPrice);
        loadResults(results);
        
        // Advanced Feature: Highlight empty results and summarize finding counts
        if (results.isEmpty()) {
            statusLabel.setText("Found 0 products matching criteria.");
            statusLabel.setForeground(Color.RED); 
        } else {
            statusLabel.setText("Found " + results.size() + " product(s). Results sorted by price.");
            statusLabel.setForeground(new Color(0, 128, 0)); // Dark green text
        }
    }
    
    /**
     * Resets the application to default state.
     */
    private void resetForm() {
        nameField.setText("");
        categoryCombo.setSelectedIndex(0);
        minPriceField.setText("");
        maxPriceField.setText("");
        
        statusLabel.setText("Ready");
        statusLabel.setForeground(Color.BLACK);
        
        loadResults(service.getAllProducts());
    }
    
    /**
     * Re-renders the JTable utilizing the underlying TableModel.
     */
    private void loadResults(List<Product> products) {
        // Clear old rows
        tableModel.setRowCount(0);
        
        // Populate new rows
        for (Product p : products) {
            tableModel.addRow(new Object[]{
                p.getId(), 
                p.getName(), 
                p.getCategory(), 
                String.format("%.2f", p.getPrice()) // Format price nicely
            });
        }
    }
}
