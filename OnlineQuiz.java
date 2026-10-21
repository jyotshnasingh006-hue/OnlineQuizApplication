import java.util.Scanner;

public class OnlineQuiz {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        System.out.println("================================");
        System.out.println("     ONLINE QUIZ APPLICATION");
        System.out.println("================================");

        System.out.print("Enter your name: ");
        String name = sc.nextLine();

        System.out.println("\nWelcome, " + name + "!");
        System.out.println("Let's start your Java Quiz.\n");

        String[] questions = {
            "1. Which keyword is used to create a class in Java?",
            "2. Which method is the entry point of a Java program?",
            "3. Which data type stores true or false?",
            "4. Which symbol ends a Java statement?",
            "5. Which keyword creates an object?"
        };

        String[][] options = {
            {"A. class", "B. define", "C. create", "D. function"},
            {"A. start()", "B. main()", "C. run()", "D. execute()"},
            {"A. int", "B. char", "C. boolean", "D. String"},
            {"A. :", "B. .", "C. ,", "D. ;"},
            {"A. new", "B. make", "C. object", "D. create"}
        };

        char[] answers = {'A', 'B', 'C', 'D', 'A'};

        int score = 0;

        Scanner input = sc;

        for (int i = 0; i < questions.length; i++) {

            System.out.println(questions[i]);

            for (String option : options[i]) {
                System.out.println(option);
            }

            System.out.print("Enter your answer (A/B/C/D): ");
            String choice = input.nextLine().trim();

            if (choice.length() == 1 &&
                Character.toUpperCase(choice.charAt(0)) == answers[i]) {

                System.out.println("Correct answer!\n");
                score++;

            } else {
                System.out.println(
                    "Wrong answer! Correct answer: " + answers[i] + "\n"
                );
            }
        }

        System.out.println("================================");
        System.out.println("           QUIZ RESULT");
        System.out.println("================================");
        System.out.println("Student Name: " + name);
        System.out.println("Total Questions: " + questions.length);
        System.out.println("Correct Answers: " + score);
        System.out.println("Wrong Answers: " + (questions.length - score));
        System.out.println("Final Score: " + score + "/" + questions.length);

        double percentage =
            (score * 100.0) / questions.length;

        System.out.printf("Percentage: %.2f%%\n", percentage);

        System.out.println("================================");

        sc.close();
    }
}