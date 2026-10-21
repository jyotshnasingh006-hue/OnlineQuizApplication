
import javax.swing.*;
import java.awt.*;

public class StudentLogin extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public StudentLogin() {

        setTitle("Student Login");
        setSize(450, 350);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(5, 2, 10, 10));
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 25, 25, 25));
        panel.setBackground(new Color(30, 30, 45));

        JLabel heading = new JLabel("STUDENT LOGIN");
        heading.setForeground(Color.WHITE);
        heading.setFont(new Font("Arial", Font.BOLD, 20));

        JLabel usernameLabel = new JLabel("Username:");
        usernameLabel.setForeground(Color.WHITE);

        usernameField = new JTextField();

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setForeground(Color.WHITE);

        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        JLabel emptyLabel = new JLabel("");

        panel.add(heading);
        panel.add(emptyLabel);
        panel.add(usernameLabel);
        panel.add(usernameField);
        panel.add(passwordLabel);
        panel.add(passwordField);
        panel.add(new JLabel(""));
        panel.add(loginButton);
        panel.add(new JLabel(""));
        panel.add(registerButton);

        add(panel);

        loginButton.addActionListener(e -> loginStudent());

        registerButton.addActionListener(e -> {
    new StudentRegistration().setVisible(true);
});
        getRootPane().setDefaultButton(loginButton);
    }

   private void loginStudent() {

String username = usernameField.getText().trim();
String password = new String(passwordField.getPassword());

if (username.isEmpty() || password.isEmpty()) {
    JOptionPane.showMessageDialog(
            this, "Please enter username and password!");
    return;
}

String studentName =
        StudentDatabase.loginStudent(username, password);

if (studentName != null) {

    JOptionPane.showMessageDialog(
            this, "Welcome, " + studentName + "!");

    dispose();

SwingUtilities.invokeLater(() -> {
new StudentDashboard(studentName, username);
});


} else {

    JOptionPane.showMessageDialog(
            this,
            "Invalid username or password!",
            "Login Failed",
            JOptionPane.ERROR_MESSAGE
    );
}


}


    public static void main(String[] args) {

        StudentDatabase.initializeDatabase();

        SwingUtilities.invokeLater(() -> {
            new StudentLogin().setVisible(true);
        });
    }
}

