import java.util.List;
import java.util.LinkedList;
import java.util.Collections;

/**
 * Class SolidGhost
 * A solid ghost in the castle.
 * 
 * @author Olaf Chitil and Costi Lacatusu
 * @version 17/2/2020
 */

public class SolidGhost extends Ghost
{
    private Room currentRoom;
    /**
     * Constructor initialising location and description.
     * Pre-condition: location not null.
     * Pre-condition: description not null.
     */
    public SolidGhost(Room loc, String desc)
    {
        super(loc, desc);
    }
        
    /**
     * Go to a random neighbouring room.
     * If there is no neighbour, then stay in current room.
     * @param rooms all rooms available - actually ignored
     */
    public void goRandom(List<Room> rooms)
    {
        currentRoom = this.getLocation();
        super.goRandom(currentRoom.getExits());
        
    }
}
