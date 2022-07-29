import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Set;

//controller class
public class FarkleGame
{
    private FarklePlayer player;
    private FarklePlayer cpu;
    private FarkleView view;
    private FarkleChangeXML xml;

    private int winScore;
//    private final int DIE = 6;
    private final int STARTING_ROLLS = 6;

    private SixDice dice;
    private ArrayList<FarkleDie> rolls;
    private Hashtable<Integer, Integer> count;

    // purpose: constructor for FarkleGame object
    // pre-conditions: game has not yet started
    // post-conditions: winScore is set to a default value of 10000; player and view objects created
    public FarkleGame()
    {
        player = new FarklePlayer();
        cpu = new FarkleCPU("CPU");
        view = new FarkleView();
        xml = new FarkleChangeXML();

        winScore = 10000;
    }

    // purpose: Plays through the FarkleGame
    // pre-conditions: game has not yet started
    // post-conditions: all games have completed
    public void run()
    {
        boolean restart = true;     // tracks whether the user wants to restart the game
        xml.initXML();

        // asks the player to input names
        ArrayList<String> names = view.namePrompt();
        player.setName(names.get(0));
        cpu.setName(names.get(1));

        // sets the names in xml
        xml.setName("human", names.get(0));  // here so it doesn't ask for names every restart
        xml.setName("cpu", names.get(1));  // here so it doesn't ask for names every restart

        view.howTo();   // here so it only plays the tutorial once

        //outer loop is for restarts
        while (restart)
        {
            // used to keep track of win status in the middle of game loop
            // initialized here to catch restarts after wins
            boolean gameWin = false;
            // gameLose is true if the cpu wins
            boolean gameLose = false;

            //initializes game values
            init();

            //inner loop for individual games
            while (!player.getEndGame() && player.getScore() < winScore && cpu.getScore() < winScore) {
                playerTurn();
                cpuTurn();

                // checks if the player has won before
                // prompting to end the game
                if (player.getScore() >= winScore)
                {
                    gameWin = true;
                    break;
                }
                else
                {
                    player.setEndGame(view.endGamePrompt());
                }

                // checks if cpu has won the game (does this after because player goes first)
                if (cpu.getScore() >= winScore)
                {
                    gameLose = true;
                    break;
                }
                // no else, only player can end the game early
            }

            //printing ending messages and restart prompt accordingly
            if (gameWin) // when the player wins
            {
                // ************* CHECK THIS (note for inc 6) **********
                // going to have to change this "player" input variable when implementing the CPU,
                // - as it is now, only the human player can be displayed here
                view.gameWin(player);
                restart = view.restartGamePrompt();
            }
            else if (gameLose) // when the cpu wins
            {
                view.gameWin(cpu);
                restart = view.restartGamePrompt();
            }
            else // when the player has chosen to end the game early
            {
                view.endEarly(player.getName(), player.getScore(), winScore);
                restart = view.restartGamePrompt();
            }

        }
    }

    // purpose: initialize a game state with default starting values
    // pre-conditions: all relevant game values are at some value
    // post-conditions: all relevant game values are changed to a starting position
    private void init()
    {
        player.setScore(0);
        player.setTurnScore(0);
        player.setEndGame(false);

        cpu.setScore(0);
        cpu.setTurnScore(0);
    }

    // purpose: carries out the player turn
    // pre-conditions: game has been started; it is the player's turn
    // post-conditions: players turn has ended entirely
    private void playerTurn()
    {
        boolean farkled = false;

        dice = new SixDice();
        FarkleDie die = new FarkleDie();
        int numRolls = STARTING_ROLLS;

        player.setTurnScore(0);

        SixDice diceToScore = new SixDice();
        SixDice diceToKeep = new SixDice();

        // turn loop for repeated rolls
        while (!farkled)
        {
            // main action of the turn, rolling dice
            rolls = dice.rollDice(numRolls);
            dice.setRolls(rolls);

            // this variable is used to store values of the user's choice
            // it's initialized here using the full set of rolls to be used in maxTurnScore
            ArrayList<Integer> scoreDiceValues = rollsToIntegers(dice.getRolls());


            // necessary to do scoring simulations
            count = countRollValues(dice.getRolls(), die.getSides());

            view.roll(player.getName(), player.getTurnScore(), player.getScore(), dice);

            // finds which dice can score
            SixDice scoringDice = findScoringDice(dice.getRolls(), die);
//            System.out.println(scoringDice.toString(scoringDice.getRolls()));


            // checking if player has farkled
            if (maxTurnScore(integersToRolls(scoreDiceValues), die) > 0)
            {

                view.possibleScoringOptions(scoringDice);

                // ask the user what dice they want to score for this roll
                scoreDiceValues = view.chooseDicePrompt(rollsToIntegers(scoringDice.getRolls()));
//                view.possibleScoringOptions(scoringDice);
//                System.out.println(scoringDice.toString(scoringDice.getRolls()));
//                scoreDiceValues = view.chooseDicePrompt(rollsToIntegers(dice.getRolls()));

                SixDice dicePicked = new SixDice();
                dicePicked.setRolls(integersToRolls(scoreDiceValues));

                view.showDicePicked(dicePicked, player);

                numRolls = dice.getRolls().size() - scoreDiceValues.size();



                calculateRollScore(player, integersToRolls(scoreDiceValues), die);



                //if there are any dice to roll next turn
                if (dice.getRolls().size() - scoreDiceValues.size() > 0)
                {
                    if (!view.continueTurnPrompt()) {
                        break;
                    }
                }
                else if (dice.getRolls().size() - dicePicked.getRolls().size() == 0)
                {
                    view.hotDice();
                    numRolls = STARTING_ROLLS;
                    if (!view.continueTurnPrompt()) {
                        break;
                    }
                }
                else
                {
//                    view.turn(player);
                    break;
                }


            }
            else // case: farkled
            {
                player.setTurnScore(0);
                view.farkled(player);
                farkled = true;

                //add turn result to xml doc
                // equivalent params to "human", 0, player.getScore())
                xml.addTurn("human", player.getTurnScore(), player.getScore());
            }
        }

        if (!farkled)
        {
            player.setScore(player.getScore() + player.getTurnScore());
            view.turn(player);

            //add turn result to xml doc
            xml.addTurn("human", player.getTurnScore(), player.getScore());
        }
    }

    // purpose: carries out the cpu turn
    // pre-conditions: game has been started; it is the cpu's turn
    // post-conditions: cpu turn has ended entirely
    private void cpuTurn()
    {
//        System.out.println("cpu turn start");

        boolean farkled = false;

        dice = new SixDice();
        FarkleDie die = new FarkleDie();
        int numRolls = STARTING_ROLLS;

        cpu.setTurnScore(0);

        SixDice diceToScore = new SixDice();
        SixDice diceToKeep = new SixDice();


        while (!farkled)
        {
            // main action of the turn, rolling dice
            rolls = dice.rollDice(numRolls);
            dice.setRolls(rolls);

            // this variable is used to store values of the user's choice
            // it's initialized here using the full set of rolls to be used in maxTurnScore
            ArrayList<Integer> scoreDiceValues = rollsToIntegers(dice.getRolls());


            // necessary to do scoring simulations
            count = countRollValues(dice.getRolls(), die.getSides());

            view.roll(cpu.getName(), cpu.getTurnScore(), cpu.getScore(), dice);

            // finds which dice can score
            SixDice scoringDice = findScoringDice(dice.getRolls(), die);
//            System.out.println(scoringDice.toString(scoringDice.getRolls()));


            // checking if player has farkled
            if (maxTurnScore(integersToRolls(scoreDiceValues), die) > 0)
            {
                /* this is where the cpu turn differs from the player turn */
                //chooses all scorable dice to be scored for this roll
                scoreDiceValues = rollsToIntegers(scoringDice.getRolls());

                SixDice dicePicked = new SixDice();
                dicePicked.setRolls(integersToRolls(scoreDiceValues));

                view.showDicePicked(dicePicked, cpu);

                numRolls = dice.getRolls().size() - scoreDiceValues.size();

                calculateRollScore(cpu, integersToRolls(scoreDiceValues), die);

                // if winScore is reached, it will stop turn to win
                if (cpu.getScore() >= winScore)
                    break;

                // hot dice
                if (dice.getRolls().size() - dicePicked.getRolls().size() == 0)
                {
                    view.hotDice();
                    numRolls = STARTING_ROLLS;
                    // always re-rolls on hot dice unless winScore is reached
                }
                else if (dice.getRolls().size() - scoreDiceValues.size() < 3)
                {
                    break;
                }


            }
            else
            {
                cpu.setTurnScore(0);
                view.farkled(cpu);
                farkled = true;

                xml.addTurn("cpu", cpu.getTurnScore(), cpu.getScore());
            }
        }

        if (!farkled)
        {
            cpu.setScore(cpu.getScore() + cpu.getTurnScore());
            view.turn(cpu);

            xml.addTurn("cpu", cpu.getTurnScore(), cpu.getScore());
        }
    }

    // purpose: changes an ArrayList<FarkleDie> into an ArrayList<Integer>
    // pre-conditions: input is an ArrayList<FarkleDie>
    // post-conditions: gives an ArrayList<Integer>
    private ArrayList<Integer> rollsToIntegers(ArrayList<FarkleDie> rolls)
    {
        ArrayList<Integer> rollInts = new ArrayList<Integer>();
        for (FarkleDie d : rolls)
        {
            rollInts.add(d.getValue());
        }
        return rollInts;
    }

    // purpose: changes an ArrayList<Integer> into an ArrayList<FarkleDie>
    // pre-conditions: input is an ArrayList<Integer>
    // post-conditions: gives an ArrayList<FarkleDie>
    private ArrayList<FarkleDie> integersToRolls(ArrayList<Integer> rolls)
    {
        ArrayList<FarkleDie> rollDice = new ArrayList<FarkleDie>();
        for (Integer i : rolls)
        {
            FarkleDie d = new FarkleDie(i);
            rollDice.add(d);
        }
        return rollDice;
    }

    // purpose: finds which dice in a roll can be scored
    // pre-conditions: some player has rolled
    // post-conditions: returns the dice which can be scored
    private SixDice findScoringDice(ArrayList<FarkleDie> dice, FarkleDie die)
    {
        SixDice scoringDice = new SixDice();

        Hashtable<Integer, Integer> countScoring = new Hashtable<Integer, Integer>();
        countScoring = countRollValues(dice, die.getSides());

        ArrayList<Integer> scoringRolls = new ArrayList<Integer>();
        ArrayList<Integer> triples = findAndRemoveTriples(countScoring);

        // case of not 1 or 5
        if (!triples.contains(1) && !triples.contains(5)) {
            for (Integer triple : triples)
                scoringRolls.add(triple);
        }
        else
        {
            for (Integer triple : triples)
            {
                scoringRolls.add(triple);
                scoringRolls.add(triple);
                scoringRolls.add(triple);
            }
        }

        for (int i = 0; i < countScoring.get(1); i++)
            scoringRolls.add(1);

        for (int i = 0; i < countScoring.get(5); i++)
            scoringRolls.add(5);

        scoringDice.setRolls(integersToRolls(scoringRolls));

        return scoringDice;
    }

    // purpose: calculates the maximum score a given roll can provide
    // pre-conditions: some player has rolled
    // post-conditions: returns the max score a roll can provide
    private int maxTurnScore(ArrayList<FarkleDie> rolls, FarkleDie d)
    {
        count = countRollValues(rolls, d.getSides());
        int maxTurnScore = 0;

        maxTurnScore += scoreSingles(count);
        maxTurnScore += scoreTriples(count);

        return maxTurnScore;
    }

    // purpose: calculates roll score and updates player's turn score
    // pre-conditions: player has picked which dice they want to score
    // post-conditions: calculates roll score and updates player.turnScore
    private int calculateRollScore(FarklePlayer p, ArrayList<FarkleDie> rolls, FarkleDie d)
    {
        count = countRollValues(rolls, d.getSides());

        p.setTurnScore(p.getTurnScore() + scoreTriples(count));
        p.setTurnScore(p.getTurnScore() + scoreSingles(count));

        return p.getTurnScore();
    }


    // purpose: reorder list to be usable in scoring logic
    // pre-conditions: a player has rolled + there is a ArrayList<FarkleDie> of the rolls
    // post-conditions: creates a Hashtable<Integer, Integer> of roll values
    private Hashtable<Integer,Integer> countRollValues(ArrayList<FarkleDie> rolls, int sides)
    {
        count = new Hashtable<Integer, Integer>();
        for (int i = 1; i <= sides; i++)
            count.put(i,0);

        //counting number of times each number is rolled
        //**increment 4 update: uses FarkleDie.getValue() to keep using Hashtable with Int for count
        for (FarkleDie i:rolls)
        {
            count.put(i.getValue(), count.get(i.getValue())+1);
        }

        return count;
    }

    // purpose: finds any triples, a scoring mechanic in the game
    // pre-conditions: there is a Hashtable<Integer, Integer> created by countRollValues
    // post-conditions: returns an ArrayList<Integer> of any triples
    private ArrayList<Integer> findTriples(Hashtable<Integer, Integer> count)
    {
        ArrayList<Integer> triples = new ArrayList<Integer>();

        Set<Integer> countSet = count.keySet(); // makes a set of just key values of count for iteration
        for (Integer key : count.keySet())
        {
            if (count.get(key) == 6)
            {
                triples.add(key);
                triples.add(key);
            }
            else if (count.get(key) >= 3)
            {
                triples.add(key);
            }
        }
        return triples;
    }

    // purpose: updating count while finding triples (specific use for 1's and 5's)
    // pre-conditions: there is a Hashtable<Integer, Integer> created by countRollValues
    // post-conditions: same as findTriples except it also removes them from data structures
    private ArrayList<Integer> findAndRemoveTriples(Hashtable<Integer, Integer> count)
    {
        ArrayList<Integer> triples = new ArrayList<Integer>();

        Set<Integer> countSet = count.keySet(); // makes a set of just key values of count for iteration
        for (Integer key : count.keySet())
        {
            if (count.get(key) == 6)
            {
                triples.add(key);
                triples.add(key);

                count.put(key, count.get(key)-6);
            }
            else if (count.get(key) >= 3)
            {
                triples.add(key);

                count.put(key, count.get(key)-3);
            }
        }
        return triples;
    }

    // purpose: scoring logic for triples (following gamerules)
    // pre-conditions: there are triples in count
    // post-conditions: returns the correct score for any triples
    private int scoreTriples(Hashtable<Integer, Integer> count)
    {
        int tripleScore = 0;

        ArrayList<Integer> triples = findTriples(count);
        if (!triples.isEmpty())
        {
            for (Integer t : triples)
            {
                // if 1 => 1000 pts, else => t * 100 pts
                if (t==1)
                    tripleScore += 1000;
                else
                    tripleScore += t*100;

                // subtracts triple from count hashtable (important for 1 + 5)
                count.put(t, count.get(t)-3);
            }
        }

        return tripleScore;
    }

    // purpose: scoring logic for individual (specifically not triples) 1's and 5's
    // pre-conditions: there are 1's and/or 5's in count
    // post-conditions: returns the correct score for individual 1's and 5's
    private int scoreSingles(Hashtable<Integer, Integer> count)
    {
        int singleScore = 0;

        singleScore += count.get(1) * 100;
        singleScore += count.get(5) * 50;

        return singleScore;
    }
}