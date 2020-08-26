package SpacedRepetition.drivers;

import SpacedRepetition.model.QuizCard;
import SpacedRepetition.model.Subject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import static SpacedRepetition.drivers.Drivers.keyboard;

public class PlayMode {
    public static void practice(Subject subject) {
        ArrayList<QuizCard> quizcards = subject.getQuizCards();
        int numQuizcards = subject.getNumQuizcards();
        int gradingChoice = 0;
        String answer;
        char result;
        boolean correctness;
        QuizCard quizcard;

        if (numQuizcards == 0) {
            System.out.println("\nSorry, you have no Quiz cards in this "
                    + "subject.");
            return;
        }


        System.out.println("\nWould you like us to automatically check your "
                + "answer (must be spelled correctly), or do you want to "
                + "check the answer for us?");
        System.out.println("1. Please check my answer.\n2. I'll check it "
                + "myself.");
        System.out.println("3. Generate multiple-choice questions for me to"
                + " answer.");
        System.out.print("Enter the number of the option you want: ");
        gradingChoice = Integer.parseInt(keyboard.nextLine());

        if (gradingChoice == 3){
            if (numQuizcards < 4) {
                System.out.println("\n\nYou need at least 4 Quiz  cards in this "
                        + "subject to use this...");
                System.out.println("You only have " + numQuizcards +
                        " Quiz cards");
                System.out.println("You will be redirected to the prior menu \n");
                return;
            }else{
                generateMultChoice(subject);
            }
            return;
        }

        int quizcardIndex = 0;

        while (true) {

            if (quizcardIndex % numQuizcards == 0) {
                System.out.println("\nType QUIT instead of any answer to quit.");
            }


            quizcard = quizcards.get(quizcardIndex % numQuizcards);

            System.out.println("\n" + quizcard.getQuestion());
            System.out.print("Answer: ");
            answer = keyboard.nextLine();

            if (answer.equalsIgnoreCase("QUIT")) {
                break;
            }

            if (gradingChoice == 1) {
                boolean thisResult = quizcard.autoCheckCorrect(answer);
                if (thisResult) {
                    System.out.println("Correct!");
                } else {
                    System.out.println("Sorry, but the correct answer was: "
                            + quizcard.getAnswer());
                }
            } else {

                while (true) {
                    System.out.println("The correct answer was: "
                            + quizcard.getAnswer());
                    System.out.print("Were you correct? Enter 'C' for Correct "
                            + "or 'I' for Incorrect: ");
                    result = keyboard.nextLine().charAt(0);
                    if (result == 'C' || result == 'I' || result == 'c'
                            || result == 'i') {
                        break;
                    } else {
                        System.out.println("You must enter 'C' or 'I'.");
                    }
                }


                correctness = (result == 'C' || result == 'c');
                quizcard.userChecksAnswer(correctness);
            }

            System.out.printf("Lifetime Percent Correct = %.2f%%\n",
                    quizcard.getPercentCorrect());
            System.out.printf("Last 5 Attempts Percent Correct = %.2f%%\n",
                    quizcard.getPercentCorrectLast5());
            quizcardIndex++;
        }
    }


    public static void generateMultChoice(Subject subject){
        ArrayList<QuizCard> quizcards = subject.getQuizCards();
        int numQuizcards = subject.getNumQuizcards();
        String wrongAnswer = "wrong123";
        QuizCard quizcard;
        int quizcardIndex = 0;

        System.out.println("Type 'quit' at any point to quit.");

        while(true){
            if (quizcardIndex > numQuizcards) {
                System.out.println("You have gone through all the subject "
                        + "questions type QUIT to stop. ");
            }


            quizcard = quizcards.get(quizcardIndex % numQuizcards);

            System.out.println("\n" + quizcard.getQuestion());

            String correctAns = quizcard.getAnswer();

            Random rand = new Random();


            QuizCard randomElement1 = quizcards.get(rand.nextInt(
                    numQuizcards));
            String randans1 = randomElement1.getAnswer();
            while(true){
                if (correctAns.equals(randans1)){
                    randomElement1 = quizcards.get(rand.nextInt(
                            numQuizcards));
                    randans1 = randomElement1.getAnswer();
                }else{break;}
            }

            QuizCard randomElement2 = quizcards.get(rand.nextInt(
                    numQuizcards));
            String randans2 = randomElement2.getAnswer();
            while(true){
                if (correctAns.equals(randans1)|| correctAns.equals(randans2) ||
                        randans1.equals(randans2)){
                    randomElement2 = quizcards.get(rand.nextInt(
                            numQuizcards));
                    randans2 = randomElement2.getAnswer();

                }else{break;}
            }

            QuizCard randomElement3 = quizcards.get(rand.nextInt(
                    numQuizcards));
            String randans3 = randomElement3.getAnswer();
            while(true){
                if (correctAns.equals(randans1)|| correctAns.equals(randans2) ||
                        correctAns.equals(randans3) || randans1.equals(randans2)
                        || randans1.equals(randans3) || randans2.equals(
                        randans3)){
                    randomElement3 = quizcards.get(rand.nextInt(
                            numQuizcards));
                    randans3 = randomElement3.getAnswer();
                }else{break;}
            }


            ArrayList<String> temp = new ArrayList<>();
            temp.add(randans1);
            temp.add(randans2);
            temp.add(randans3);
            temp.add(correctAns);

            Collections.shuffle(temp);


            int count1 = 0;
            int i = 1;
            while (temp.size() > count1){
                System.out.println(i + ". " + temp.get(count1));
                count1 ++;
                i++;
            }

            System.out.println("What is your choice? ");
            String in = keyboard.nextLine();

            if (in.equalsIgnoreCase("QUIT")) {
                break;
            }

            while(!in.matches("[1-4]")){
                System.out.println("You must enter 1-4. Try again!");
                in = keyboard.nextLine();
            }

            int inputInt = 0;
            try{
                inputInt = Integer.parseInt(in);
            }catch(NumberFormatException e){
                System.out.println("You need to need to enter a number..." +
                        e);}

            if(correctAns.equals(temp.get(inputInt-1))){
                System.out.println("You are correct!");
                quizcard.autoCheckCorrect(correctAns);
            }else{
                System.out.println("Try again next time, the answer was " +
                        correctAns);
                quizcard.autoCheckCorrect(wrongAnswer);
            }

            System.out.printf("Lifetime Percent Correct = %.2f%%\n",
                    quizcard.getPercentCorrect());
            System.out.printf("Last 5 Attempts Percent Correct = %.2f%%\n",
                    quizcard.getPercentCorrectLast5());

            quizcardIndex++;
        }
    }

}
