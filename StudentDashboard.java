import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class StudentDashboard extends JFrame {

private final String studentName;
private final String username;

private JLabel totalLabel;
private JLabel highestLabel;
private JLabel averageLabel;
private DefaultTableModel tableModel;

private static final String DB_URL =
        "jdbc:sqlite:quiz_results.db";

private final Color BACKGROUND = new Color(18, 24, 45);
private final Color CARD = new Color(30, 39, 68);
private final Color PRIMARY = new Color(124, 92, 255);
private final Color TEXT = new Color(245, 247, 255);
private final Color MUTED = new Color(180, 190, 215);

public StudentDashboard(String studentName, String username) {

    this.studentName = studentName;
    this.username = username;

    setTitle("Student Dashboard - Online Quiz Application");
    setSize(900, 600);
    setMinimumSize(new Dimension(750, 500));
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);

    createDashboard();
    loadStatistics();
    loadQuizHistory();

    setVisible(true);
}

private void createDashboard() {

    JPanel mainPanel = new JPanel(new BorderLayout(15, 20));
    mainPanel.setBackground(BACKGROUND);
    mainPanel.setBorder(
            BorderFactory.createEmptyBorder(20, 25, 20, 25));

    JLabel heading = new JLabel(
            "Welcome, " + studentName + "!",
            SwingConstants.CENTER);

    heading.setFont(new Font("Segoe UI", Font.BOLD, 27));
    heading.setForeground(TEXT);

    JLabel usernameLabel = new JLabel(
            "Username: " + username,
            SwingConstants.CENTER);

    usernameLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
    usernameLabel.setForeground(MUTED);

    JPanel header = new JPanel(new GridLayout(2, 1, 5, 5));
    header.setBackground(BACKGROUND);
    header.add(heading);
    header.add(usernameLabel);

    mainPanel.add(header, BorderLayout.NORTH);

    JPanel statsPanel = new JPanel(new GridLayout(1, 3, 15, 15));
    statsPanel.setBackground(BACKGROUND);

    totalLabel = createStatLabel("Total Quizzes", "0");
    highestLabel = createStatLabel("Highest Score", "0 / 0");
    averageLabel = createStatLabel("Average Percentage", "0%");

    statsPanel.add(createStatCard(totalLabel));
    statsPanel.add(createStatCard(highestLabel));
    statsPanel.add(createStatCard(averageLabel));

    JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
    centerPanel.setBackground(BACKGROUND);

    JLabel historyHeading = new JLabel("My Quiz History");
    historyHeading.setFont(new Font("Segoe UI", Font.BOLD, 20));
    historyHeading.setForeground(TEXT);

    tableModel = new DefaultTableModel(
            new String[]{
                    "Category",
                    "Score",
                    "Total Questions",
                    "Percentage",
                    "Date & Time"
            }, 0) {

        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    JTable historyTable = new JTable(tableModel);
    historyTable.setRowHeight(25);
    historyTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    historyTable.getTableHeader().setFont(
            new Font("Segoe UI", Font.BOLD, 13));

    JScrollPane scrollPane = new JScrollPane(historyTable);

    centerPanel.add(historyHeading, BorderLayout.NORTH);
    centerPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel contentPanel = new JPanel(new BorderLayout(15, 20));
    contentPanel.setBackground(BACKGROUND);
    contentPanel.add(statsPanel, BorderLayout.NORTH);
    contentPanel.add(centerPanel, BorderLayout.CENTER);

    mainPanel.add(contentPanel, BorderLayout.CENTER);

    JButton startButton = createButton("Start New Quiz");
    JButton achievementsButton = createButton("My Achievements");
    JButton refreshButton = createButton("Refresh");
    JButton logoutButton = createButton("Logout");
    JButton analyticsButton = createButton("Performance Analytics");

    JPanel buttonPanel = new JPanel(
            new FlowLayout(FlowLayout.CENTER, 15, 5));

    buttonPanel.setBackground(BACKGROUND);
    buttonPanel.add(startButton);
    buttonPanel.add(refreshButton);
    buttonPanel.add(achievementsButton);
    buttonPanel.add(analyticsButton);
    buttonPanel.add(logoutButton);

    mainPanel.add(buttonPanel, BorderLayout.SOUTH);

    startButton.addActionListener(e -> {
        dispose();
        new OnlineQuizGUI(studentName);
    });

    refreshButton.addActionListener(e -> {
        loadStatistics();
        loadQuizHistory();
    });
    achievementsButton.addActionListener(e -> {
    showAchievements();
    });

    analyticsButton.addActionListener(e -> {
    new StudentAnalytics(studentName);
    });

    logoutButton.addActionListener(e -> {
        dispose();
        new StudentLogin();
    });

    setContentPane(mainPanel);
}

private JLabel createStatLabel(String title, String value) {

    JLabel label = new JLabel(
            "<html><center>" + title
                    + "<br><br><b>" + value
                    + "</b></center></html>",
            SwingConstants.CENTER);

    label.setForeground(TEXT);
    label.setFont(new Font("Segoe UI", Font.PLAIN, 17));

    return label;
}

private JPanel createStatCard(JLabel label) {

    JPanel card = new JPanel(new GridBagLayout());
    card.setBackground(CARD);
    card.setBorder(
            BorderFactory.createEmptyBorder(15, 10, 15, 10));

    card.add(label);

    return card;
}

private JButton createButton(String text) {

    JButton button = new JButton(text);
    button.setFont(new Font("Segoe UI", Font.BOLD, 14));
    button.setBackground(PRIMARY);
    button.setForeground(Color.WHITE);
    button.setFocusPainted(false);
    button.setOpaque(true);
    button.setBorder(
            BorderFactory.createEmptyBorder(10, 18, 10, 18));

    return button;
}

private void loadStatistics() {

    String sql = """
            SELECT COUNT(*) AS total,
                   COALESCE(MAX(score), 0) AS highest_score,
                   COALESCE(MAX(total_questions), 0) AS highest_total,
                   COALESCE(AVG(percentage), 0) AS average_percentage
            FROM quiz_results
            WHERE student_name = ?
            """;

    try (Connection con = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, studentName);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int total = rs.getInt("total");
                int highestScore = rs.getInt("highest_score");
                int highestTotal = rs.getInt("highest_total");
                double average = rs.getDouble("average_percentage");

                totalLabel.setText(
                        "<html><center>Total Quizzes<br><br><b>"
                                + total + "</b></center></html>");

                highestLabel.setText(
                        "<html><center>Highest Score<br><br><b>"
                                + highestScore + " / "
                                + highestTotal
                                + "</b></center></html>");

                averageLabel.setText(
                        String.format(
                                "<html><center>Average Percentage"
                                        + "<br><br><b>%.2f%%"
                                        + "</b></center></html>",
                                average));
            }
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Could not load statistics: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}

private void loadQuizHistory() {

    tableModel.setRowCount(0);

    String sql = """
            SELECT category, score, total_questions,
                   percentage, date_time
            FROM quiz_results
            WHERE student_name = ?
            ORDER BY id DESC
            """;

    try (Connection con = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, studentName);

        try (ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                tableModel.addRow(new Object[]{
                        rs.getString("category"),
                        rs.getInt("score"),
                        rs.getInt("total_questions"),
                        String.format(
                                "%.2f%%",
                                rs.getDouble("percentage")),
                        rs.getString("date_time")
                });
            }
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Could not load quiz history: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
    private void showAchievements() {

    String sql = """
            SELECT COUNT(*) AS total,
                   COALESCE(MAX(percentage), 0) AS highest,
                   COALESCE(AVG(percentage), 0) AS average
            FROM quiz_results
            WHERE student_name = ?
            """;

    try (Connection con = DriverManager.getConnection(DB_URL);
         PreparedStatement ps = con.prepareStatement(sql)) {

        ps.setString(1, studentName);

        try (ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                int total = rs.getInt("total");
                double highest = rs.getDouble("highest");
                double average = rs.getDouble("average");

                StringBuilder badges = new StringBuilder();

                badges.append("🏆 MY ACHIEVEMENTS\n\n");

                if (total >= 1) {
                    badges.append("🥇 First Quiz - UNLOCKED\n");
                } else {
                    badges.append("🔒 First Quiz - Complete your first quiz\n");
                }

                if (total >= 5) {
                    badges.append("🎯 Quiz Explorer - UNLOCKED\n");
                } else {
                    badges.append("🔒 Quiz Explorer - ")
                          .append(total)
                          .append("/5 quizzes completed\n");
                }

                if (highest >= 80) {
                    badges.append("⭐ Quiz Master - UNLOCKED\n");
                } else {
                    badges.append("🔒 Quiz Master - Score 80% in a quiz\n");
                }

                if (highest >= 100) {
                    badges.append("💎 Perfect Score - UNLOCKED\n");
                } else {
                    badges.append("🔒 Perfect Score - Get 100% in a quiz\n");
                }

                if (total >= 5 && average >= 70) {
                    badges.append("🔥 Consistent Learner - UNLOCKED\n");
                } else {
                    badges.append("🔒 Consistent Learner - Complete 5 quizzes ")
                          .append("with a 70% average\n");
                }

                badges.append("\nTotal Quizzes: ").append(total);
                badges.append(String.format(
                        "\nHighest Percentage: %.2f%%", highest));
                badges.append(String.format(
                        "\nAverage Percentage: %.2f%%", average));

                JOptionPane.showMessageDialog(
                        this,
                        badges.toString(),
                        "My Achievements",
                        JOptionPane.INFORMATION_MESSAGE
                );
            }
        }

    } catch (SQLException e) {

        JOptionPane.showMessageDialog(
                this,
                "Could not load achievements: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

}


public static void main(String[] args) {
SwingUtilities.invokeLater(() -> {
new StudentDashboard("Test Student", "testuser");
});
}
}
