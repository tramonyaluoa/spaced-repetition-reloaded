package SpacedRepetition.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static SpacedRepetition.drivers.Login.verifyUserPassword;

public class User {

    private String username, password;
    private String salt;
    private String encryptedPassword;
    private ArrayList<Subject> subjects;
    private int numSubjects;


    public User(String username, String password) {
        this.username = username;
        this.password = password;
        subjects = new ArrayList<>();
    }


    public ArrayList<Subject> getSubjects() {
        return subjects;
    }


    public void addSubject(Subject subject) {
        subjects.add(subject);
        numSubjects++;
    }

    public void deleteSubject(Subject subject) {
        subjects.remove(subject);
        numSubjects--;
    }

    public String getUsername() {
        return username;
    }

    public int getNumSubjects() {
        return numSubjects;
    }


    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }
    

    public String getEncryptedPassword() {
        return this.encryptedPassword;
    }
    

    public void setSalt(String salt) {
        this.salt = salt;
    }


    public String getSalt() {
        return this.salt;
    }

    public void saveUserState() {

        new File(System.getProperty("user.dir") + "/userStates").mkdirs();

        String filename = System.getProperty("user.dir") + "/userStates/"
                + username + ".txt";
        PrintWriter output;
        ArrayList<QuizCard> quizcards;

        try {
            output = new PrintWriter(filename);


            for (Subject subject : subjects) {
                output.println("Subject: " + subject.getTitle());


                quizcards = subject.getQuizCards();
                for (QuizCard quizcard : quizcards) {
                    output.print(quizcard);
                }
            }

            output.close();

        } catch (FileNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void loadUserState() {
        String filename = System.getProperty("user.dir") + "/userStates/" 
                + username + ".txt";
        File userState = new File(filename);
        Scanner input;
        String inputLine;
        QuizCard quizcard;

        try {
            input = new Scanner(userState);


            while (input.hasNextLine()) {
                inputLine = input.nextLine();
                if (inputLine.startsWith("Subject: ")) {

                    inputLine = inputLine.substring("Subject: ".length());
                    addSubject(new Subject(inputLine));
                } else {

                    quizcard = QuizCard.fromString(inputLine);
                    subjects.get(numSubjects - 1).addQuizCard(quizcard);
                }
            }
        } catch (FileNotFoundException ex) {
            try {
                userState.createNewFile();
            } catch (IOException ex1) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null,
                        ex1);
            }
        }
    }

    public static boolean isDuplicateUser(String potentialUsername) {
        File userList = new File("users.txt");
        Scanner input;
        String usernameAndPassword, usernameOnly;

        if (!userList.exists()) {
            return false;
        } else {
            try {
                input = new Scanner(userList);
                while (input.hasNextLine()) {
                    usernameAndPassword = input.nextLine();
                    usernameOnly = usernameAndPassword.split(" ")[0];
                    if (potentialUsername.equals(usernameOnly)) {
                        return true;
                    }
                }
            } catch (FileNotFoundException ex) {
                Logger.getLogger(User.class.getName()).log(Level.SEVERE, null,
                        ex);
            }
        }

        return false;
    }


    public boolean saveUsernameAndPassword() {
        File userList = new File("users.txt");
        PrintWriter output;
        String oldFileContents = "";


        if (userList.exists()) {
            oldFileContents = getOldFileContents(userList);
        }

        try {
            output = new PrintWriter(userList);
            output.print(oldFileContents);
            output.println(username + " " + salt + " " + encryptedPassword);

            output.close();
            return true;
        } catch (FileNotFoundException ex) {
            return false;
        }
    }


    public static User login(String typedUsername, String typedPassword) {
        File userList = new File("users.txt");
        String[] usernamePasswordEntry;
        User theUser;

        try {
            Scanner input = new Scanner(userList);

            while (input.hasNextLine()) {
                usernamePasswordEntry = input.nextLine().split(" ");
                if (typedUsername.equals(usernamePasswordEntry[0])) {
                    
                    if (verifyUserPassword(typedPassword, 
                                           usernamePasswordEntry[1], 
                                           usernamePasswordEntry[2])) {
                        theUser = new User(typedUsername, typedPassword);
                        theUser.loadUserState();
                        return theUser;
                    }
                           

                }
            }

            return null;
        } catch (FileNotFoundException ex) {
            return null;
        }
    }

    public static String getOldFileContents(File file) {
        Scanner input;
        String oldFileContents = "";

        try {
            input = new Scanner(file);

            while (input.hasNextLine()) {
                oldFileContents += input.nextLine() + "\n";
            }

            input.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(User.class.getName()).log(Level.SEVERE, null, ex);
        }

        return oldFileContents;
    }
    
}
