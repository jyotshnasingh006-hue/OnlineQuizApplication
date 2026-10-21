
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import java.util.LinkedHashMap;
import java.util.Map;

public class OnlineQuizGUI extends JFrame {

    private String studentName = "";
    private String selectedCategory = "";

    private JPanel mainPanel;
    private JLabel timerLabel;
    private JLabel questionLabel;
    private JLabel progressLabel;

    private JRadioButton[] optionButtons;
    private ButtonGroup optionGroup;

    private JButton nextButton;
    private JButton previousButton;

    private javax.swing.Timer timer;

    private int timeLeft = 600;
    private int currentQuestion = 0;
    private int score = 0;

    private boolean finished = false;

    private final Map<String, ArrayList<Question>> quizBank =
            new LinkedHashMap<>();

    private final Map<Integer, Integer> userAnswers =
            new LinkedHashMap<>();

    // Question class
    static class Question {

        String question;
        String[] options;
        int correctAnswer;

        Question(String question, String[] options, int correctAnswer) {
            this.question = question;
            this.options = options;
            this.correctAnswer = correctAnswer;
        }
    }

    // Theme colors
    private final Color BACKGROUND = new Color(18, 24, 45);
    private final Color CARD = new Color(30, 39, 68);
    private final Color PRIMARY = new Color(124, 92, 255);
    private final Color TEXT = new Color(245, 247, 255);
    private final Color MUTED = new Color(180, 190, 215);
    private final Color SUCCESS = new Color(34, 197, 140);

public OnlineQuizGUI() {
this("");
}

public OnlineQuizGUI(String loggedInStudentName) {
setTitle("Online Quiz Application");
setSize(800, 600);
setMinimumSize(new Dimension(700, 500));
setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
setLocationRelativeTo(null);


createQuestionBank();

if (loggedInStudentName != null
        && !loggedInStudentName.trim().isEmpty()) {
    studentName = loggedInStudentName.trim();
    showCategoryPage();
} else {
    showLoginPage();
}

setVisible(true);


}


    // Create all quiz questions
    private void createQuestionBank() {

        ArrayList<Question> javaQuestions = new ArrayList<>();

        javaQuestions.add(new Question(
                "Which keyword is used to inherit a class in Java?",
                new String[]{"implements", "extends", "inherits", "super"},
                1));

        javaQuestions.add(new Question(
                "Which method is the entry point of a Java program?",
                new String[]{"start()", "run()", "main()", "init()"},
                2));

        javaQuestions.add(new Question(
                "Which of these is not a primitive data type in Java?",
                new String[]{"int", "char", "boolean", "String"},
                3));

        javaQuestions.add(new Question(
                "Which keyword creates an object in Java?",
                new String[]{"new", "class", "this", "void"},
                0));

        javaQuestions.add(new Question(
                "Which keyword is used to define a constant in Java?",
                new String[]{"static", "final", "const", "fixed"},
                1));
        javaQuestions.add(new Question(
                "Which company developed Java?",
                new String[]{"Microsoft", "Sun Microsystems", "Google", "Apple"},
                1));

        javaQuestions.add(new Question(
                "Which keyword is used to create a subclass?",
                new String[]{"extends", "implements", "import", "package"},
                0));

        javaQuestions.add(new Question(
                "Which data type stores whole numbers?",
                new String[]{"float", "String", "int", "boolean"},
                2));

        javaQuestions.add(new Question(
                "Which keyword creates a class?",
                new String[]{"object", "class", "new", "void"},
                1));

        javaQuestions.add(new Question(
                "Which method compares string contents?",
                new String[]{"==", "compare", "equals()", "match()"},
                2));

        javaQuestions.add(new Question(
                "Which collection stores unique elements?",
                new String[]{"ArrayList", "HashSet", "LinkedList", "Array"},
                1));

        javaQuestions.add(new Question(
                "Which keyword prevents inheritance?",
                new String[]{"static", "private", "final", "abstract"},
                2));

        javaQuestions.add(new Question(
                "Which package contains Scanner?",
                new String[]{"java.io", "java.util", "java.lang", "java.net"},
                1));

        javaQuestions.add(new Question(
                "Which loop executes at least once?",
                new String[]{"for", "while", "do-while", "enhanced for"},
                2));

        javaQuestions.add(new Question(
                "Which keyword refers to the current object?",
                new String[]{"super", "this", "self", "current"},
                1));

        javaQuestions.add(new Question(
                "Which exception occurs when dividing an integer by zero?",
                new String[]{"IOException", "NullPointerException",
                "ArithmeticException", "ClassNotFoundException"},
                2));

        javaQuestions.add(new Question(
                "Which access modifier provides the widest visibility?",
                new String[]{"private", "protected", "default", "public"},
                3));

        javaQuestions.add(new Question(
                "Which keyword is used to implement an interface?",
                new String[]{"extends", "implements", "interface", "instanceof"},
                1));

        javaQuestions.add(new Question(
                "Which operator checks equality of primitive values?",
                new String[]{"=", "==", "!=", "equals"},
                1));

        javaQuestions.add(new Question(
                "Which block is used to handle an exception?",
                new String[]{"try-catch", "if-else", "for-loop", "switch"},
                0));
                
        quizBank.put("Java", javaQuestions);

        ArrayList<Question> pythonQuestions = new ArrayList<>();

        pythonQuestions.add(new Question(
                "Which keyword is used to define a function in Python?",
                new String[]{"function", "fun", "def", "define"},
                2));

        pythonQuestions.add(new Question(
                "Which symbol is used for comments in Python?",
                new String[]{"//", "#", "/*", "--"},
                1));

        pythonQuestions.add(new Question(
                "Which data type stores True or False?",
                new String[]{"int", "str", "list", "bool"},
                3));

        pythonQuestions.add(new Question(
                "Which function displays output in Python?",
                new String[]{"show()", "display()", "print()", "write()"},
                2));

        pythonQuestions.add(new Question(
                "Which collection stores key-value pairs in Python?",
                new String[]{"List", "Tuple", "Dictionary", "Set"},
                2));
        pythonQuestions.add(new Question(
                "Which company developed Python originally?",
                new String[]{"Microsoft", "Google", "Python Software Foundation", "It was created by Guido van Rossum"},
                3));

        pythonQuestions.add(new Question(
                "Which symbol is used for exponentiation in Python?",
                new String[]{"^", "**", "//", "%"},
                3));

        pythonQuestions.add(new Question(
                "What is the output of len('Hello')?",
                new String[]{"4", "5", "6", "Error"},
                1));

        pythonQuestions.add(new Question(
                "Which data type is immutable?",
                new String[]{"List", "Dictionary", "Tuple", "Set"},
                2));

        pythonQuestions.add(new Question(
                "Which keyword is used for a loop?",
                new String[]{"repeat", "loop", "for", "iterate"},
                2));

        pythonQuestions.add(new Question(
                "Which method adds an item to a list?",
                new String[]{"add()", "append()", "insertEnd()", "push()"},
                1));

        pythonQuestions.add(new Question(
                "Which keyword checks a condition?",
                new String[]{"if", "when", "check", "condition"},
                0));

        pythonQuestions.add(new Question(
                "What does // perform in Python?",
                new String[]{"Normal division", "Floor division", "Multiplication", "Exponentiation"},
                1));

        pythonQuestions.add(new Question(
                "Which function converts a value to an integer?",
                new String[]{"str()", "float()", "int()", "bool()"},
                2));

        pythonQuestions.add(new Question(
                "Which keyword defines a class in Python?",
                new String[]{"object", "class", "define", "struct"},
                1));

        pythonQuestions.add(new Question(
                "Which collection stores key-value pairs?",
                new String[]{"List", "Tuple", "Set", "Dictionary"},
                3));

        pythonQuestions.add(new Question(
                "Which keyword is used to import a module?",
                new String[]{"include", "using", "import", "require"},
                2));

        pythonQuestions.add(new Question(
                "What does the break statement do?",
                new String[]{"Skips one iteration", "Ends the loop", "Restarts the loop", "Pauses the program"},
                1));

        pythonQuestions.add(new Question(
                "Which value represents no value in Python?",
                new String[]{"null", "None", "undefined", "empty"},
                1));

        pythonQuestions.add(new Question(
                "Which operator checks whether two values are equal?",
                new String[]{"=", "==", ":=", "!="},
                1));

        quizBank.put("Python", pythonQuestions);

        ArrayList<Question> cppQuestions = new ArrayList<>();

        cppQuestions.add(new Question(
                "Which symbol ends a statement in C++?",
                new String[]{".", ":", ";", ","},
                2));

        cppQuestions.add(new Question(
                "Which header is commonly used for cout and cin?",
                new String[]{"iostream", "stdio.h", "string.h", "math.h"},
                0));

        cppQuestions.add(new Question(
                "Which operator accesses a member through an object?",
                new String[]{"->", ".", "::", "&"},
                1));

        cppQuestions.add(new Question(
                "Which function is the entry point of a C++ program?",
                new String[]{"start()", "main()", "begin()", "run()"},
                1));

        cppQuestions.add(new Question(
                "Which concept allows the same function name with different parameters?",
                new String[]{"Inheritance", "Encapsulation",
                        "Function overloading", "Abstraction"},
                2));
        cppQuestions.add(new Question(
                "Who developed the C++ programming language?",
                new String[]{"Dennis Ritchie", "Bjarne Stroustrup", "James Gosling", "Guido van Rossum"},
                1));

        cppQuestions.add(new Question(
                "Which operator is used to access a member through a pointer?",
                new String[]{".", "::", "->", "&"},
                2));

        cppQuestions.add(new Question(
               "Which keyword is used to create an object dynamically?",
               new String[]{"create", "new", "object", "malloc"},
               1));

        cppQuestions.add(new Question(
               "Which header file is commonly used for string operations?",
               new String[]{"<iostream>", "<string>", "<cmath>", "<vector>"},
               1));

        cppQuestions.add(new Question(
               "Which concept allows a class to acquire properties of another class?",
               new String[]{"Polymorphism", "Encapsulation", "Inheritance", "Abstraction"},
               2));

        cppQuestions.add(new Question(
               "Which symbol is used to declare a pointer?",
               new String[]{"&", "*", "%", "#"},
               1));

        cppQuestions.add(new Question(
               "Which loop checks its condition before executing the body?",
               new String[]{"do-while", "while", "Both always execute once", "None"},
               1));

        cppQuestions.add(new Question(
               "Which keyword makes a member belong to the class rather than an object?",
               new String[]{"static", "public", "virtual", "friend"},
               0));

        cppQuestions.add(new Question(
               "Which access specifier allows access from anywhere?",
               new String[]{"private", "protected", "public", "internal"},
               2));

        cppQuestions.add(new Question(
               "Which function is called automatically when an object is created?",
               new String[]{"Destructor", "Constructor", "Main function", "Operator"},
        1));

        cppQuestions.add(new Question(
              "Which operator is used for logical AND?",
              new String[]{"&", "||", "&&", "!"},
              2));

        cppQuestions.add(new Question(
              "Which container stores elements in a sequence and can resize dynamically?",
              new String[]{"vector", "int", "char", "bool"},
              0));

        cppQuestions.add(new Question(
              "Which keyword can be used to prevent overriding in a derived class?",
              new String[]{"override", "final", "virtual", "friend"},
              1));

        cppQuestions.add(new Question(
               "Which statement skips the remaining statements in the current loop iteration?",
               new String[]{"break", "return", "continue", "exit"},
               2));

        cppQuestions.add(new Question(
               "Which header provides the standard input and output stream objects?",
               new String[]{"<fstream>", "<iostream>", "<iomanip>", "<sstream>"},
               1));
        quizBank.put("C++", cppQuestions);

        ArrayList<Question> gkQuestions = new ArrayList<>();

        gkQuestions.add(new Question(
                "What is the capital of India?",
                new String[]{"Mumbai", "New Delhi", "Kolkata", "Chennai"},
                1));

        gkQuestions.add(new Question(
                "Which planet is known as the Red Planet?",
                new String[]{"Venus", "Jupiter", "Mars", "Saturn"},
                2));

        gkQuestions.add(new Question(
                "How many days are there in a leap year?",
                new String[]{"365", "366", "364", "360"},
                1));

        gkQuestions.add(new Question(
                "Which is the largest ocean on Earth?",
                new String[]{"Atlantic Ocean", "Indian Ocean",
                        "Arctic Ocean", "Pacific Ocean"},
                3));

        gkQuestions.add(new Question(
                "Which gas do plants absorb during photosynthesis?",
                new String[]{"Oxygen", "Nitrogen",
                        "Carbon dioxide", "Hydrogen"},
                2));

        gkQuestions.add(new Question(
                "What is the national animal of India?",
                new String[]{"Lion", "Tiger", "Elephant", "Leopard"},
                1));

        gkQuestions.add(new Question(
                "Which is the largest continent?",
                new String[]{"Africa", "Europe", "Asia", "Australia"},
                2));

        gkQuestions.add(new Question(
                "Who was the first Prime Minister of India?",
                new String[]{"Mahatma Gandhi", "Jawaharlal Nehru", "Sardar Patel", "Dr. Rajendra Prasad"},
                1));

        gkQuestions.add(new Question(
                "Which planet is closest to the Sun?",
                new String[]{"Venus", "Earth", "Mercury", "Mars"},
                2));

        gkQuestions.add(new Question(
                "What is the currency of Japan?",
                new String[]{"Yuan", "Won", "Yen", "Dollar"},
                2));

        gkQuestions.add(new Question(
                "Which is the largest mammal in the world?",
                new String[]{"African Elephant", "Blue Whale", "Giraffe", "Shark"},
                 1));

        gkQuestions.add(new Question(
                "How many sides does a hexagon have?",
                new String[]{"Five", "Six", "Seven", "Eight"},
                1));

        gkQuestions.add(new Question(
                "Which organ pumps blood throughout the human body?",
                new String[]{"Lungs", "Brain", "Heart", "Kidneys"},
                2));

        gkQuestions.add(new Question(
                "Which is the smallest prime number?",
                new String[]{"0", "1", "2", "3"},
                2));

        gkQuestions.add(new Question(
                "Which festival is widely known as the Festival of Lights in India?",
                new String[]{"Holi", "Diwali", "Eid", "Pongal"},
                1));

        gkQuestions.add(new Question(
                "What is the chemical symbol for gold?",
                new String[]{"Ag", "Au", "Fe", "Go"},
                1));

        gkQuestions.add(new Question(
                "Which is the largest planet in our Solar System?",
                new String[]{"Earth", "Saturn", "Jupiter", "Neptune"},
                2));

        gkQuestions.add(new Question(
                "How many colours are traditionally listed in a rainbow?",
                new String[]{"Five", "Six", "Seven", "Eight"},
                2));

        gkQuestions.add(new Question(
                "Which instrument is used to measure temperature?",
                new String[]{"Barometer", "Thermometer", "Speedometer", "Hygrometer"},
                1));

        gkQuestions.add(new Question(
                "Which gas is most abundant in Earth's atmosphere?",
                new String[]{"Oxygen", "Carbon dioxide", "Nitrogen", "Hydrogen"},
                2));

        quizBank.put("General Knowledge", gkQuestions);
    }

    // Common panel creator
    private JPanel createPanel() {

        JPanel panel = new JPanel();
        panel.setBackground(BACKGROUND);
        panel.setBorder(
                BorderFactory.createEmptyBorder(25, 35, 25, 35));

        return panel;
    }

    // Create styled button
    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Segoe UI", Font.BOLD, 15));
        button.setBackground(PRIMARY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setBorder(
                BorderFactory.createEmptyBorder(12, 22, 12, 22));

        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR));

        return button;
    }

    // Create heading
    private JLabel createTitle(String text) {

        JLabel label = new JLabel(
                text, SwingConstants.CENTER);

        label.setFont(
                new Font("Segoe UI", Font.BOLD, 28));

        label.setForeground(TEXT);

        return label;
    }

    // Login page
    private void showLoginPage() {

        if (timer != null) {
            timer.stop();
        }

        JPanel panel = createPanel();

        panel.setLayout(
                new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 2;

        JLabel title = createTitle(
                "ONLINE QUIZ APPLICATION");

        gbc.gridy = 0;
        panel.add(title, gbc);

        JLabel subtitle = new JLabel(
                "Test your knowledge and challenge yourself!",
                SwingConstants.CENTER);

        subtitle.setForeground(MUTED);
        subtitle.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        gbc.gridy = 1;
        panel.add(subtitle, gbc);

        JLabel nameLabel = new JLabel(
                "Enter Student Name:");

        nameLabel.setForeground(TEXT);
        nameLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 16));

        gbc.gridy = 2;
        panel.add(nameLabel, gbc);

        JTextField nameField = new JTextField(22);

        nameField.setFont(
                new Font("Segoe UI", Font.PLAIN, 16));

        gbc.gridy = 3;
        panel.add(nameField, gbc);

        JButton startButton = createButton("Continue");

        gbc.gridy = 4;
        panel.add(startButton, gbc);

        startButton.addActionListener(e -> {

            studentName = nameField.getText().trim();

            if (studentName.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your name!",
                        "Name Required",
                        JOptionPane.WARNING_MESSAGE);

                return;
            }

            showCategoryPage();
        });

        setMainPanel(panel);
    }

    // Category selection page
    private void showCategoryPage() {

        JPanel panel = createPanel();

        panel.setLayout(
                new GridBagLayout());

        GridBagConstraints gbc = new GridBagConstraints();

        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridwidth = 1;

        JLabel title = createTitle(
                "Choose Your Quiz Category");

        gbc.gridy = 0;
        gbc.gridwidth = 2;

        panel.add(title, gbc);

        JLabel welcome = new JLabel(
                "Welcome, " + studentName + "!");

        welcome.setForeground(MUTED);
        welcome.setFont(
                new Font("Segoe UI", Font.PLAIN, 16));

        gbc.gridy = 1;

        panel.add(welcome, gbc);

        String[] categories = {
                "Java",
                "Python",
                "C++",
                "General Knowledge"
        };

        gbc.gridwidth = 1;

        for (int i = 0; i < categories.length; i++) {

            final String category = categories[i];

            JButton categoryButton =
                    createButton(category);

            gbc.gridx = i % 2;
            gbc.gridy = 2 + i / 2;

            panel.add(categoryButton, gbc);

            categoryButton.addActionListener(e -> {

                selectedCategory = category;

                startQuiz();
            });
        }

        setMainPanel(panel);
    }

    // Start quiz
    private void startQuiz() {

        currentQuestion = 0;
        score = 0;
        timeLeft = 600;
        finished = false;

        userAnswers.clear();

        showQuizPage();

        startTimer();
    }

    // Quiz page
    private void showQuizPage() {

        JPanel panel = createPanel();

        panel.setLayout(
                new BorderLayout(15, 20));

        JPanel topPanel = new JPanel(
                new BorderLayout());

        topPanel.setBackground(BACKGROUND);

        JLabel categoryLabel = new JLabel(
                selectedCategory + " Quiz");

        categoryLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 22));

        categoryLabel.setForeground(TEXT);

        topPanel.add(
                categoryLabel,
                BorderLayout.WEST);

        timerLabel = new JLabel(
                "Time Left: 10:00");

        timerLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 18));

        timerLabel.setForeground(SUCCESS);

        topPanel.add(
                timerLabel,
                BorderLayout.EAST);

        panel.add(
                topPanel,
                BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(
                new GridBagLayout());

        centerPanel.setBackground(CARD);

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25));

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        progressLabel = new JLabel();

        progressLabel.setForeground(MUTED);
        progressLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 15));

        centerPanel.add(
                progressLabel, gbc);

        gbc.gridy = 1;

        questionLabel = new JLabel();

        questionLabel.setForeground(TEXT);
        questionLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 19));

        centerPanel.add(
                questionLabel, gbc);

        optionButtons = new JRadioButton[4];

        optionGroup = new ButtonGroup();

        for (int i = 0; i < 4; i++) {

            gbc.gridy = i + 2;

            optionButtons[i] = new JRadioButton();

            optionButtons[i].setBackground(CARD);
            optionButtons[i].setForeground(TEXT);

            optionButtons[i].setFont(
                    new Font("Segoe UI", Font.PLAIN, 16));

            optionButtons[i].setFocusPainted(false);

            optionGroup.add(
                    optionButtons[i]);

            centerPanel.add(
                    optionButtons[i], gbc);
        }

        panel.add(
                centerPanel,
                BorderLayout.CENTER);

        JPanel bottomPanel = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 15, 5));

        bottomPanel.setBackground(BACKGROUND);

        previousButton = createButton("Previous");

        nextButton = createButton("Next");

        JButton finishButton =
                createButton("Finish Quiz");

        bottomPanel.add(previousButton);
        bottomPanel.add(nextButton);
        bottomPanel.add(finishButton);

        panel.add(
                bottomPanel,
                BorderLayout.SOUTH);

        previousButton.addActionListener(e -> {

            saveAnswer();

            if (currentQuestion > 0) {

                currentQuestion--;

                displayQuestion();
            }
        });

        nextButton.addActionListener(e -> {

            saveAnswer();

            ArrayList<Question> questions =
                    quizBank.get(selectedCategory);

            if (currentQuestion < questions.size() - 1) {

                currentQuestion++;

                displayQuestion();

            } else {

                finishQuiz();
            }
        });

        finishButton.addActionListener(e -> {

            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to finish the quiz?",
                    "Finish Quiz",
                    JOptionPane.YES_NO_OPTION);

            if (choice == JOptionPane.YES_OPTION) {

                finishQuiz();
            }
        });

        setMainPanel(panel);

        displayQuestion();
    }

    // Display current question
    private void displayQuestion() {

        ArrayList<Question> questions =
                quizBank.get(selectedCategory);

        Question question =
                questions.get(currentQuestion);

        progressLabel.setText(
                "Question " + (currentQuestion + 1)
                        + " of " + questions.size());

        questionLabel.setText(
                "<html><div style='width:520px'>"
                        + question.question
                        + "</div></html>");

        optionGroup.clearSelection();

        for (int i = 0; i < 4; i++) {

            optionButtons[i].setText(
                    question.options[i]);

            optionButtons[i].setSelected(false);
        }

        if (userAnswers.containsKey(currentQuestion)) {

            int answer =
                    userAnswers.get(currentQuestion);

            if (answer >= 0 && answer < 4) {

                optionButtons[answer].setSelected(true);
            }
        }

        previousButton.setEnabled(
                currentQuestion > 0);

        if (currentQuestion ==
                questions.size() - 1) {

            nextButton.setText("Submit Quiz");

        } else {

            nextButton.setText("Next");
        }
    }

    // Save selected answer
    private void saveAnswer() {

        for (int i = 0; i < optionButtons.length; i++) {

            if (optionButtons[i].isSelected()) {

                userAnswers.put(
                        currentQuestion, i);

                return;
            }
        }

        userAnswers.remove(currentQuestion);
    }

    // Start countdown timer
    private void startTimer() {

        if (timer != null) {

            timer.stop();
        }

        timer = new javax.swing.Timer(
                1000, e -> {

            timeLeft--;

            int minutes = timeLeft / 60;
            int seconds = timeLeft % 60;

            timerLabel.setText(
                    String.format(
                            "Time Left: %02d:%02d",
                            minutes, seconds));

            if (timeLeft <= 60) {

                timerLabel.setForeground(
                        Color.RED);
            }

            if (timeLeft <= 0) {

                timer.stop();

                JOptionPane.showMessageDialog(
                        this,
                        "Time is up!");

                finishQuiz();
            }
        });

        timer.start();
    }

    // Calculate final score
   private void finishQuiz() {

   if (finished) {
    return;
}

   saveAnswer();

   finished = true;

   if (timer != null) {
      timer.stop();
   }

   score = 0;

   ArrayList<Question> questions =
        quizBank.get(selectedCategory);

   for (int i = 0; i < questions.size(); i++) {

    Integer answer = userAnswers.get(i);

    if (answer != null
            && answer == questions.get(i).correctAnswer) {
        score++;
    }
}

// Save quiz result in SQLite database
QuizDatabase.saveResult(
        studentName,
        selectedCategory,
        score,
        questions.size()
);

showResult();

}

    // Result page
    private void showResult() {

        JPanel panel = createPanel();

        panel.setLayout(
                new GridBagLayout());

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.gridx = 0;
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = createTitle(
                "Quiz Completed!");

        gbc.gridy = 0;

        panel.add(title, gbc);

        JLabel nameLabel = new JLabel(
                "Student: " + studentName,
                SwingConstants.CENTER);

        nameLabel.setForeground(MUTED);

        nameLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 17));

        gbc.gridy = 1;

        panel.add(nameLabel, gbc);

        JLabel categoryLabel = new JLabel(
                "Category: " + selectedCategory,
                SwingConstants.CENTER);

        categoryLabel.setForeground(MUTED);

        categoryLabel.setFont(
                new Font("Segoe UI", Font.PLAIN, 17));

        gbc.gridy = 2;

        panel.add(categoryLabel, gbc);

        int total =
                quizBank.get(selectedCategory).size();

        double percentage =
                (score * 100.0) / total;

        JLabel scoreLabel = new JLabel(
                "Score: " + score + " / " + total,
                SwingConstants.CENTER);

        scoreLabel.setForeground(SUCCESS);

        scoreLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 26));

        gbc.gridy = 3;

        panel.add(scoreLabel, gbc);

        JLabel percentageLabel = new JLabel(
                String.format(
                        "Percentage: %.2f%%",
                        percentage),
                SwingConstants.CENTER);

        percentageLabel.setForeground(TEXT);

        percentageLabel.setFont(
                new Font("Segoe UI", Font.BOLD, 21));

        gbc.gridy = 4;

        panel.add(percentageLabel, gbc);

        String message;

        if (percentage >= 80) {

            message = "Excellent work!";

        } else if (percentage >= 60) {

            message = "Good job! Keep learning.";

        } else if (percentage >= 40) {

            message = "Nice attempt! Keep practising.";

        } else {

            message = "Keep practising. You can improve!";
        }

        JLabel feedback = new JLabel(
                message,
                SwingConstants.CENTER);

        feedback.setForeground(MUTED);

        feedback.setFont(
                new Font("Segoe UI", Font.PLAIN, 16));

        gbc.gridy = 5;

        panel.add(feedback, gbc);

        JPanel buttons = new JPanel(
                new FlowLayout(
                        FlowLayout.CENTER, 12, 5));

        buttons.setBackground(BACKGROUND);

        JButton restartButton =
                createButton("Restart Quiz");

        JButton categoryButton =
                createButton("Choose Category");

        JButton exitButton =
                createButton("Exit");

        buttons.add(restartButton);
        buttons.add(categoryButton);
        buttons.add(exitButton);

        gbc.gridy = 6;

        panel.add(buttons, gbc);

        restartButton.addActionListener(e -> {

            startQuiz();
        });

        categoryButton.addActionListener(e -> {

            if (timer != null) {

                timer.stop();
            }

            showCategoryPage();
        });

        exitButton.addActionListener(e -> {

            dispose();
        });

        setMainPanel(panel);
    }

    // Update application screen
    private void setMainPanel(JPanel panel) {

        mainPanel = panel;

        setContentPane(mainPanel);

        revalidate();

        repaint();
    }

    // Main method
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            new OnlineQuizGUI();
        });
    }
}