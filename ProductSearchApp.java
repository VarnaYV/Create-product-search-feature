import ui.ProductUI;

import javax.swing.*;

/**
 * Main application class that launches the Java Swing application.
 */
public class ProductSearchApp {
    public static void main(String[] args) {
        // Run UI creation on Event Dispatch Thread for thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set system look and feel for native OS appearance
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            ProductUI ui = new ProductUI();
            ui.setVisible(true);
        });
    }
}
