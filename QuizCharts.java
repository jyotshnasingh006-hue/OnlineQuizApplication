
import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

public class QuizCharts extends JFrame {

    private final Color backgroundColor = new Color(30, 30, 45);
    private final Color textColor = Color.WHITE;

    private final ArrayList<String> categories = new ArrayList<>();
    private final ArrayList<Integer> attempts = new ArrayList<>();
    private final ArrayList<Double> averages = new ArrayList<>();

    public QuizCharts() {

        setTitle("Quiz Graphical Dashboard");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        loadData();

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(backgroundColor);
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
                "QUIZ GRAPHICAL DASHBOARD",
                SwingConstants.CENTER
        );

        heading.setForeground(textColor);
        heading.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel chartsPanel = new JPanel(new GridLayout(1, 2, 15, 15));
        chartsPanel.setBackground(backgroundColor);

        chartsPanel.add(new BarChartPanel());
        chartsPanel.add(new PieChartPanel());

        JButton refreshButton = new JButton("Refresh Charts");

        refreshButton.addActionListener(e -> {
            categories.clear();
            attempts.clear();
            averages.clear();

            loadData();

            chartsPanel.removeAll();
            chartsPanel.add(new BarChartPanel());
            chartsPanel.add(new PieChartPanel());

            chartsPanel.revalidate();
            chartsPanel.repaint();
        });

        JPanel bottomPanel = new JPanel();
        bottomPanel.setBackground(backgroundColor);
        bottomPanel.add(refreshButton);

        mainPanel.add(heading, BorderLayout.NORTH);
        mainPanel.add(chartsPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void loadData() {

        String query =
                "SELECT category, COUNT(*) AS total, " +
                "AVG(percentage) AS average " +
                "FROM quiz_results " +
                "GROUP BY category";

        try (
                Connection con = DriverManager.getConnection(
                        "jdbc:sqlite:quiz_results.db");

                Statement stmt = con.createStatement();

                ResultSet rs = stmt.executeQuery(query)
        ) {

            while (rs.next()) {

                categories.add(rs.getString("category"));
                attempts.add(rs.getInt("total"));
                averages.add(rs.getDouble("average"));
            }

        } catch (SQLException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Database Error: " + e.getMessage()
            );
        }
    }

    class BarChartPanel extends JPanel {

        BarChartPanel() {
            setBackground(new Color(42, 42, 60));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            g2.drawString("Quiz Attempts by Category", 20, 35);

            if (categories.isEmpty()) {
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.drawString("No quiz data available", 30, 80);
                g2.dispose();
                return;
            }

            int left = 45;
            int top = 65;
            int bottom = getHeight() - 90;
            int chartHeight = bottom - top;

            int max = 1;

            for (int value : attempts) {
                max = Math.max(max, value);
            }

            g2.setColor(Color.LIGHT_GRAY);
            g2.drawLine(left, top, left, bottom);
            g2.drawLine(left, bottom, getWidth() - 15, bottom);

            int availableWidth = getWidth() - left - 25;
            int slotWidth = availableWidth / categories.size();
            int barWidth = Math.max(10, slotWidth / 2);

            for (int i = 0; i < categories.size(); i++) {

                int barHeight =
                        (int) ((attempts.get(i) / (double) max)
                                * (chartHeight - 15));

                int x = left + i * slotWidth
                        + (slotWidth - barWidth) / 2;

                int y = bottom - barHeight;

                g2.setColor(new Color(70, 130, 220));
                g2.fillRoundRect(
                        x, y, barWidth, barHeight, 8, 8);

                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Arial", Font.BOLD, 12));

                g2.drawString(
                        String.valueOf(attempts.get(i)),
                        x, Math.max(top + 12, y - 5));

                g2.setFont(new Font("Arial", Font.PLAIN, 11));

                String category = categories.get(i);

                g2.drawString(
                        category,
                        x - Math.max(0, (category.length() * 3 - barWidth / 2)),
                        bottom + 20);
            }

            g2.dispose();
        }
    }

    class PieChartPanel extends JPanel {

        PieChartPanel() {
            setBackground(new Color(42, 42, 60));
        }

        @Override
        protected void paintComponent(Graphics g) {

            super.paintComponent(g);

            Graphics2D g2 = (Graphics2D) g.create();

            g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.BOLD, 18));

            g2.drawString("Category-wise Attempts", 20, 35);

            if (categories.isEmpty()) {
                g2.setFont(new Font("Arial", Font.PLAIN, 14));
                g2.drawString("No quiz data available", 30, 80);
                g2.dispose();
                return;
            }

            int total = 0;

            for (int value : attempts) {
                total += value;
            }

            if (total == 0) {
                g2.drawString("No attempts recorded", 30, 80);
                g2.dispose();
                return;
            }

            int size = Math.min(240, getHeight() - 130);
            int x = (getWidth() - size) / 2;
            int y = 60;

            Color[] colors = {
                    new Color(70, 130, 220),
                    new Color(70, 180, 120),
                    new Color(240, 170, 60),
                    new Color(210, 80, 100),
                    new Color(150, 100, 210)
            };

            int startAngle = 0;
            int accumulated = 0;

            for (int i = 0; i < categories.size(); i++) {

                accumulated += attempts.get(i);

                int endAngle = (int) Math.round(
                        accumulated * 360.0 / total);

                int arcAngle = endAngle - startAngle;

                g2.setColor(colors[i % colors.length]);

                g2.fillArc(
                        x, y, size, size,
                        startAngle, arcAngle);

                startAngle = endAngle;
            }

            int legendY = y + size + 25;

            g2.setFont(new Font("Arial", Font.PLAIN, 12));

            for (int i = 0; i < categories.size(); i++) {

                int column = i % 2;
                int row = i / 2;

                int legendX = 20 + column * (getWidth() / 2);
                int currentY = legendY + row * 25;

                g2.setColor(colors[i % colors.length]);
                g2.fillRect(legendX, currentY - 12, 12, 12);

                g2.setColor(Color.WHITE);

                String label = categories.get(i) + ": "
                        + attempts.get(i) + " attempts";

                g2.drawString(label, legendX + 18, currentY);
            }

            g2.dispose();
        }
    }

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {
            new QuizCharts().setVisible(true);
        });
    }
}

