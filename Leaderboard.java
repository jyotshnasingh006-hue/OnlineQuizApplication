import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class Leaderboard extends JFrame {

    private JTable leaderboardTable;
    private DefaultTableModel tableModel;

    public Leaderboard() {

        setTitle("Student Leaderboard");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(30, 30, 45));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel heading = new JLabel(
                "STUDENT LEADERBOARD", SwingConstants.CENTER);

        heading.setFont(new Font("Arial", Font.BOLD, 26));
        heading.setForeground(Color.WHITE);
        heading.setBorder(
                BorderFactory.createEmptyBorder(10, 10, 20, 10));

        String[] columns = {
                "Rank",
                "Student Name",
                "Category",
                "Score",
                "Total Questions",
                "Percentage"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaderboardTable = new JTable(tableModel);
        leaderboardTable.setRowHeight(30);
        leaderboardTable.setFont(new Font("Arial", Font.PLAIN, 14));

        leaderboardTable.getTableHeader()
                .setFont(new Font("Arial", Font.BOLD, 14));

        JScrollPane scrollPane = new JScrollPane(leaderboardTable);

        JButton refreshButton = new JButton("Refresh Leaderboard");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 14));

        refreshButton.addActionListener(e -> loadLeaderboard());

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(refreshButton, BorderLayout.SOUTH);

        add(mainPanel);

        loadLeaderboard();
    }

    private void loadLeaderboard() {

        tableModel.setRowCount(0);

        String query =
                "SELECT student_name, category, score, " +
                "total_questions, percentage " +
                "FROM quiz_results " +
                "ORDER BY percentage DESC, score DESC";

        try (
                Connection connection =
                        DriverManager.getConnection(
                                "jdbc:sqlite:quiz_results.db");

                Statement statement = connection.createStatement();

                ResultSet resultSet = statement.executeQuery(query)
        ) {

            int rank = 1;

            while (resultSet.next()) {

                String studentName =
                        resultSet.getString("student_name");

                String category =
                        resultSet.getString("category");

                int score =
                        resultSet.getInt("score");

                int totalQuestions =
                        resultSet.getInt("total_questions");

                double percentage =
                        resultSet.getDouble("percentage");

                Object[] row = {
                        rank++,
                        studentName,
                        category,
                        score,
                        totalQuestions,
                        String.format("%.2f%%", percentage)
                };

                tableModel.addRow(row);
            }

            if (tableModel.getRowCount() == 0) {
                JOptionPane.showMessageDialog(
                        this,
                        "Abhi koi quiz result available nahi hai."
                );
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new Leaderboard().setVisible(true);
        });
    }
}
