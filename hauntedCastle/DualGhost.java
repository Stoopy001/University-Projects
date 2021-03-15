import java.util.List;
/**
 * Class DualGhost
 * A dual ghost in the castle.
 * 
 * @author Olaf Chitil and Costi Lacatusu
 * @version 17/2/2020
 */

public class DualGhost extends Ghost
{
    private Room currentRoom;
    /**
     * Constructor initialising location and description.
     * Pre-condition: location not null.
     * Pre-condition: description not null.
     */
    public DualGhost(Room loc, String desc)
    {        
        super(loc, desc); 
        loc.dual();      
    }
   
   
    /**
     * Move ghost to a given room.
     * This involves removing the ghost from the room it was before.
     * Works also if previous and new room are the same.
     * Pre-condition: room is not null.
     * Call the dual function on both the previous and the new room to switch exits.
     */
    public void move(Room loc)
    {
        getLocation().dual();
        super.move(loc);
        getLocation().dual();    
    }

}
