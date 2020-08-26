package SpacedRepetition;

import SpacedRepetition.drivers.Drivers;

import java.util.Scanner;


public class Main {




    public static void main(String[] args) {

        Scanner reader = new Scanner(System.in);
        System.out.println ("Enter 1 to play");
        String input = reader.next();
        if (input.equals("1")) {
            Drivers.consoleInterface ();
            System.exit(0);
        }

    }

}
