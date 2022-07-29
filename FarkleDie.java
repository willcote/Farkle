import java.util.ArrayList;

public class FarkleDie
{
    private int sides;
    private Integer value;

    // purpose: default constructor for FarkleDie object
    // pre-conditions: FarkleDie is created somewhere
    // post-conditions: creates a FarkleDie with default value for sides (6)
    public FarkleDie()
    {
        this.sides = 6;
    }

    // purpose: constructor for FarkleDie object
    // pre-conditions: FarkleDie is created somewhere
    // post-conditions: creates a FarkleDie with a value from input param
    public FarkleDie(Integer value)
    {
        this.value = value;
    }

    // purpose: get FarkleDie's sides from outside this class
    // pre-conditions: want to obtain FarkleDie's sides from outside class
    // post-conditions: returns a FarkleDie's sides
    public int getSides()
    {
        return sides;
    }

    // purpose: get FarkleDie's current roll value from outside this class
    // pre-conditions: want to obtain FarkleDie's current roll value from outside class
    // post-conditions: returns a FarkleDie's current roll value
    public Integer getValue()
    {
        return value;
    }

    // purpose: roll simulation for die
    // pre-conditions: dice needs to be rolled
    // post-conditions: returns the outcome of the roll
    public Integer roll()
    {
        //int truncate for random 1-6, Integer cast for arraylist
        this.value = (Integer)(int)(Math.random()*sides+1);
        return this.value;
    }
}