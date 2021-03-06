
// LibertyQuest: Main program for LibertyQuest game.
// Copyright Brennon Miller
// Available under Creative Commons Attribution-ShareAlike 4.0 License

import java.io.File;
import java.io.FileNotFoundException;
import java.util.InputMismatchException;
import java.util.Scanner;

class LibertyQuest {

    public static void main(String[] args) throws Exception {
        int delay = 16;
        File currentFile = new File("intro.txt");
        Scanner fileReader = new Scanner(currentFile);

        byte strength = 0; // Player stats
        byte stealth = 0;
        byte charisma = 0;
        
        int selection = 0;
        boolean validSelection = false; // Checks that selection is valid.
        Scanner input = new Scanner(System.in);

        // Set delay value.
        if (args.length > 0) {
            try {
                delay = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                // Keep delay at 0.
            }
        }

        typeWrite(fileReader, 0); // Title Screen

        input.nextLine(); // User presses Enter.

        fileReader = switchTo("prologue1.txt", currentFile);
        typeWrite(fileReader, delay); // ...political turmoil...
        input.nextLine(); // Enter to continue

        fileReader = switchTo("prologue2.txt", currentFile);
        typeWrite(fileReader, delay); // ...Daniel Salvador...
        input.nextLine(); // Enter to continue

        // Start Gameplay!
        for (int i = 1; i <= 3; i++) {
            fileReader = switchTo(String.format("encounter%d.txt", i), currentFile);
            typeWrite(fileReader, delay);

            while (!validSelection) {
                while (!input.hasNextInt()) {
                    if (input.hasNext()) {
                        input.next();
                    } // Toss invalid value.
                    System.out.print("Please make a valid selection.\n> ");
                }
                selection = input.nextInt();
                if (selection < 4 && selection > 0) {
                    validSelection = true;
                } else {
                    System.out.print("Please make a valid selection.\n> ");
                }
                input.nextLine();   // Clears previous input.
            }
            switch (selection) {
            case 1:
                fileReader = switchTo(String.format("result/strength%d.txt", i), currentFile);
                typeWrite(fileReader, delay);
                strength++;
                break;
            case 2:
                fileReader = switchTo(String.format("result/charisma%d.txt", i), currentFile);
                typeWrite(fileReader, delay);
                charisma++;
                break;
            case 3:
                fileReader = switchTo(String.format("result/stealth%d.txt", i), currentFile);
                typeWrite(fileReader, delay);
                stealth++;
            }
            input.nextLine();   // Enter to countinue
            validSelection = false;
        }

        // Boss Fight
        fileReader = switchTo("boss/fight.txt", currentFile);
        typeWrite(fileReader, delay);
        while (!validSelection) {
            while (!input.hasNextInt()) {
                if (input.hasNext()) {
                    input.next();
                } // Toss invalid value.
                System.out.print("Please make a valid selection.\n> ");
            }
            selection = input.nextInt();
            if (selection < 4 && selection > 0) {
                validSelection = true;
            } else {
                System.out.print("Please make a valid selection.\n> ");
            }
        }
        switch (selection) {
        case 1:
            if (strength >= 2) {
                fileReader = switchTo("boss/strengthWin.txt", currentFile);
            } else {
                fileReader = switchTo("boss/strengthFail.txt", currentFile);
            }
            break;
        case 2:
            if (charisma >= 2) {
                fileReader = switchTo("boss/charismaWin.txt", currentFile);
            } else {
                fileReader = switchTo("boss/charismaFail.txt", currentFile);
            }
            break;
        case 3:
            if (stealth >= 2) {
                fileReader = switchTo("boss/stealthWin.txt", currentFile);
            } else {
                fileReader = switchTo("boss/stealthFail.txt", currentFile);
            }
        }
        typeWrite(fileReader, delay);
        input.nextLine();
        input.nextLine();           // Enter to continue.

        /* ========================================================================== */

        // When done with the program.
        System.out.println();
        System.out.println("Strength: " + strength);
        System.out.println("Charisma: " + charisma);
        System.out.println("Stealth:  " + stealth);

        System.out.print("\nPlay again for more unique results! ");
        System.out.println("You can even try a new playstyle!");

        fileReader.close();
        input.close();
    }

    /**
     * Prints text from a {@code Scanner} one letter at a time, with a variable
     * length of time between each letter printed. Will also pause for a longer
     * time at the end of sentences, and will delay when using backspace
     * characters to simulate a correction.
     * @param from {@code Scanner} from which to print text.
     * @param delay the amount of time between individual letters.
     */
    static void typeWrite(Scanner from, int delay) throws InterruptedException {
        from.useDelimiter("");
        while (from.hasNext()) {
            if (from.hasNext("(\u0008)")) {
                Thread.sleep(delay * 4);
            }
            if (from.hasNext("\\.")) {
                System.out.print(from.next());
                Thread.sleep(delay * 16);
                continue;
            }
            System.out.print(from.next());
            Thread.sleep(delay);
        }
    }

    /**
     * A helper function to switch between files more easily, and to display an
     * error if the file has been removed.
     * @param name a relative path indicating the desired file to switch to.
     * @param file the file to change.
     */
    static Scanner switchTo(String name, File file) {
        try {
            file = new File(name);
            return new Scanner(file);
        } catch (FileNotFoundException e) {
            System.out.print("ERROR: A game file is missing. Have you edited");
            System.out.println(" the folder or its files?");
            Runtime.getRuntime().exit(2);
        }
        return null;
    }
}
