/*
intended behavior:
- will greedily pick highest scoring combination possible each roll
- will end turn if < 3 dice remain
    - will end turn if they have enough points to win the game
    - will end turn if over some score limit (i.e. 2000)
- retains these rules for hot dice
*/

public class FarkleCPU extends FarklePlayer
{
    private String name;
    private final String DEFAULT_NAME = "CPU";

    private int score;
    private int turnScore;
    private SixDice dice;

    public FarkleCPU()
    {
        super();
        this.score = 0;
        this.turnScore = 0;
        this.name = DEFAULT_NAME;
    }

    public FarkleCPU(String name)
    {
        super(name);
        this.score = 0;
        this.turnScore = 0;
        this.name = name;
    }
}