import java.util.ArrayList;
import java.util.Hashtable;

//multi-die class
public class SixDice
{
    // FarkleDie objects stored in this multi-die object
    private ArrayList<FarkleDie> rolls;
//    private Hashtable<Integer, Integer> count;

    // purpose: get rolls (dice with values in SixDice object) from outside this class
    // pre-conditions: want to obtain rolls from outside class
    // post-conditions: returns rolls
    public ArrayList<FarkleDie> getRolls()
    {
        return rolls;
    }

    // purpose: set rolls from outside the class
    // pre-conditions: want to change rolls from outside class
    // post-conditions: changes rolls to the input parameter rolls
    public void setRolls(ArrayList<FarkleDie> rolls)
    {
        this.rolls = rolls;
    }

    // purpose: hides algorithm for roll
    // pre-conditions: set of SixDice is rolled
    // post-conditions: calls roll
    public ArrayList<FarkleDie> rollDice(int numRolls)
    {
        return roll(numRolls);
    }

    // purpose: calculates rolls for dice in SixDice
    // pre-conditions: want to roll SixDice; given number of rolls
    // post-conditions: returns rolls of all dice in ArrayList<FarkleDie> form
    private ArrayList<FarkleDie> roll(int numRolls)
    {
        rolls = new ArrayList<FarkleDie>();

        for (int i = 0; i < numRolls; i++)
        {
            FarkleDie die = new FarkleDie();
            die.roll();
            rolls.add(die);
        }
        return rolls;
    }

    // purpose: print formatting for dice
    // pre-conditions: want to print dice values/present to user
    // post-conditions: returns SixDice in a readable format
    public String toString(ArrayList<FarkleDie> rolls)
    {
        StringBuilder s = new StringBuilder();

        for (FarkleDie i: rolls)
        {
            s.append("[").append(i.getValue()).append("] ");
        }

        return s.toString();
    }
}
