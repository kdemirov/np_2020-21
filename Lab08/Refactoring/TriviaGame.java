package Lab08.Refactoring;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

enum QuestionType {
    TRUEFALSE, FREEFORM
}

abstract class Question {
    private String question;
    private String answer;
    private int value;

    public Question(String question, String answer, int value) {
        this.question = question;
        this.answer = answer;
        this.value = value;
    }

    abstract void showQuestion();

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getValue() {
        return value;
    }

    public static Question factoryQuestion(String question, String answer, int value, QuestionType questionType) {
        switch (questionType) {
            case TRUEFALSE:
                return new TrueFalseQuestion(question, answer, value);
            case FREEFORM:
                return new FreeFormQuestion(question, answer, value);
            default:
                return null;
        }
    }
}

class TrueFalseQuestion extends Question {

    public TrueFalseQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    void showQuestion() {
        System.out.println(getQuestion());
        System.out.println("Enter 'T' for true or 'F' for false.");
    }
}

class FreeFormQuestion extends Question {

    public FreeFormQuestion(String question, String answer, int value) {
        super(question, answer, value);
    }

    @Override
    void showQuestion() {
        System.out.println(getQuestion());
    }
}

class TriviaData {
    private List<Question> questions;

    public TriviaData() {
        this.questions = new ArrayList<>();
    }

    public void addQuestion(String question, String answer, int value, QuestionType questionType) {
        this.questions.add(Question.factoryQuestion(question, answer, value, questionType));
    }

    public void showQuestion(int index) {
        Question question = this.questions.get(index);
        System.out.println("Question " + (index + 1) + ".  " + question.getValue() + " points.");
        question.showQuestion();
    }

    public int numQuestions() {
        return this.questions.size();
    }

    public Question getQuestion(int index) {
        return this.questions.get(index);
    }
}

public class TriviaGame {

    public TriviaData questions;    // Questions

    public TriviaGame() {
        // Load questions
        questions = new TriviaData();
        questions.addQuestion("The possession of more than two sets of chromosomes is termed?",
                "polyploidy", 3, QuestionType.FREEFORM);
        questions.addQuestion("Erling Kagge skiied into the north pole alone on January 7, 1993.",
                "F", 1, QuestionType.TRUEFALSE);
        questions.addQuestion("1997 British band that produced 'Tub Thumper'",
                "Chumbawumba", 2, QuestionType.FREEFORM);
        questions.addQuestion("I am the geometric figure most like a lost parrot",
                "polygon", 2, QuestionType.FREEFORM);
        questions.addQuestion("Generics were introducted to Java starting at version 5.0.",
                "T", 1, QuestionType.TRUEFALSE);
    }
    // Main game loop

    public static void main(String[] args) {
        int score = 0;            // Overall score
        int questionNum = 0;    // Which question we're asking
        TriviaGame game = new TriviaGame();
        Scanner keyboard = new Scanner(System.in);
        // Ask a question as long as we haven't asked them all
        while (questionNum < game.questions.numQuestions()) {
            // Show question
            game.questions.showQuestion(questionNum);
            // Get answer
            String answer = keyboard.nextLine();
            // Validate answer
            Question q = game.questions.getQuestion(questionNum);
            if (answer.toLowerCase().equals(q.getAnswer().toLowerCase())) {
                System.out.println("That is correct!  You get " + q.getValue() + " points.");
                score += q.getValue();
            } else {
                System.out.println("Wrong, the correct answer is " + q.getAnswer());
            }
            System.out.println("Your score is " + score);
            questionNum++;
        }
        System.out.println("Game over!  Thanks for playing!");
    }
}
