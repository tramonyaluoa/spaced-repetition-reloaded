package SpacedRepetition.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;


public class Subject {

    private String title;
    private ArrayList<QuizCard> quizcards;
    private int numQuizcards;


    public Subject(String title) {
        this.title = title;
        quizcards = new ArrayList<>();
        numQuizcards = 0;
    }


    public String getTitle() {
        return title;
    }


    public ArrayList<QuizCard> getQuizCards() {
        return quizcards;
    }


    public void addQuizCard(String question, String answer) {
        quizcards.add(new QuizCard (question, answer));
        numQuizcards++;
    }


    public void addQuizCard(QuizCard quizcard) {
        quizcards.add(quizcard);
        numQuizcards++;
    }

    public int getNumQuizcards() {
        return numQuizcards;
    }


    public void resetQuizcards() {
        for (QuizCard card : quizcards) {
            card.resetScore();
        }
    }

    public void removeQuizcard(QuizCard card) {
        quizcards.remove(card);
        numQuizcards--;
    }

    public void shuffleQuizcards() {
        Collections.shuffle(quizcards);
    }

    public static Subject createSubjectFromFile(File subjectFile) {
        String[] questionAnswer;
        Subject subject;
        String title;

        try {
            Scanner input = new Scanner(subjectFile);
            title = input.nextLine();
            subject = new Subject(title);

            while (input.hasNextLine()) {
                questionAnswer = input.nextLine().split("@DL");
                subject.addQuizCard(questionAnswer[0].trim(),
                        questionAnswer[1].trim());
            }

            return subject;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }
}
