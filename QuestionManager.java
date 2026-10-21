
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class QuestionManager extends JFrame {

    private static final String DB_URL = "jdbc:sqlite:quiz_results.db";

    private JComboBox<String> categoryBox;
    private JTextField questionField;
    private JTextField optionAField;
    private JTextField optionBField;
    private JTextField optionCField;
    private JTextField optionDField;
    private JComboBox<String> answerBox;

    private DefaultTableModel tableModel;
    private JTable questionTable;

    public QuestionManager() {
        setTitle("Admin - Question Management");
        setSize(1000, 650);
        setMinimumSize(new Dimension(800, 550));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        createTable();
        createUI();
        loadQuestions();

        setVisible(true);
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS quiz_questions (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    category TEXT NOT NULL,
                    question TEXT NOT NULL,
                    option_a TEXT NOT NULL,
                    option_b TEXT NOT NULL,
                    option_c TEXT NOT NULL,
                    option_d TEXT NOT NULL,
                    correct_answer TEXT NOT NULL
                )
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement()) {
            st.execute(sql);
        } catch (SQLException e) {
            showError(e);
        }
    }

    private void createUI() {
        JPanel main = new JPanel(new BorderLayout(10, 10));
        main.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel heading = new JLabel(
                "Question Management System",
                SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        main.add(heading, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridLayout(8, 2, 8, 8));

        categoryBox = new JComboBox<>(new String[]{
                "Java", "Python", "C++", "General Knowledge"
        });

        questionField = new JTextField();
        optionAField = new JTextField();
        optionBField = new JTextField();
        optionCField = new JTextField();
        optionDField = new JTextField();

        answerBox = new JComboBox<>(new String[]{"A", "B", "C", "D"});

        form.add(new JLabel("Category:"));
        form.add(categoryBox);

        form.add(new JLabel("Question:"));
        form.add(questionField);

        form.add(new JLabel("Option A:"));
        form.add(optionAField);

        form.add(new JLabel("Option B:"));
        form.add(optionBField);

        form.add(new JLabel("Option C:"));
        form.add(optionCField);

        form.add(new JLabel("Option D:"));
        form.add(optionDField);

        form.add(new JLabel("Correct Answer:"));
        form.add(answerBox);

        JButton addButton = new JButton("Add Question");
        JButton updateButton = new JButton("Update Selected");
        JButton deleteButton = new JButton("Delete Selected");
        JButton clearButton = new JButton("Clear Form");

        JPanel actions = new JPanel(new FlowLayout());
        actions.add(addButton);
        actions.add(updateButton);
        actions.add(deleteButton);
        actions.add(clearButton);

        JPanel top = new JPanel(new BorderLayout(8, 8));
        top.add(form, BorderLayout.CENTER);
        top.add(actions, BorderLayout.SOUTH);

        tableModel = new DefaultTableModel(new String[]{
                "ID", "Category", "Question",
                "Option A", "Option B", "Option C",
                "Option D", "Answer"
        }, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        questionTable = new JTable(tableModel);
        questionTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        questionTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION);

        questionTable.getColumnModel().getColumn(0).setPreferredWidth(45);
        questionTable.getColumnModel().getColumn(1).setPreferredWidth(130);
        questionTable.getColumnModel().getColumn(2).setPreferredWidth(260);

        for (int i = 3; i < 8; i++) {
            questionTable.getColumnModel()
                    .getColumn(i).setPreferredWidth(120);
        }

        questionTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelection();
            }
        });

        addButton.addActionListener(e -> addQuestion());
        updateButton.addActionListener(e -> updateQuestion());
        deleteButton.addActionListener(e -> deleteQuestion());
        clearButton.addActionListener(e -> clearForm());

        main.add(top, BorderLayout.NORTH);
        main.add(new JScrollPane(questionTable), BorderLayout.CENTER);

        setContentPane(main);
    }

    private boolean validateForm() {
        if (questionField.getText().trim().isEmpty()
                || optionAField.getText().trim().isEmpty()
                || optionBField.getText().trim().isEmpty()
                || optionCField.getText().trim().isEmpty()
                || optionDField.getText().trim().isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please fill in the question and all four options.");
            return false;
        }

        return true;
    }

    private void addQuestion() {
        if (!validateForm()) {
            return;
        }

        String sql = """
                INSERT INTO quiz_questions
                (category, question, option_a, option_b,
                 option_c, option_d, correct_answer)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            fillStatement(ps);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question added successfully!");
            loadQuestions();
            clearForm();

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void updateQuestion() {
        int row = questionTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a question from the table.");
            return;
        }

        if (!validateForm()) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        String sql = """
                UPDATE quiz_questions
                SET category = ?, question = ?, option_a = ?,
                    option_b = ?, option_c = ?, option_d = ?,
                    correct_answer = ?
                WHERE id = ?
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            fillStatement(ps);
            ps.setInt(8, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question updated successfully!");
            loadQuestions();
            clearForm();

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void deleteQuestion() {
        int row = questionTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a question to delete.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this question?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION);

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM quiz_questions WHERE id = ?")) {

            ps.setInt(1, id);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Question deleted.");
            loadQuestions();
            clearForm();

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void fillStatement(PreparedStatement ps) throws SQLException {
        ps.setString(1, (String) categoryBox.getSelectedItem());
        ps.setString(2, questionField.getText().trim());
        ps.setString(3, optionAField.getText().trim());
        ps.setString(4, optionBField.getText().trim());
        ps.setString(5, optionCField.getText().trim());
        ps.setString(6, optionDField.getText().trim());
        ps.setString(7, (String) answerBox.getSelectedItem());
    }

    private void loadQuestions() {
        tableModel.setRowCount(0);

        String sql = """
                SELECT id, category, question, option_a,
                       option_b, option_c, option_d, correct_answer
                FROM quiz_questions
                ORDER BY id DESC
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                tableModel.addRow(new Object[]{
                        rs.getInt("id"),
                        rs.getString("category"),
                        rs.getString("question"),
                        rs.getString("option_a"),
                        rs.getString("option_b"),
                        rs.getString("option_c"),
                        rs.getString("option_d"),
                        rs.getString("correct_answer")
                });
            }

        } catch (SQLException e) {
            showError(e);
        }
    }

    private void fillFormFromSelection() {
        int row = questionTable.getSelectedRow();

        if (row == -1) {
            return;
        }

        categoryBox.setSelectedItem(tableModel.getValueAt(row, 1));
        questionField.setText(String.valueOf(tableModel.getValueAt(row, 2)));
        optionAField.setText(String.valueOf(tableModel.getValueAt(row, 3)));
        optionBField.setText(String.valueOf(tableModel.getValueAt(row, 4)));
        optionCField.setText(String.valueOf(tableModel.getValueAt(row, 5)));
        optionDField.setText(String.valueOf(tableModel.getValueAt(row, 6)));
        answerBox.setSelectedItem(tableModel.getValueAt(row, 7));
    }

    private void clearForm() {
        questionTable.clearSelection();
        questionField.setText("");
        optionAField.setText("");
        optionBField.setText("");
        optionCField.setText("");
        optionDField.setText("");
        categoryBox.setSelectedIndex(0);
        answerBox.setSelectedIndex(0);
    }

    private void showError(SQLException e) {
        JOptionPane.showMessageDialog(
                this,
                "Database error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(QuestionManager::new);
    }
}
