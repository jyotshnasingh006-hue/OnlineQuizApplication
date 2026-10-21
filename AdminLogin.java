import javax.swing.*;
import java.awt.*;

public class AdminLogin extends JFrame {


private JTextField usernameField;
private JPasswordField passwordField;

public AdminLogin() {
    setTitle("Admin Login");
    setSize(400, 300);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panel = new JPanel(new GridLayout(4, 2, 10, 10));
    panel.setBorder(BorderFactory.createEmptyBorder(25, 25, 25, 25));
    panel.setBackground(new Color(30, 30, 45));

    JLabel heading = new JLabel("ADMIN LOGIN");
    heading.setForeground(Color.WHITE);
    heading.setFont(new Font("Arial", Font.BOLD, 20));

    JLabel usernameLabel = new JLabel("Username:");
    usernameLabel.setForeground(Color.WHITE);

    usernameField = new JTextField();

    JLabel passwordLabel = new JLabel("Password:");
    passwordLabel.setForeground(Color.WHITE);

    passwordField = new JPasswordField();

    JButton loginButton = new JButton("Login");

    panel.add(heading);
    panel.add(new JLabel(""));
    panel.add(usernameLabel);
    panel.add(usernameField);
    panel.add(passwordLabel);
    panel.add(passwordField);
    panel.add(new JLabel(""));
    panel.add(loginButton);

    add(panel);

    loginButton.addActionListener(e -> checkLogin());

    getRootPane().setDefaultButton(loginButton);
}

private void checkLogin() {
    String username = usernameField.getText().trim();
    String password = new String(passwordField.getPassword());

    if (username.equals("admin") && password.equals("Admin@123")) {

        JOptionPane.showMessageDialog(
                this, "Login Successful!");

        new AdminDashboard().setVisible(true);
        dispose();

    } else {
        JOptionPane.showMessageDialog(
                this,
                "Incorrect Username or Password!",
                "Login Failed",
                JOptionPane.ERROR_MESSAGE);
    }
}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new AdminLogin().setVisible(true);
    });
}


}
