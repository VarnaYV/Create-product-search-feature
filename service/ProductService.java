package service;

import model.Product;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 2. Service Class: ProductService
 * Handles business logic, data storage, and filtering using Java Streams API.
 */
public class ProductService {
    private List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
        // Seed some initial dummy data
        products.add(new Product("P001", "Laptop Pro Max", "Electronics", 1200.00));
        products.add(new Product("P002", "Wireless Mouse", "Accessories", 25.50));
        products.add(new Product("P003", "Mechanical Keyboard", "Accessories", 80.00));
        products.add(new Product("P004", "Smartphone X", "Electronics", 999.00));
        products.add(new Product("P005", "Office Chair", "Furniture", 150.00));
        products.add(new Product("P006", "Standing Desk", "Furniture", 300.00));
        products.add(new Product("P007", "Bluetooth Headphones", "Electronics", 199.99));
        products.add(new Product("P008", "USB-C Hub", "Accessories", 45.00));
        products.add(new Product("P009", "Ergonomic Mouse", "Accessories", 65.00));
        products.add(new Product("P010", "4K Monitor", "Electronics", 350.00));
    }

    /**
     * Gets all products for the initial display.
     */
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    /**
     * Completes search requirements by matching name (partial) and category (exact).
     * Filters by min/max price range and incorporates sorting (advanced feature).
     */
    public List<Product> searchProducts(String name, String category, Double minPrice, Double maxPrice) {
        return products.stream()
                // a) Search by name (partial match, case-insensitive)
                .filter(p -> name == null || name.trim().isEmpty() || 
                             p.getName().toLowerCase().contains(name.toLowerCase().trim()))
                
                // b) Search by category (exact match, "All" skips filter)
                .filter(p -> category == null || category.trim().isEmpty() || 
                             category.equalsIgnoreCase("All") || p.getCategory().equalsIgnoreCase(category.trim()))
                
                // c) Search by price range (min to max)
                .filter(p -> minPrice == null || p.getPrice() >= minPrice)
                .filter(p -> maxPrice == null || p.getPrice() <= maxPrice)
                
                // Advanced Feature: Sort results by price ascending
                .sorted((p1, p2) -> Double.compare(p1.getPrice(), p2.getPrice()))
                
                // Collect results to a list
                .collect(Collectors.toList());
    }
}
