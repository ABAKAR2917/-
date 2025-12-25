import javax.swing.SwingUtilities;

// Точка входа в программу
public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            HRMainFrame frame = new HRMainFrame();
            frame.setVisible(true);
        });
    }
}