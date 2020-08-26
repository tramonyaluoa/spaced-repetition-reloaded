package SpacedRepetition.model;


public class QuizCard {

    private static final String DELIM = " @DL ";
    private static final int QUESTION = 0, ANSWER = 1, NUM_ATTEMPTS = 2,
            NUM_CORRECT = 3, NUM_INCORRECT = 4, LAST_5_ATTEMPTS = 5;

    private String question, answer;
    private int numAttempts, numCorrect, numIncorrect;
    private int[] last5Attempts; // "1" for correct, "0" for incorrect


    public QuizCard(String question, String answer) {
        this.question = question;
        this.answer = answer;
        numAttempts = numCorrect = numIncorrect = 0;
        last5Attempts = new int[5];
    }


    public String getQuestion() {
        return question;
    }


    public String getAnswer() {
        return answer;
    }


    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }


    public int getNumAttempts() {
        return numAttempts;
    }


    public int getNumCorrect() {
        return numCorrect;
    }


    public int getNumIncorrect() {
        return numIncorrect;
    }


    public void setNumAttempts(int numAttempts) {
        this.numAttempts = numAttempts;
    }


    public void setNumCorrect(int numCorrect) {
        this.numCorrect = numCorrect;
    }


    public void setNumIncorrect(int numIncorrect) {
        this.numIncorrect = numIncorrect;
    }


    public int[] getLast5Attempts() {
        return last5Attempts;
    }


    public void setLast5Attempts(int[] last5) {
        last5Attempts = last5;
    }


    public boolean autoCheckCorrect(String attempt) {
        boolean correct;
        if (answer.equalsIgnoreCase(attempt)) {
            numCorrect++;
            last5Attempts[numAttempts % 5] = 1;
            correct = true;
        } else {
            numIncorrect++;
            last5Attempts[numAttempts % 5] = 0;
            correct = false;
        }

        numAttempts++;
        return correct;
    }

    public void userChecksAnswer(boolean correct) {
        if (correct) {
            numCorrect++;
            last5Attempts[numAttempts % 5] = 1;
        } else {
            numIncorrect++;
            last5Attempts[numAttempts % 5] = 0;
        }

        numAttempts++;
    }
    public double getPercentCorrect() {
        if (numAttempts == 0) {
            return 0;
        }

        return (numCorrect * 1.0 / numAttempts) * 100;
    }


    public double getPercentCorrectLast5() {
        if (numAttempts == 0) {
            return 0;
        }

        int sum = 0;
        for (int i = 0; i < 5 && i < numAttempts; i++) {
            sum += last5Attempts[i];
        }

        int divisor = Math.min(numAttempts, 5); // user may not have 5 attempts
        return (sum * 1.0 / divisor) * 100;
    }


    public void resetScore() {
        numAttempts = 0;
        numCorrect = 0;
        numIncorrect = 0;
        last5Attempts = new int[5];
    }


    @Override
    public String toString() {
        StringBuilder quizcardString = new StringBuilder(question + DELIM
                + answer + DELIM + numAttempts + DELIM + numCorrect + DELIM
                + numIncorrect + DELIM);

        for (int i = 0; i < 5; i++) {
            quizcardString.append(last5Attempts[i]).append(" ");
        }

        quizcardString.append("\n");
        return quizcardString.toString();
    }


    public static QuizCard fromString(String quizcardString) {
        String[] data = quizcardString.split(DELIM);
        QuizCard quizcard = new QuizCard (data[QUESTION], data[ANSWER]);
        quizcard.setNumAttempts(Integer.parseInt(data[NUM_ATTEMPTS]));
        quizcard.setNumCorrect(Integer.parseInt(data[NUM_CORRECT]));
        quizcard.setNumIncorrect(Integer.parseInt(data[NUM_INCORRECT]));

        String[] last5 = data[LAST_5_ATTEMPTS].split(" ");
        int[] last5Atmpts = new int[5];
        for (int i = 0; i < 5; i++) {
            last5Atmpts[i] = Integer.parseInt(last5[i]);
        }
        quizcard.setLast5Attempts(last5Atmpts);

        return quizcard;
    }
}
