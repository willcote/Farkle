import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

/*
user interaction:

tell the game which dice to keep
- if 3 of a kind, just type the number once
- if 3 or more of 1's or 5's, type exactly how many of those you want to keep (scoring changes)
 */

public class FarkleView {
    private Scanner reader;
    private ArrayList<Integer> diceValuesToScore;
    private ArrayList<Integer> verifiedValues;
//    private ArrayList<Integer> scoreDice;

    // purpose: constructor for FarkleView object
    // pre-conditions: want to create FarkleView object
    // post-conditions: creates FarkleView object and initializes the reader
    public FarkleView() {
        reader = new Scanner(System.in);
    }

    // purpose: ask the player if they want to end the game before the win condition is met
    // pre-conditions: player has just ended their turn
    // post-conditions: asks the player if they want to end the game
    //          - if yes, ends the game
    //          - if no, continues play
    public boolean endGamePrompt() {
        reader = new Scanner(System.in);
        String response = "";

        //validation loop
        while (true) {
            System.out.print("Would you like to end the game now? (y/n): ");
            response = reader.nextLine();

            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            }
        }
    }

    // purpose: ask the player if they want to restart the game
    // pre-conditions: the game has ended
    // post-conditions: asks the player if they want to restart the game
    //          - if yes, restarts the game
    //          - if no, exits the program
    public boolean restartGamePrompt() {
        reader = new Scanner(System.in);
        String response = "";

        //validation loop
        while (true) {
            System.out.print("would you like to restart the game? (y/n): ");
            response = reader.nextLine();

            if (response.equals("y")) {
//                System.out.println("input received");
                return true;
            } else if (response.equals("n")) {
                return false;
            }
        }
    }

    // purpose: compares values picked by user to values available in scoring options
    // pre-conditions: user input has passed first part of verifyDicePrompt;
    //                  contains all numbers and has less values than the number of dice rolled
    // post-conditions: returns whether the numbers exist in the scoring options
    //                  - if they do: adds them to an ArrayList<Integer>
    private boolean compareResponseValues(ArrayList<Integer> diceRolls) {
        verifiedValues = new ArrayList<Integer>();
        for (Integer i : diceValuesToScore) {
            if (diceRolls.contains(i)) {
                if (i != 1 && i != 5) {
                    verifiedValues.add(i);
                    verifiedValues.add(i);
                    verifiedValues.add(i);
                }
                else
                    verifiedValues.add(i);
            } else
                return false;
        }
        return true;
    }

    // purpose: validates user input when choosing dice to score
    // pre-conditions: user has made some input response to choose dice
    // post-conditions: returns whether the response is valid
    private boolean verifyDicePrompt(String response, ArrayList<Integer> diceRolls) {
        String[] responseArray = response.split("");
        diceValuesToScore = new ArrayList<Integer>();

        for (String s : responseArray) {
            try {
                Integer x = Integer.parseInt(s);
                diceValuesToScore.add(x);
            } catch (Exception e) {
                return false;
            }
        }
        if (diceValuesToScore.size() <= diceRolls.size() && diceValuesToScore.size() > 0)
            return true;
        else
            return false;
    }

    // purpose: asks the player what dice they want to score
    // pre-conditions: player has rolled dice and there are scoring options
    // post-conditions: player has picked which dice they want to score for this roll
    public ArrayList<Integer> chooseDicePrompt(ArrayList<Integer> diceRolls) {
        reader = new Scanner(System.in);
        String response = "";

        //validation loop
        boolean validResponse = false;
        while (!validResponse) {
            System.out.print("which dice would you like to score? ");
            response = reader.nextLine();

            //validation method
            if (verifyDicePrompt(response, diceRolls)) {
//                System.out.println(diceValuesToScore);
                validResponse = compareResponseValues(diceRolls);
            }

        }
        return verifiedValues;
    }

    // purpose: asks user whether they want to continue rolling this turn
    // pre-conditions: user has rolled and picked which dice to score
    // post-conditions:
    //      - returns true if user wants to roll again
    //      - returns false if user wants to end their turn
    public boolean continueTurnPrompt() {
        reader = new Scanner(System.in);
        String response = "";
        while (true) {
            System.out.print("would you like to continue your turn? (y/n): ");
            response = reader.nextLine();

            if (response.equals("y")) {
                return true;
            } else if (response.equals("n")) {
                return false;
            }
        }
    }

    // purpose: shows the user which dice they picked (feedback)
    // pre-conditions: user has picked which dice they want to score
    // post-conditions: outputs a message showing the user which dice they are scoring
    public void showDicePicked(SixDice dice, FarklePlayer p)
    {
        System.out.println(p.getName() + " scored these dice: " + dice.toString(dice.getRolls()));
    }


    // purpose: prints a message when the user has ended the game early
    // pre-conditions: the user has opted to end the game early (when prompted)
    // post-conditions: outputs a message with game status data at the time the game ends
    public void endEarly(String name, int score, int winScore)
    {
        System.out.println(name + " ended the game early with " + score + "/" + winScore + "pts.");
        System.out.println();
        //        return restartGamePrompt();
    }

    // purpose: prints win message
    // pre-conditions: someone has won the game by reaching the score requirement
    // post-conditions: prints a message saying who won the game
    public void gameWin(FarklePlayer p)
    {
        System.out.println(p.getName() + " wins!!!");
    }

    public void roll(String name, int turnScore, int score, SixDice dice /*, ArrayList<FarkleDie> rolls*/)
    {
        System.out.println(name + " has rolled: " + dice.toString(dice.getRolls()));
    }

    // purpose: outputs a message when a player's turn has ended entirely
    // pre-conditions: some player's turn has ended; they have decided to end their turn
    // post-conditions:
    //      - prints a message displaying the score earned this turn
    //      - prints a message displaying the total score the player currently has
    public void turn(FarklePlayer p)
    {
        String name = p.getName();
        int turnScore = p.getTurnScore();
        int score = p.getScore();

        System.out.println(name + " earned: " + turnScore + "pts this turn");
        System.out.println(name + " has: " + score + "pts overall");

        System.out.println();   // helps seperate player and cpu turns in display
    }

    // purpose: prints a message when a player has farkled
    // pre-conditions: a player has just farkled
    // post-conditions: prints a message when a player has farkled and lost thier points for the round
    public void farkled(FarklePlayer p)
    {
        System.out.println(p.getName() + " has FARKLED and receives 0 points this turn.");
        System.out.println();
    }

    // purpose: prints a message when a player has scored all of their dice
    // pre-conditions: a player has just scored all of their dice
    // post-conditions: prints a message when a player has scored all of their dice
    public void hotDice()
    {
        System.out.println("All dice have been scored!");
    }

    // purpose:
    // pre-conditions:
    // post-conditions:
    public void possibleScoringOptions(SixDice scoringDice)
    {
        String dice = scoringDice.toString(scoringDice.getRolls());
        System.out.println("scoring options: " + dice);
    }

    // purpose: prints a tutorial for choosing dice to score
    // pre-conditions: game has just started
    // post-conditions: prints a tutorial for choosing dice to score
    public void howTo()
    {
        System.out.println(":TUTORIAL:");
        System.out.println("\tHow to choose which dice to score:");
        System.out.println("\ttype a combination of numbers from the 'scoring options' preview (ex: 1125)");
        System.out.println("\tfor 1's and 5's, type the exact amount you want to keep (ex: 11555)");
        System.out.println("\tfor other numbers, type just once to score three (ex: 2 -> scores 222)");
        System.out.println(":HAVE FUN:");
        System.out.println();

    }



    public ArrayList<String> namePrompt()
    {
        ArrayList<String> names = new ArrayList<String>();
        reader = new Scanner(System.in);

        String playerName = playerNamePrompt();
        String cpuName = cpuNamePrompt();

        names.add(playerName);
        names.add(cpuName);

        return names;
    }

    private String playerNamePrompt()
    {
        String response = "";
        while (true) {
            System.out.print("what is your name? (letters only): ");
            response = reader.nextLine();

            if (response.matches("[a-zA-Z]+"))
            {
                break;
            }
        }
        return response;
    }

    private String cpuNamePrompt()
    {
        String response = "";
        while (true) {
            System.out.print("what is your rival's name? (letters only): ");
            response = reader.nextLine();

            if (response.matches("[a-zA-Z]+"))
            {
                break;
            }
        }
        return response;
    }



}