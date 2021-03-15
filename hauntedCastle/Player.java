/**
 * Class Player
 * A single object represents the single player.
 * 
 * @author Olaf Chitil and Costi Lacatusu
 * @version 17/2/2020
 */

public class Player extends Character
{
    private final int TIME_LIMIT = 12;
    private int currentTime = 0;
    private Room goal;
    /**
     * Constructor, taking start room and goal room.
     * Pre-condition: start location is not null.
     * Pre-condition: goal is not null.
     */
    public Player(Room start, Room goal)
    {
        super(start);
        assert goal != null: "Goal room is null";
        this.goal = goal;
    }

    /**
     * Check whether time limit has been reached.
     */
    public boolean isAtTimeLimit()
    {
        if (currentTime < TIME_LIMIT){
            return false;
        }
        else{
            return true;
        }
    }

    /**
     * Check whether goal has been reached.
     */
    public boolean isAtGoal()
    {
        if (getLocation() == goal){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Return a description.
     */
    public String toString()
    {
        return "you";
    }

    /**
     * Move the character to a different room
     * Increment the current time by one
     */
    public void move(Room loc){
        super.move(loc);
        currentTime++;
    }

}
