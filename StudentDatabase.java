
import java.sql.*;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class StudentDatabase {

    private static final String DB_URL =
            "jdbc:sqlite:quiz_results.db";

    public static void initializeDatabase() {

        String sql = """
                CREATE TABLE IF NOT EXISTS students (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    username TEXT UNIQUE NOT NULL,
                    password_hash TEXT NOT NULL,
                    salt TEXT NOT NULL
                )
                """;

        try (
                Connection con = DriverManager.getConnection(DB_URL);
                Statement stmt = con.createStatement()
        ) {
            stmt.execute(sql);
            System.out.println("Student database ready!");

        } catch (SQLException e) {
            System.out.println("Database Error: " + e.getMessage());
        }
    }

    public static boolean registerStudent(
            String name,
            String username,
            String password) {

        if (name == null || name.isBlank()
                || username == null || username.isBlank()
                || password == null || password.length() < 8) {
            return false;
        }

        byte[] saltBytes = new byte[16];
        new SecureRandom().nextBytes(saltBytes);

        String salt = Base64.getEncoder()
                .encodeToString(saltBytes);

        String passwordHash = hashPassword(password, salt);

        String sql = """
                INSERT INTO students
                (name, username, password_hash, salt)
                VALUES (?, ?, ?, ?)
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, name.trim());
            ps.setString(2, username.trim());
            ps.setString(3, passwordHash);
            ps.setString(4, salt);

            ps.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Registration Error: " + e.getMessage());
            return false;
        }
    }

    public static String loginStudent(
            String username,
            String password) {

        String sql = """
                SELECT name, password_hash, salt
                FROM students
                WHERE username = ?
                """;

        try (Connection con = DriverManager.getConnection(DB_URL);
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, username.trim());

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    String name = rs.getString("name");
                    String storedHash = rs.getString("password_hash");
                    String salt = rs.getString("salt");

                    String enteredHash = hashPassword(password, salt);

                    if (MessageDigest.isEqual(
                            storedHash.getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8),
                            enteredHash.getBytes(
                                    java.nio.charset.StandardCharsets.UTF_8))) {
                        return name;
                    }
                }
            }

        } catch (SQLException e) {
            System.out.println("Login Error: " + e.getMessage());
        }

        return null;
    }

    private static String hashPassword(
            String password, String salt) {

        try {
            MessageDigest md =
                    MessageDigest.getInstance("SHA-256");

            String combined = salt + password;

            byte[] hash = md.digest(
                    combined.getBytes(
                            java.nio.charset.StandardCharsets.UTF_8));

            return Base64.getEncoder().encodeToString(hash);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        initializeDatabase();
    }
}

