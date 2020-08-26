package SpacedRepetition.drivers;
import SpacedRepetition.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Random;

import static SpacedRepetition.drivers.Drivers.keyboard;

public class Login {

    private static final Random RANDOM = new SecureRandom ( );
    private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    public static User registerOrLogin() {
        String userAccessChoice, username, password;
        User theUser = null;
        boolean loggedIn = false;


        while (!loggedIn) {
            System.out.print("\nPlease type 'register' or 'login': ");
            userAccessChoice = keyboard.next();

            if (userAccessChoice.equalsIgnoreCase("register")) {
                System.out.print("Enter your desired username: ");
                username = keyboard.next();
                System.out.print("Enter your desired password: ");
                password = keyboard.next();


                byte[] salt = getSalt();

                String encryptedPassword = generateSecurePassword(password, salt.toString());

                if (User.isDuplicateUser(username)) {
                    System.out.println("Sorry, that username is taken.");
                } else {

                    theUser = new User(username, password);
                    theUser.setSalt(salt.toString());
                    theUser.setEncryptedPassword(encryptedPassword);

                    if (theUser.saveUsernameAndPassword()) {
                        System.out.println("welcome "+ username +" Your Username & password were successfuly "
                                + "created.");
                        loggedIn = true;
                    } else {
                        System.out.println("There was a problem and we couldn't"
                                + " save your username and password.");
                    }

                }
            } else if (userAccessChoice.equalsIgnoreCase("login")) {
                System.out.print("Enter username: ");
                username = keyboard.next();
                System.out.print("Enter password: ");
                password = keyboard.next();
                theUser = User.login(username, password);

                if (theUser == null) {
                    System.out.println("Incorrect username, password "
                            + "combination.");
                } else {
                    System.out.println("You are logged in " + username +"!");
                    loggedIn = true;
                }
            } else {
                System.out.println("Please re-enter your choice.");
            }
        }

        keyboard.nextLine();
        return theUser;
    }


    public static String encrypt(String textToEncrypt) {
        byte[] salt = getSalt();
        System.out.println("hash: " + salt);
        byte[] hash = hash(textToEncrypt.toCharArray(), salt);
        String hashString = "";
        for (int i = 0; i < hash.length; i++) {
            hashString += (char) hash[i];
        }
        return hashString;
    }


    public static byte[] getSalt() {
        byte[] salt = new byte[16];
        RANDOM.nextBytes(salt);
        return salt;
    }


    public static byte[] hash(char[] password, byte[] salt) {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LENGTH);
        Arrays.fill(password, Character.MIN_VALUE);

        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            return skf.generateSecret(spec).getEncoded();
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new AssertionError("Error while hashing a password: " + e.getMessage(), e);
        } finally {
            spec.clearPassword();
        }

    }


    public static String generateSecurePassword(String password, String salt) {
        String returnValue = null;

        byte[] securePassword = hash(password.toCharArray(), salt.getBytes());

        returnValue = Base64.getEncoder().encodeToString(securePassword);

        return returnValue;
    }


    public static boolean verifyUserPassword(String providedPassword,
                                             String salt,
                                             String securedPassword) {
        boolean returnValue = false;


        String newSecurePassword = generateSecurePassword(providedPassword, salt);


        returnValue = newSecurePassword.equalsIgnoreCase(securedPassword);

        return returnValue;
    }

}

