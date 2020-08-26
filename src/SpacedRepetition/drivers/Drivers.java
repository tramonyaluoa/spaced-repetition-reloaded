package SpacedRepetition.drivers;

import SpacedRepetition.model.QuizCard;
import SpacedRepetition.model.Subject;
import SpacedRepetition.model.User;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

import static SpacedRepetition.drivers.Login.registerOrLogin;
import static SpacedRepetition.drivers.PlayMode.practice;


public class Drivers {

    static Scanner keyboard = new Scanner (System.in);
    public static void consoleInterface() {

        User theUser = registerOrLogin ( );
        int option;

        while (true) {
            System.out.println ("\nWould you like to choose a Quiz subject, read a "
                    + "new Quiz subject from a file, delete a Quiz subject, or quit?\n"
                    + "1. Choose Quiz Subject\n2. Read Quiz Subject From File \n3. Delete"
                    + " Quiz Subject\n4. Quit");
            System.out.print ("Enter the number of the option you want: ");
            option = Integer.parseInt (keyboard.nextLine ( ));

            if (option > 3 || option < 1) {
                break;
            }

            if (option == 2) {
                readSubjectFromFile (theUser);
            }

            if (option == 3) {
                deleteSubject (theUser);
            }


            if (option == 1) {
                Subject subject = chooseSubject (theUser);
                if (subject == null) {
                    break;
                }

                System.out.println ("You have chosen " + subject.getTitle ( ));


                thisSubject:
                while (true) {
                    System.out.println ("\nWould you like to add Quiz  or "
                            + "play?\n1. Add Quiz \n2. Play\n3. Delete Quiz \n4. Reset Score\n5. Return"
                            + " to previous menu");
                    System.out.print ("Enter the number of the option you want: ");
                    option = Integer.parseInt (keyboard.nextLine ( ));

                    switch (option) {
                        case 1:
                            addQuizCards (subject);
                            break;
                        case 2:
                            practice (subject);
                            break;
                        case 3:
                            removeQuizCard (subject);
                            System.out.println ("Quiz  card(s) removed!");
                            break;
                        case 4:
                            subject.resetQuizcards ( );
                            System.out.println ("The scores for this subject have been reset!");
                            break;
                        default:
                            break thisSubject;
                    }
                }
            }

        }

        theUser.saveUserState ( );
        System.out.println ("\nAll of your work has been saved. See you next "
                + "time!");
    }


    public static void addQuizCards(Subject subject) {
        String question, answer;


        while (true) {
            System.out.print ("\nEnter quizcard question or 'QUIT' to quit: ");
            question = keyboard.nextLine ( );
            if (question.equalsIgnoreCase ("QUIT")) {
                return;
            } else {
                System.out.print ("Enter quizcard answer: ");
                answer = keyboard.nextLine ( );
                subject.addQuizCard (question, answer);
            }
        }
    }

    public static void removeQuizCard(Subject subject) {
        ArrayList<QuizCard> quizcards = subject.getQuizCards ( );
        int numQuizcards = subject.getNumQuizcards ( );
        String answer;
        QuizCard quizcard;


        while (numQuizcards > 0) {

            for (int i = 0; i < numQuizcards; i++) {
                quizcard = quizcards.get (i);

                System.out.println ("\n" + quizcard.getQuestion ( ));
                System.out.print ("Do you want to delete this quiz card? 'Y' or 'N' ");
                answer = keyboard.nextLine ( );

                if (answer.equalsIgnoreCase ("Y")) {
                    subject.removeQuizcard (quizcard);
                    quizcards.remove (quizcard);
                    numQuizcards = subject.getNumQuizcards ( );
                    i--;
                }
            }
            System.out.println ("Type 'QUIT' to quit or 'again' to keep deleting. ");
            answer = keyboard.nextLine ( );
            if (answer.equalsIgnoreCase ("QUIT")) {
                break;
            }
        }
    }

    public static Subject chooseSubject(User theUser) {
        int subjectChoice;
        String subjectTitle;

        System.out.println ("\n1. Create New Subject");
        ArrayList<Subject> subjects = theUser.getSubjects ( );


        int index;
        for (index = 0; index < theUser.getNumSubjects ( ); index++) {
            System.out.println ((index + 2) + ". "
                    + subjects.get (index).getTitle ( ));
        }

        System.out.println ((index + 2) + ". Quit");
        System.out.print ("Enter the number of the option you want: ");

        subjectChoice = Integer.parseInt (keyboard.nextLine ( )) - 2;

        if (subjectChoice == -1) {
            System.out.print ("Enter subject title: ");
            subjectTitle = keyboard.nextLine ( );
            Subject newSubject = new Subject (subjectTitle);
            theUser.addSubject (newSubject);
            return newSubject;
        } else if (subjectChoice < index && subjectChoice > -1) {
            return subjects.get (subjectChoice);
        } else {
            return null;
        }
    }

    public static void deleteSubject(User theUser) {
        int subjectChoice;
        ArrayList<Subject> subjects = theUser.getSubjects ( );


        int index;
        for (index = 0; index < theUser.getNumSubjects ( ); index++) {
            System.out.println ((index + 1) + ". "
                    + subjects.get (index).getTitle ( ));
        }

        System.out.print ("Enter the subject number you want to delete or type '0' to quit ");

        subjectChoice = Integer.parseInt (keyboard.nextLine ( )) - 1;

        if (subjectChoice < index && subjectChoice > -1) {
            System.out.println (subjects.get (subjectChoice).getTitle ( ) + " has been deleted!");
            theUser.deleteSubject (subjects.get (subjectChoice));


        }
    }

    public static void readSubjectFromFile(User theUser) {
        JFileChooser chooser = new JFileChooser (System.getProperty ("user.dir")
                + "/subjectFiles");
        System.out.println ("\nYou may need to minimize the window to see the "
                + "file chooser.");
        chooser.setFileFilter (new FileNameExtensionFilter ("Text files", "txt"));

        int result = chooser.showOpenDialog (null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = chooser.getSelectedFile ( );
            Subject subject = Subject.createSubjectFromFile (selectedFile);
            theUser.addSubject (subject);
            String fileName = selectedFile.getName ( );
            System.out.println ("Subject: \"" + subject.getTitle ( ) + "\" "
                    + "created from " + fileName);
        } else {
            System.out.println ("You cancelled the file dialog");
        }
    }
}



