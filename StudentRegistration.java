
import javax.swing.*;
import java.awt.*;

public class StudentRegistration extends JFrame {

    private JTextField nameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public StudentRegistration() {

        setTitle("Student Registration");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(6, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(30, 30, 45));

        JLabel heading = new JLabel("CREATE ACCOUNT");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel nameLabel = new JLabel("Full Name:");
        JLabel usernameLabel = new JLabel("Username:");
        JLabel passwordLabel = new JLabel("Password:");
        JLabel confirmLabel = new JLabel("Confirm Password:");

        nameLabel.setForeground(Color.WHITE);
        usernameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);
        confirmLabel.setForeground(Color.WHITE);

        nameField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        JButton registerButton = new JButton("Create Account");

        panel.add(heading);
        panel.add(new JLabel(""));
        panel.add(nameLabel);
        panel.add(nameField);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(confirmLabel);
        panel.add(confirmPasswordField);
        panel.add(new JLabel(""));
        panel.add(registerButton);

        add(panel);

        registerButton.addActionListener(e -> registerStudent());
    }

    private void registerStudent() {

        String name = nameField.getText().trim();
        String username = usernameField.getText().trim();

        String password =
                new String(passwordField.getPassword());

        String confirmPassword =
                new String(confirmPasswordField.getPassword());

        if (name.isEmpty() || username.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill all fields!");
            return;
        }

        if (password.length() < 8) {
            JOptionPane.showMessageDialog(
                    this, "Password must be at least 8 characters!");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    this, "Passwords do not match!");
            return;
        }

        boolean success = StudentDatabase.registerStudent(
                name, username, password);

        if (success) {

            JOptionPane.showMessageDialog(
                    this, "Registration successful! Please login.");

            dispose();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed. Username may already exist.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}

