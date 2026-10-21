
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class StudentAnalytics extends JFrame {

    private final String studentName;
    private static final String DB_URL = "jdbc:sqlite:quiz_results.db";

    private int totalQuizzes;
    private double averagePercentage;
    private double highestPercentage;

    private final Color BACKGROUND = new Color(18, 24, 45);
    private final Color CARD = new Color(30, 39, 68);
    private final Color TEXT = new Color(245, 247, 255);
    private final Color PRIMARY = new Color(124, 92, 255);

    public StudentAnalytics(String studentName) {
        this.studentName = studentName;

        setTitle("Student Performance Analytics");
        setSize(900, 600);
        setMinimumSize(new Dimension(750, 500));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        loadStatistics();
        createUI();

        setVisible(true);
    }

    private void loadStatistics() {
        String sql = """
                SELECT COUNT(*) AS total,
                       COALESCE(AVG(percentage), 0) AS average,
                       COALESCE(MAX(percentage), 0) AS highest
                FROM quiz_results
                WHERE student_name = ?
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentName);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalQuizzes = rs.getInt("total");
                    averagePercentage = rs.getDouble("average");
                    highestPercentage = rs.getDouble("highest");
                }
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(
                    this, "Database error: " + e.getMessage());
        }
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(15, 20));
        main.setBackground(BACKGROUND);
        main.setBorder(BorderFactory.createEmptyBorder(20, 25, 20, 25));

        JLabel heading = new JLabel(
                "Performance Analytics - " + studentName,
                SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 23));
        heading.setForeground(TEXT);

        main.add(heading, BorderLayout.NORTH);

        JPanel stats = new JPanel(new GridLayout(1, 3, 15, 15));
        stats.setBackground(BACKGROUND);

        stats.add(createCard("Total Quizzes", String.valueOf(totalQuizzes)));
        stats.add(createCard(
                "Average Score", String.format("%.2f%%", averagePercentage)));
        stats.add(createCard(
                "Highest Score", String.format("%.2f%%", highestPercentage)));

        JPanel center = new JPanel(new GridLayout(1, 2, 15, 15));
        center.setBackground(BACKGROUND);

        center.add(new CategoryChart());
        center.add(new ProgressChart());

        JPanel content = new JPanel(new BorderLayout(15, 20));
        content.setBackground(BACKGROUND);
        content.add(stats, BorderLayout.NORTH);
        content.add(center, BorderLayout.CENTER);

        main.add(content, BorderLayout.CENTER);

        JButton refresh = new JButton("Refresh Analytics");
        refresh.addActionListener(e -> {
            dispose();
            new StudentAnalytics(studentName);
        });

        JButton close = new JButton("Close");
        close.addActionListener(e -> dispose());

        JPanel buttons = new JPanel();
        buttons.setBackground(BACKGROUND);
        buttons.add(refresh);
        buttons.add(close);

        main.add(buttons, BorderLayout.SOUTH);
        setContentPane(main);
    }

    private JPanel createCard(String title, String value) {
        JPanel panel = new JPanel(new GridLayout(2, 1, 5, 5));
        panel.setBackground(CARD);
        panel.setBorder(BorderFactory.createEmptyBorder(15, 10, 15, 10));

        JLabel titleLabel = new JLabel(title, SwingConstants.CENTER);
        titleLabel.setForeground(TEXT);
        titleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 15));

        JLabel valueLabel = new JLabel(value, SwingConstants.CENTER);
        valueLabel.setForeground(TEXT);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 25));

        panel.add(titleLabel);
        panel.add(valueLabel);

        return panel;
    }

    private class CategoryChart extends JPanel {
        CategoryChart() {
            setBackground(CARD);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(TEXT);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
            g2.drawString("Category-wise Average", 18, 28);

            String sql = """
                    SELECT category, AVG(percentage) AS avg_score
                    FROM quiz_results
                    WHERE student_name = ?
                    GROUP BY category
                    ORDER BY category
                    """;

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Double> values = new ArrayList<>();

            try (Connection con = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, studentName);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        names.add(rs.getString("category"));
                        values.add(rs.getDouble("avg_score"));
                    }
                }

            } catch (SQLException e) {
                g2.drawString("Unable to load category data", 15, 65);
            }

            if (names.isEmpty()) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString("Complete a quiz to see your chart.", 15, 65);
            }

            int chartHeight = Math.max(100, getHeight() - 100);
            int slot = Math.max(1, chartHeight / Math.max(1, names.size()));
            int y = 55;

            for (int i = 0; i < names.size(); i++) {
                int barWidth = (int) (
                        Math.max(0, Math.min(100, values.get(i)))
                                / 100.0 * Math.max(10, getWidth() - 135));

                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 11));
                g2.drawString(names.get(i), 12, y + 14);

                int barX = Math.min(130, getWidth() / 2);
                int availableWidth = Math.max(10, getWidth() - barX - 45);
                barWidth = (int) (
                        Math.max(0, Math.min(100, values.get(i)))
                                / 100.0 * availableWidth);

                g2.setColor(new Color(65, 75, 110));
                g2.fillRoundRect(
                        barX, y, availableWidth, 18, 8, 8);

                g2.setColor(PRIMARY);
                g2.fillRoundRect(
                        barX, y, barWidth, 18, 8, 8);

                g2.setColor(TEXT);
                g2.drawString(
                        String.format("%.0f%%", values.get(i)),
                        Math.min(getWidth() - 35, barX + availableWidth + 5),
                        y + 14);

                y += slot;
            }

            g2.dispose();
        }
    }

    private class ProgressChart extends JPanel {
        ProgressChart() {
            setBackground(CARD);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(TEXT);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 17));
            g2.drawString("Quiz Score Progress", 18, 28);

            ArrayList<Double> scores = new ArrayList<>();

            String sql = """
                    SELECT percentage
                    FROM quiz_results
                    WHERE student_name = ?
                    ORDER BY id ASC
                    """;

            try (Connection con = DriverManager.getConnection(DB_URL);
                 PreparedStatement ps = con.prepareStatement(sql)) {

                ps.setString(1, studentName);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        scores.add(rs.getDouble("percentage"));
                    }
                }

            } catch (SQLException e) {
                g2.drawString("Unable to load progress data", 15, 65);
            }

            if (scores.isEmpty()) {
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 13));
                g2.drawString("Complete a quiz to see your progress.", 15, 65);
                g2.dispose();
                return;
            }

            int left = 40;
            int right = Math.max(left + 1, getWidth() - 20);
            int top = 55;
            int bottom = Math.max(top + 1, getHeight() - 40);

            g2.setColor(new Color(90, 100, 130));
            g2.drawLine(left, top, left, bottom);
            g2.drawLine(left, bottom, right, bottom);

            for (int p = 0; p <= 100; p += 25) {
                int y = bottom - (bottom - top) * p / 100;
                g2.setColor(new Color(65, 75, 100));
                g2.drawLine(left, y, right, y);

                g2.setColor(TEXT);
                g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
                g2.drawString(p + "%", 5, y + 4);
            }

            int previousX = -1;
            int previousY = -1;
            int count = scores.size();

            for (int i = 0; i < count; i++) {
                double score = Math.max(0, Math.min(100, scores.get(i)));

                int x = count == 1
                        ? (left + right) / 2
                        : left + (right - left) * i / (count - 1);

                int y = bottom - (int) (
                        (bottom - top) * score / 100.0);

                if (previousX >= 0) {
                    g2.setColor(PRIMARY);
                    g2.setStroke(new BasicStroke(3));
                    g2.drawLine(previousX, previousY, x, y);
                }

                g2.setColor(new Color(100, 220, 180));
                g2.fillOval(x - 4, y - 4, 8, 8);

                previousX = x;
                previousY = y;
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {
        String name = JOptionPane.showInputDialog(
                null, "Enter your registered student name:");

        if (name != null && !name.trim().isEmpty()) {
            SwingUtilities.invokeLater(
                    () -> new StudentAnalytics(name.trim()));
        }
    }
}

