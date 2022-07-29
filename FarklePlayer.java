//model
import java.lang.reflect.Array;
import java.util.ArrayList;


public class FarklePlayer
{
    private String name;
    private final String DEFAULT_NAME = "PLAYER";

    private int score;
    private int turnScore;
    private SixDice dice;

    private boolean endGame = false;    //var to track whether the player wants to end the game

//    private final int DIE = 6;
//    private final int ROLLS_PER_TURN = 6;


    // purpose: constructor for FarklePlayer object
    // pre-conditions: game has started; player needs to be created
    // post-conditions: FarklePlayer is created with zeroed score/turnScore and a default name
    public FarklePlayer()
    {
        this.score = 0;
        this.turnScore = 0;
        this.name = DEFAULT_NAME;
    }

    // purpose: constructor for FarklePlayer with param: String name
    // pre-conditions: game has started; player needs to be created with a custom name
    // post-conditions: FarklePlayer is created with zeroed score/turnScore and a custom name
    public FarklePlayer(String name)
    {
        this.score = 0;
        this.turnScore = 0;
        this.name = name;
    }

    // purpose: get player's name from outside this class
    // pre-conditions: want to obtain player's name from outside class
    // post-conditions: returns a player's name
    public String getName()
    {
        return this.name;
    }

    public void setName(String n)
    {
        this.name = n;
    }

    // purpose: get player's score from outside this class
    // pre-conditions: want to obtain player's score from outside class
    // post-conditions: returns a player's total score
    public int getScore()
    {
        return this.score;
    }

    // purpose: get player's turnScore from outside this class
    // pre-conditions: want to obtain player's turnScore from outside class
    // post-conditions: returns a player's turnScore
    public int getTurnScore()
    {
        return this.turnScore;
    }

    // purpose: get player's dice from outside this class
    // pre-conditions: want to obtain player's dice from outside class
    // post-conditions: returns player's dice
    public SixDice getDice()
    {
        return this.dice;
    }

    // purpose: set player's score from outside the class
    // pre-conditions: want to change a player's score from outside class
    // post-conditions: changes player's score value to the input parameter score
    public void setScore(int score)
    {
        this.score = score;
    }

    // purpose: set player's turnScore from outside the class
    // pre-conditions: want to change a player's turnScore from outside class
    // post-conditions: changes player's turnScore value to the input parameter turnScore
    public void setTurnScore(int turnScore)
    {
        this.turnScore = turnScore;
    }

    // purpose: set player's current dice from outside the class
    // pre-conditions: want to change a player's dice from outside class
    // post-conditions: changes player's dice to input param dice
    public void setDice(SixDice dice)
    {
        this.dice = dice;
    }

    // purpose: get player's endGame from outside the class
    // pre-conditions: need to know if the player wants to end the game
    // post-conditions: returns player's endGame value
    public boolean getEndGame()
    {
        return this.endGame;
    }

    // purpose: set player's endGame from outside the class
    // pre-conditions: want to update the player's endGame value
    // post-conditions: updates the player's endGame
    public void setEndGame(boolean endGame)
    {
        this.endGame = endGame;
    }
}