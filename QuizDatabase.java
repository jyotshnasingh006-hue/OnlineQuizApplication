import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class QuizDatabase {

    private static final String URL = "jdbc:sqlite:quiz_results.db";

    public static void initializeDatabase() {
        String sql = "CREATE TABLE IF NOT EXISTS quiz_results ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "student_name TEXT NOT NULL, "
                + "category TEXT NOT NULL, "
                + "score INTEGER NOT NULL, "
                + "total_questions INTEGER NOT NULL, "
                + "percentage REAL NOT NULL, "
                + "date_time TEXT DEFAULT CURRENT_TIMESTAMP"
                + ")";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Database connected successfully!");

        } catch (Exception e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static void saveResult(
            String studentName,
            String category,
            int score,
            int totalQuestions) {

        String sql = "INSERT INTO quiz_results "
                + "(student_name, category, score, total_questions, percentage) "
                + "VALUES (?, ?, ?, ?, ?)";

        double percentage = totalQuestions > 0
                ? (score * 100.0) / totalQuestions
                : 0.0;

        try (Connection conn = DriverManager.getConnection(URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, studentName);
            pstmt.setString(2, category);
            pstmt.setInt(3, score);
            pstmt.setInt(4, totalQuestions);
            pstmt.setDouble(5, percentage);

            pstmt.executeUpdate();

            System.out.println("Quiz result saved successfully!");

        } catch (Exception e) {
            System.out.println("Error saving result: " + e.getMessage());
        }
    }

    

public static void displayResults() {

    String sql = "SELECT * FROM quiz_results ORDER BY id DESC";

    try (Connection conn = DriverManager.getConnection(URL);
         Statement stmt = conn.createStatement();
         java.sql.ResultSet rs = stmt.executeQuery(sql)) {

        System.out.println("\n========== SAVED QUIZ RESULTS ==========");

        boolean found = false;

        while (rs.next()) {
            found = true;

            System.out.println("----------------------------------------");
            System.out.println("Student Name: " + rs.getString("student_name"));
            System.out.println("Category: " + rs.getString("category"));
            System.out.println("Score: " + rs.getInt("score")
                    + "/" + rs.getInt("total_questions"));
            System.out.printf("Percentage: %.2f%%%n",
                    rs.getDouble("percentage"));
            System.out.println("Date & Time: " + rs.getString("date_time"));
        }

        if (!found) {
            System.out.println("No quiz results found.");
        }

        System.out.println("========================================");

    } catch (Exception e) {
        System.out.println("Error displaying results: " + e.getMessage());
    }
}


}