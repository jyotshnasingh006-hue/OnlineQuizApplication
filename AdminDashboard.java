import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class AdminDashboard extends JFrame {


private static final String URL = "jdbc:sqlite:quiz_results.db";

public AdminDashboard() {
    setTitle("Quiz Application - Admin Dashboard");
    setSize(600, 450);
    setLocationRelativeTo(null);
    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    JPanel panel = new JPanel();
    panel.setLayout(new GridLayout(8, 1, 10, 10));
    panel.setBackground(new Color(30, 30, 45));
    panel.setBorder(BorderFactory.createEmptyBorder(25, 30, 25, 30));

    JLabel heading = new JLabel("ADMIN DASHBOARD", SwingConstants.CENTER);
    heading.setFont(new Font("Arial", Font.BOLD, 26));
    heading.setForeground(Color.WHITE);

    JLabel attemptsLabel = createLabel("Total Quiz Attempts: Loading...");
    JLabel averageLabel = createLabel("Average Score: Loading...");
    JLabel studentsLabel = createLabel("Unique Students: Loading...");

    JButton refreshButton = new JButton("Refresh Dashboard");
    refreshButton.setFont(new Font("Arial", Font.BOLD, 16));
    JButton categoryButton = new JButton("Category Statistics");
    categoryButton.setFont(new Font("Arial", Font.BOLD, 16));
    JButton exportButton = new JButton("Export Results to CSV");
    exportButton.setFont(new Font("Arial", Font.BOLD, 16));
    JButton historyButton = new JButton("View Quiz History");
    historyButton.setFont(new Font("Arial", Font.BOLD, 16));


    panel.add(heading);
    panel.add(attemptsLabel);
    panel.add(averageLabel);
    panel.add(studentsLabel);
    panel.add(refreshButton);
    panel.add(categoryButton);
    panel.add(exportButton);
    panel.add(historyButton);
    add(panel);

    refreshButton.addActionListener(e -> {
        loadStatistics(attemptsLabel, averageLabel, studentsLabel);
    });

    categoryButton.addActionListener(e -> {
        showCategoryStatistics();
    });

    exportButton.addActionListener(e -> {
        exportResultsToCSV();
    });

    
    historyButton.addActionListener(e -> {
       showQuizHistory();
    });

    loadStatistics(attemptsLabel, averageLabel, studentsLabel);
}

private JLabel createLabel(String text) {
    JLabel label = new JLabel(text, SwingConstants.CENTER);
    label.setFont(new Font("Arial", Font.BOLD, 17));
    label.setForeground(Color.WHITE);
    return label;
}

private void loadStatistics(
        JLabel attemptsLabel,
        JLabel averageLabel,
        JLabel studentsLabel) {

    String sql = "SELECT COUNT(*) AS attempts, "
            + "AVG(percentage) AS average_score, "
            + "COUNT(DISTINCT student_name) AS students "
            + "FROM quiz_results";

    try (Connection conn = DriverManager.getConnection(URL);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(sql)) {

        if (rs.next()) {
            attemptsLabel.setText(
                    "Total Quiz Attempts: " + rs.getInt("attempts"));

            averageLabel.setText(String.format(
                    "Average Score: %.2f%%",
                    rs.getDouble("average_score")));

            studentsLabel.setText(
                    "Unique Students: " + rs.getInt("students"));
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Database Error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

private void showCategoryStatistics() {
String sql = "SELECT category, COUNT(*) AS attempts, "
+ "AVG(percentage) AS average_score "
+ "FROM quiz_results GROUP BY category";


StringBuilder report = new StringBuilder();
report.append("CATEGORY-WISE STATISTICS\n\n");

try (Connection conn = DriverManager.getConnection(URL);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {

    boolean found = false;

    while (rs.next()) {
        found = true;
        report.append("Category: ")
                .append(rs.getString("category"))
                .append("\nQuiz Attempts: ")
                .append(rs.getInt("attempts"))
                .append("\nAverage Score: ")
                .append(String.format("%.2f%%",
                        rs.getDouble("average_score")))
                .append("\n\n");
    }

    if (!found) {
        report.append("No quiz results available.");
    }

    JOptionPane.showMessageDialog(
            this,
            report.toString(),
            "Category Statistics",
            JOptionPane.INFORMATION_MESSAGE);

} catch (SQLException e) {
    JOptionPane.showMessageDialog(
            this,
            "Database Error: " + e.getMessage());
}


}
private void exportResultsToCSV() {
String sql = "SELECT student_name, category, score, "
+ "total_questions, percentage, date_time "
+ "FROM quiz_results ORDER BY id DESC";


JFileChooser fileChooser = new JFileChooser();
fileChooser.setSelectedFile(new java.io.File("quiz_results.csv"));

int choice = fileChooser.showSaveDialog(this);

if (choice != JFileChooser.APPROVE_OPTION) {
    return;
}

java.io.File file = fileChooser.getSelectedFile();

if (!file.getName().toLowerCase().endsWith(".csv")) {
    file = new java.io.File(file.getAbsolutePath() + ".csv");
}

try (Connection conn = DriverManager.getConnection(URL);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql);
     java.io.PrintWriter writer = new java.io.PrintWriter(file)) {

    writer.println("Student Name,Category,Score,Total Questions,Percentage,Date Time");

    while (rs.next()) {
        writer.printf("\"%s\",\"%s\",%d,%d,%.2f,\"%s\"%n",
                rs.getString("student_name").replace("\"", "\"\""),
                rs.getString("category").replace("\"", "\"\""),
                rs.getInt("score"),
                rs.getInt("total_questions"),
                rs.getDouble("percentage"),
                rs.getString("date_time"));
    }

    JOptionPane.showMessageDialog(
            this,
            "Results exported successfully!\n" + file.getAbsolutePath());

} catch (Exception e) {
    JOptionPane.showMessageDialog(
            this,
            "Export Error: " + e.getMessage());
}


}

private void showQuizHistory() {
String sql = "SELECT student_name, category, score, "
+ "total_questions, percentage, date_time "
+ "FROM quiz_results ORDER BY id DESC";


String[] columns = {
        "Student Name", "Category", "Score",
        "Total Questions", "Percentage", "Date & Time"
};

javax.swing.table.DefaultTableModel model =
        new javax.swing.table.DefaultTableModel(columns, 0);

try (Connection conn = DriverManager.getConnection(URL);
     Statement stmt = conn.createStatement();
     ResultSet rs = stmt.executeQuery(sql)) {

    while (rs.next()) {
        Object[] row = {
                rs.getString("student_name"),
                rs.getString("category"),
                rs.getInt("score"),
                rs.getInt("total_questions"),
                String.format("%.2f%%", rs.getDouble("percentage")),
                rs.getString("date_time")
        };

        model.addRow(row);
    }

    JTable table = new JTable(model);
    table.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    JScrollPane scrollPane = new JScrollPane(table);
    scrollPane.setPreferredSize(new Dimension(850, 300));

    JOptionPane.showMessageDialog(
            this,
            scrollPane,
            "Quiz History",
            JOptionPane.INFORMATION_MESSAGE
    );

} catch (SQLException e) {
    JOptionPane.showMessageDialog(
            this,
            "Database Error: " + e.getMessage()
    );
}


}

public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
        new AdminDashboard().setVisible(true);
    });
}


}
