
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;

public class QuizTheme {

    // Background colors
    public static final Color BACKGROUND =
            new Color(18, 24, 45);

    public static final Color CARD =
            new Color(30, 39, 68);

    // Accent colors
    public static final Color PRIMARY =
            new Color(124, 92, 255);

    public static final Color SUCCESS =
            new Color(34, 197, 140);

    public static final Color TEXT =
            new Color(245, 247, 255);

    public static final Color MUTED =
            new Color(180, 190, 215);

    public static void applyTheme() {

        UIManager.put(
                "OptionPane.background",
                CARD
        );

        UIManager.put(
                "Panel.background",
                CARD
        );

        UIManager.put(
                "OptionPane.messageForeground",
                TEXT
        );

        UIManager.put(
                "Button.background",
                PRIMARY
        );

        UIManager.put(
                "Button.foreground",
                Color.WHITE
        );

        UIManager.put(
                "Button.font",
                new Font("Segoe UI", Font.BOLD, 14)
        );
    }

    public static JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(
                new Font("Segoe UI", Font.BOLD, 15)
        );

        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);

        button.setFocusPainted(false);
        button.setOpaque(true);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                PRIMARY, 1
                        ),
                        new EmptyBorder(10, 20, 10, 20)
                )
        );

        button.setCursor(
                new java.awt.Cursor(
                        java.awt.Cursor.HAND_CURSOR
                )
        );

        return button;
    }

    public static JLabel createTitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 26)
        );

        label.setForeground(TEXT);

        return label;
    }

    public static JLabel createSubtitle(String text) {

        JLabel label = new JLabel(text);

        label.setFont(
                new Font("Segoe UI", Font.PLAIN, 15)
        );

        label.setForeground(MUTED);

        return label;
    }
}
